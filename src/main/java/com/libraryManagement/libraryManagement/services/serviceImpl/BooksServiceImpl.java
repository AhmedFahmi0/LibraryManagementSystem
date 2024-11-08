package com.libraryManagement.libraryManagement.services.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.libraryManagement.libraryManagement.entities.Books;
import com.libraryManagement.libraryManagement.model.mapper.BooksMapper;
import com.libraryManagement.libraryManagement.model.request.BookReqModel;
import com.libraryManagement.libraryManagement.projections.BookProjection;
import com.libraryManagement.libraryManagement.repositories.BooksRepository;
import com.libraryManagement.libraryManagement.services.BooksService;
import com.libraryManagement.libraryManagement.entities.BorrowingRecord;
import com.libraryManagement.libraryManagement.repositories.BorrowingRecordRepository;
import com.libraryManagement.libraryManagement.exceptions.ApiErrorMessageKeyEnum;
import com.libraryManagement.libraryManagement.exceptions.BusinessLogicViolationException;

import jakarta.transaction.Transactional;


@Component
@Transactional
public class BooksServiceImpl implements BooksService {
	
	@Autowired
	private BooksRepository booksRepository;
	
	@Autowired
	private BooksMapper booksMapper;
	
    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;
	
	@Override
	@Transactional
	@CacheEvict(value= {"getBookById","getAllBooks"},key="#root.getAllBooks",allEntries=true)
	public BookProjection createBook(BookReqModel bookReqModel) {
	    Books book = booksMapper.mapToBooks(bookReqModel);
	    book = booksRepository.save(book);
	    return booksRepository.findBookById(book.getId());
	}

	@Override
	@Cacheable(value="getBookById", key="#bookId")
	public BookProjection getBookById(Long bookId) {
		return booksRepository.findBookById(bookId);
	}
	
	@Override
	@Transactional
	@CacheEvict(value= {"getBookById","getAllBooks"},key="#root.updateBookById",allEntries=true)
	public BookProjection updateBookById(BookReqModel bookReqModel , Long bookId) {
		Books book = booksRepository.findById(bookId).get();
		Books updatedBook = booksMapper.mapToBooks(book, bookReqModel);
		booksRepository.save(updatedBook);
		return booksRepository.findBookById(bookId);
	}
	
	
	@Override
	@Cacheable(value="getAllBooks", key="#root.methodName + '_' + #pageSize + '_' + #pageIndex + '_' + #sortField + '_' + #sortOrder")
	public List<BookProjection> getAllBooks(Integer pageSize, Integer pageIndex, String sortField, String sortOrder) {
	    Pageable pageable = null;
	    if (sortField != null && !sortField.isBlank() && sortOrder != null && !sortOrder.isBlank()) {
	        pageable = PageRequest.of(pageIndex, pageSize,
					sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending()
							: Sort.by(sortField).descending());
	    } else {
	        pageable = PageRequest.of(pageIndex, pageSize);
	    }
	    
	    return booksRepository.findAll(pageable).getContent();
	}
	
	@Override
	@CacheEvict(value= {"getBookById","getAllBooks"},key="#root.deleteBookById",allEntries=true)
	public void deleteBookById(Long bookId) {
		Optional<Books> optionalBook = booksRepository.findById(bookId);
		if (optionalBook.isPresent()) {
			Books book = optionalBook.get();
			List<BorrowingRecord> borrowingRecords = borrowingRecordRepository.findByBookIdAndActualReturnDateIsNull(bookId);
			if (!borrowingRecords.isEmpty()) {
				borrowingRecordRepository.deleteAll(borrowingRecords);
				booksRepository.delete(book);
			} else {
				List<BorrowingRecord> unreturnedRecords = borrowingRecordRepository.findByBookIdAndActualReturnDateIsNull(bookId);
				if (!unreturnedRecords.isEmpty()) {
					throw new BusinessLogicViolationException(ApiErrorMessageKeyEnum.BCV_BOOK_SHOULD_BE_RETURNED.name());
				} else {
					booksRepository.delete(book);
				}
			}
		} else {
			throw new BusinessLogicViolationException(ApiErrorMessageKeyEnum.BCV_BOOK_NOT_FOUND.name());
			}
		}
}
