package com.libraryManagement.libraryManagement.services.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.libraryManagement.libraryManagement.entities.Books;
import com.libraryManagement.libraryManagement.repositories.BooksRepository;
import com.libraryManagement.libraryManagement.entities.BorrowingRecord;
import com.libraryManagement.libraryManagement.model.mapper.BorrowingRecordMapper;
import com.libraryManagement.libraryManagement.projections.BorrowingRecordProjection;
import com.libraryManagement.libraryManagement.repositories.BorrowingRecordRepository;
import com.libraryManagement.libraryManagement.services.BorrowingRecordService;
import com.libraryManagement.libraryManagement.services.TaskService;
import com.libraryManagement.libraryManagement.exceptions.ApiErrorMessageKeyEnum;
import com.libraryManagement.libraryManagement.exceptions.BusinessLogicViolationException;
import com.libraryManagement.libraryManagement.entities.Patron;
import com.libraryManagement.libraryManagement.repositories.PatronRepository;

import jakarta.transaction.Transactional;

@Component
@Transactional
public class BorrowingRecordServiceImpl implements BorrowingRecordService {
	
    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;
    
    @Autowired
    private BorrowingRecordMapper borrowingRecordMapper;
    
    @Autowired
    private BooksRepository booksRepository;
    
    @Autowired
    private PatronRepository patronRepository;
    
    @Autowired
    private TaskService taskService;

	@Transactional
	@Override
	public BorrowingRecordProjection borrowBook(Long bookId, Long patronId) {		
		Optional <Books> optionalBook = booksRepository.findById(bookId);
		BorrowingRecord borrowingRecord = new BorrowingRecord();
	    if (optionalBook.isPresent()) {
	        Books book = optionalBook.get();
	        Patron patron = patronRepository.findById(patronId).orElseThrow(() ->
	                new BusinessLogicViolationException(ApiErrorMessageKeyEnum.BCV_PATRON_NOT_FOUND.name()));

	        if (book.getAvailable()) {
		            borrowingRecord.setBook(book);
		            borrowingRecord.setPatron(patron);
		            borrowingRecord.setBorrowingDate(LocalDateTime.now());
		            borrowingRecord.setDueReturnDate(borrowingRecord.getBorrowingDate().plusWeeks(2));
		            borrowingRecordRepository.save(borrowingRecord);
		            book.setAvailable(false);
		            booksRepository.save(book);
	        } else {
	            throw new BusinessLogicViolationException(ApiErrorMessageKeyEnum.BCV_BOOK_IS_NOT_AVAILABLE.name());
	        }
	    } else {
	        throw new BusinessLogicViolationException(ApiErrorMessageKeyEnum.BCV_BOOK_NOT_FOUND.name());
	    }
	    return borrowingRecordRepository.findBorrowingRecordById(borrowingRecord.getId());
	}

	@Transactional
	@Override
	public BorrowingRecordProjection returnBook(Long bookId, Long patronId) {
		 BorrowingRecord borrowingRecord = borrowingRecordRepository.findByBookIdAndPatronIdAndActualReturnDateIsNull(bookId, patronId);

	        if (borrowingRecord != null) {
	            borrowingRecord.setActualReturnDate(LocalDateTime.now());
	            borrowingRecord=borrowingRecordRepository.save(borrowingRecord);
	            Books book = borrowingRecord.getBook();
	            book.setAvailable(true);
	            booksRepository.save(book);
	        }
	        else {
	        	throw new BusinessLogicViolationException(ApiErrorMessageKeyEnum.BCV_BOOK_HAS_ALREADY_BEEN_RETURNED.name());
	        }
		return borrowingRecordRepository.findBorrowingRecordById(borrowingRecord.getId());
	}
	
	@Transactional
	@Scheduled(cron = "0 21 20 * * *")
	public void scheduledTask() {
		List<BorrowingRecord> borrowingRecords = borrowingRecordRepository.findAllByActualReturnDateIsNullAndLateReturnIsFalse();
		LocalDateTime now = LocalDateTime.now();
		borrowingRecords.stream()
		.filter(b -> b.getDueReturnDate().isBefore(now))
        .forEach(b -> b.setLateReturn(true));

		borrowingRecordRepository.saveAll(borrowingRecords);
		
		String taskName = taskService.generateTaskName("Update Late Return Status");
        taskService.saveTask(taskName);
	}

}
