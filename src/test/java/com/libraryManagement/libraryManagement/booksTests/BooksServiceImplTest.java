package com.libraryManagement.libraryManagement.booksTests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.libraryManagement.libraryManagement.books.entities.Books;
import com.libraryManagement.libraryManagement.books.models.mapinterface.BooksMapper;
import com.libraryManagement.libraryManagement.books.models.request.BookReqModel;
import com.libraryManagement.libraryManagement.books.models.response.BookResModel;
import com.libraryManagement.libraryManagement.books.repositories.BooksRepository;
import com.libraryManagement.libraryManagement.books.services.impl.BooksServiceImpl;
import com.libraryManagement.libraryManagement.borrowingRecord.entites.BorrowingRecord;
import com.libraryManagement.libraryManagement.borrowingRecord.repositories.BorrowingRecordRepository;
import com.libraryManagement.libraryManagement.exceptions.ApiErrorMessageKeyEnum;
import com.libraryManagement.libraryManagement.exceptions.BusinessLogicViolationException;

@ExtendWith(MockitoExtension.class)
public class BooksServiceImplTest {
	
    @Mock
    private BooksRepository booksRepository;

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @Mock
    private BooksMapper booksMapper;

    @InjectMocks
    private BooksServiceImpl booksService;
	
    @Test
    void testCreateBook() {
        BookReqModel bookReqModel = new BookReqModel();
        Books book = new Books();
        BookResModel expected = new BookResModel();
        when(booksMapper.mapToBooks(bookReqModel)).thenReturn(book);
        when(booksRepository.save(book)).thenReturn(book);
        when(booksMapper.mapToBookResModel(book)).thenReturn(expected);

        BookResModel actual = booksService.createBook(bookReqModel);

        assertEquals(expected, actual);
        verify(booksMapper).mapToBooks(bookReqModel);
        verify(booksRepository).save(book);
        verify(booksMapper).mapToBookResModel(book);
    }

    @Test
    void testGetBookById() {
        long bookId = 1L;
        Books book = new Books();
        BookResModel expected = new BookResModel();
        when(booksRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(booksMapper.mapToBookResModel(book)).thenReturn(expected);

        BookResModel actual = booksService.getBookById(bookId);

        assertEquals(expected, actual);
        verify(booksRepository).findById(bookId);
        verify(booksMapper).mapToBookResModel(book);
    }
    
    @Test
    void testUpdateBookById() {
        long bookId = 1L;
        BookReqModel bookReqModel = new BookReqModel();
        Books book = new Books();
        BookResModel expected = new BookResModel();
        when(booksRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(booksRepository.save(any(Books.class))).thenReturn(book);
        when(booksMapper.mapToBooks(book, bookReqModel)).thenReturn(book);
        when(booksMapper.mapToBookResModel(book)).thenReturn(expected);

        BookResModel actual = booksService.updateBookById(bookReqModel, bookId);

        assertEquals(expected, actual);
        verify(booksRepository).findById(bookId);
        verify(booksRepository).save(book);
        verify(booksMapper).mapToBooks(book, bookReqModel);
        verify(booksMapper).mapToBookResModel(book);
    }
    
    @Test
    void testGetAllBooks() {
        int pageSize = 10;
        int pageIndex = 0;
        String sortField = "title";
        String sortOrder = "asc";
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(Sort.Direction.ASC, sortField));
        List <Books> booksList = new ArrayList<>();
        Books book1 = new Books();
        book1.setTitle("Book 1");
        booksList.add(book1);
        Books book2 = new Books();
        book2.setTitle("Book 2");
        booksList.add(book2);
        Page <Books> page = new PageImpl<>(booksList, pageable, booksList.size());
        List<BookResModel> expected = new ArrayList<>();
        for (Books book : booksList) {
            expected.add(new BookResModel());
        }
        when(booksRepository.findAll(pageable)).thenReturn(page);
        when(booksMapper.mapToBookResModel(any(Books.class))).thenReturn(new BookResModel());

        List<BookResModel> actual = booksService.getAllBooks(pageSize, pageIndex, sortField, sortOrder);

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
        verify(booksRepository).findAll(pageable);
        verify(booksMapper, times(booksList.size())).mapToBookResModel(any(Books.class));
    }
    
//    @Test
//    void testDeleteBookById_BookFound() {
//        long bookId = 1L;
//        List<BorrowingRecord> borrowingRecord = new ArrayList<>();
//        borrowingRecord.add(new BorrowingRecord()); // Add a borrowing record to simulate a book with borrowing records
//        when(borrowingRecordRepository.findByBookIdAndReturnDateIsNotNull(bookId)).thenReturn(borrowingRecord);
//
//        assertDoesNotThrow(() -> booksService.deleteBookById(bookId));
//        verify(borrowingRecordRepository).findByBookIdAndReturnDateIsNotNull(bookId);
//        verify(borrowingRecordRepository).deleteAll(borrowingRecord); // Verify that borrowing records are deleted
//        verify(booksRepository).deleteById(bookId); // Verify that the book is deleted
//    }

//    @Test
//    void testDeleteBookById_BookNotFound() {
//        long bookId = 1L;
//        when(borrowingRecordRepository.findByBookIdAndReturnDateIsNotNull(bookId)).thenReturn(new ArrayList<>());
//
//        assertThrows(BusinessLogicViolationException.class, () -> booksService.deleteBookById(bookId));
//        verify(borrowingRecordRepository).findByBookIdAndReturnDateIsNotNull(bookId);
//        verify(borrowingRecordRepository, never()).deleteAll(any()); // Verify that borrowing records deletion is not attempted
//        verify(booksRepository).deleteById(bookId); // Verify that the book deletion is attempted
//    }

//    @Test
//    void testDeleteBookById_BookNotFound() {
//        long bookId = 1L;
//        when(booksRepository.findById(bookId)).thenReturn(Optional.empty());
//
//        BusinessLogicViolationException exception = assertThrows(BusinessLogicViolationException.class, () -> booksService.deleteBookById(bookId));
//        assertEquals(ApiErrorMessageKeyEnum.BCV_BOOK_SHOULD_BE_RETURNED.name(), exception.getMessage());
//
//        verify(borrowingRecordRepository, never()).findByBookIdAndReturnDateIsNotNull(bookId);
//        verify(borrowingRecordRepository, never()).deleteAll(any());
//        verify(booksRepository, never()).deleteById(bookId);
//    }
    
    
    @Test
    void testDeleteBookById_BookFoundWithReturnDate() {
        // Arrange
        long bookId = 1L;
        List<BorrowingRecord> borrowingRecordList = new ArrayList<>();
        borrowingRecordList.add(new BorrowingRecord()); // Simulate a borrowing record with a return date
        Books book = new Books(); // Create a book object for the verification
        when(booksRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(borrowingRecordRepository.findByBookIdAndReturnDateIsNotNull(bookId)).thenReturn(borrowingRecordList);

        // Act
        assertDoesNotThrow(() -> booksService.deleteBookById(bookId));

        // Assert
        verify(borrowingRecordRepository).findByBookIdAndReturnDateIsNotNull(bookId);
        verify(borrowingRecordRepository).deleteAll(borrowingRecordList);
        verify(booksRepository).delete(book); // Verify delete(book) instead of deleteById(bookId)
    }



    @Test
    void testDeleteBookById_BookFoundWithUnreturnedRecords() {
        // Arrange
        long bookId = 1L;
        Books book = new Books();
        book.setId(bookId);
        when(booksRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(borrowingRecordRepository.findByBookIdAndReturnDateIsNotNull(bookId)).thenReturn(new ArrayList<>());
        List<BorrowingRecord> unreturnedRecords = new ArrayList<>();
        unreturnedRecords.add(new BorrowingRecord());
        when(borrowingRecordRepository.findByBookIdAndReturnDateIsNull(bookId)).thenReturn(unreturnedRecords);

        // Act & Assert
        assertThrows(BusinessLogicViolationException.class, () -> booksService.deleteBookById(bookId));
        verify(borrowingRecordRepository, never()).deleteAll(any());
        verify(booksRepository, never()).deleteById(any());
    }

    @Test
    void testDeleteBookById_BookNotFound() {
        // Arrange
        long bookId = 1L;
        when(booksRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BusinessLogicViolationException.class, () -> booksService.deleteBookById(bookId));
        verify(borrowingRecordRepository, never()).findByBookIdAndReturnDateIsNotNull(bookId);
        verify(borrowingRecordRepository, never()).deleteAll(any());
        verify(booksRepository, never()).deleteById(any());
    }



}
