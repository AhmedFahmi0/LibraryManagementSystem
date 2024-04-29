package com.libraryManagement.libraryManagement.borrowingRecord.services.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.libraryManagement.libraryManagement.books.entities.Books;
import com.libraryManagement.libraryManagement.books.repositories.BooksRepository;
import com.libraryManagement.libraryManagement.borrowingRecord.entites.BorrowingRecord;
import com.libraryManagement.libraryManagement.borrowingRecord.models.mapinterface.BorrowingRecordMapper;
import com.libraryManagement.libraryManagement.borrowingRecord.models.response.BorrowingRecordResModel;
import com.libraryManagement.libraryManagement.borrowingRecord.repositories.BorrowingRecordRepository;
import com.libraryManagement.libraryManagement.borrowingRecord.services.BorrowingRecordService;
import com.libraryManagement.libraryManagement.exceptions.ApiErrorMessageKeyEnum;
import com.libraryManagement.libraryManagement.exceptions.BusinessLogicViolationException;
import com.libraryManagement.libraryManagement.patron.entities.Patron;
import com.libraryManagement.libraryManagement.patron.repositories.PatronRepository;

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

	@Override
	public BorrowingRecordResModel borrowBook(Long bookId, Long patronId) {
		BorrowingRecordResModel borrowingRecordResModel = new BorrowingRecordResModel();		
		Optional <Books> optionalBook = booksRepository.findById(bookId);
	    if (optionalBook.isPresent()) {
	        Books book = optionalBook.get();
	        Patron patron = patronRepository.findById(patronId).orElseThrow(() ->
	                new BusinessLogicViolationException(ApiErrorMessageKeyEnum.BCV_PATRON_NOT_FOUND.name()));

	        if (book.getAvailable()) {
	            BorrowingRecord borrowingRecord = new BorrowingRecord();
	            borrowingRecord.setBook(book);
	            borrowingRecord.setPatron(patron);
	            borrowingRecord.setBorrowingDate(LocalDateTime.now());

	            borrowingRecordResModel = borrowingRecordMapper.mapToBorrowingRecordResModel(borrowingRecordRepository.save(borrowingRecord));
	            book.setAvailable(false);
	            booksRepository.save(book);
	        } else {
	            throw new BusinessLogicViolationException(ApiErrorMessageKeyEnum.BCV_BOOK_IS_NOT_AVAILABLE.name());
	        }
	    } else {
	        throw new BusinessLogicViolationException(ApiErrorMessageKeyEnum.BCV_BOOK_NOT_FOUND.name());
	    }
	    return borrowingRecordResModel;
	}

	@Override
	public BorrowingRecordResModel returnBook(Long bookId, Long patronId) {
		 BorrowingRecordResModel borrowingRecordResModel = new BorrowingRecordResModel();
		 BorrowingRecord borrowingRecord = borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(bookId, patronId);

	        if (borrowingRecord != null) {
	            borrowingRecord.setReturnDate(LocalDateTime.now());
	            borrowingRecordResModel = borrowingRecordMapper.mapToBorrowingRecordResModel(borrowingRecordRepository.save(borrowingRecord));
	            Books book = borrowingRecord.getBook();
	            book.setAvailable(true);
	            booksRepository.save(book);
	        }
	        else {
	        	throw new BusinessLogicViolationException(ApiErrorMessageKeyEnum.BCV_BOOK_HAS_ALREADY_BEEN_RETURNED.name());
	        }
		return borrowingRecordResModel;
	}

}
