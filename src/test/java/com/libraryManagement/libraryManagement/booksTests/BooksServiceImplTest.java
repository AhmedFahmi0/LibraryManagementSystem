package com.libraryManagement.libraryManagement.booksTests;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.libraryManagement.libraryManagement.entities.Books;
import com.libraryManagement.libraryManagement.entities.BorrowingRecord;
import com.libraryManagement.libraryManagement.model.mapper.BooksMapper;
import com.libraryManagement.libraryManagement.model.request.BookReqModel;
import com.libraryManagement.libraryManagement.projections.BookProjection;
import com.libraryManagement.libraryManagement.repositories.BooksRepository;
import com.libraryManagement.libraryManagement.repositories.BorrowingRecordRepository;
import com.libraryManagement.libraryManagement.services.serviceImpl.BooksServiceImpl;
import com.libraryManagement.libraryManagement.exceptions.BusinessLogicViolationException;
import com.libraryManagement.libraryManagement.exceptions.ApiErrorMessageKeyEnum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.cache.CacheManager;
import org.springframework.cache.Cache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class BooksServiceImplTest {

    @Mock
    private BooksRepository booksRepository;

    @Mock
    private BooksMapper booksMapper;

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @InjectMocks
    private BooksServiceImpl booksService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBook() {
        // Arrange
        BookReqModel bookReqModel = new BookReqModel();
        Books book = new Books();
        book.setId(1L);
        BookProjection bookProjection = mock(BookProjection.class);

        when(booksMapper.mapToBooks(bookReqModel)).thenReturn(book);
        when(booksRepository.save(book)).thenReturn(book);
        when(booksRepository.findBookById(1L)).thenReturn(bookProjection);

        // Act
        BookProjection result = booksService.createBook(bookReqModel);

        // Assert
        assertNotNull(result);
        verify(booksRepository, times(1)).save(book);
        verify(booksRepository, times(1)).findBookById(1L);
    }

    @Test
    void testGetBookById() {
        // Arrange
        Long bookId = 1L;
        BookProjection bookProjection = mock(BookProjection.class);
        
        when(booksRepository.findBookById(bookId)).thenReturn(bookProjection);

        // Act
        BookProjection result = booksService.getBookById(bookId);

        // Assert
        assertNotNull(result);
        verify(booksRepository, times(1)).findBookById(bookId);
    }

    @Test
    void testUpdateBookById() {
        // Arrange
        Long bookId = 1L;
        BookReqModel bookReqModel = new BookReqModel();
        Books existingBook = new Books();
        existingBook.setId(bookId);
        Books updatedBook = new Books();
        updatedBook.setId(bookId);
        BookProjection bookProjection = mock(BookProjection.class);

        when(booksRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(booksMapper.mapToBooks(existingBook, bookReqModel)).thenReturn(updatedBook);
        when(booksRepository.save(updatedBook)).thenReturn(updatedBook);
        when(booksRepository.findBookById(bookId)).thenReturn(bookProjection);

        // Act
        BookProjection result = booksService.updateBookById(bookReqModel, bookId);

        // Assert
        assertNotNull(result);
        verify(booksRepository, times(1)).findById(bookId);
        verify(booksRepository, times(1)).save(updatedBook);
    }

    @Test
    void testGetAllBooks() {
        // Arrange
        int pageSize = 10;
        int pageIndex = 0;
        String sortField = "title";
        String sortOrder = "asc";
        Pageable pageable = PageRequest.of(pageIndex, pageSize, 
                        sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());

        // Create mock BookProjection object
        BookProjection bookProjection = mock(BookProjection.class);
        List<BookProjection> bookProjectionList = List.of(bookProjection);
        Page<BookProjection> page = new PageImpl<>(bookProjectionList);

        // Mock the repository method to return the Page<BookProjection>
        when(booksRepository.findAll(pageable)).thenReturn(page);

        // Act
        List<BookProjection> result = booksService.getAllBooks(pageSize, pageIndex, sortField, sortOrder);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());  // since we mocked one BookProjection in the list
        verify(booksRepository, times(1)).findAll(pageable);
    }


    @Test
    void testDeleteBookById_bookNotFound() {
        // Arrange
        Long bookId = 1L;
        when(booksRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessLogicViolationException exception = assertThrows(
            BusinessLogicViolationException.class,
            () -> booksService.deleteBookById(bookId)
        );
        assertEquals(ApiErrorMessageKeyEnum.BCV_BOOK_NOT_FOUND.name(), exception.getMessage());
    }

    @Test
    void testDeleteBookById_withBorrowingRecords() {
        // Arrange
        Long bookId = 1L;
        Books book = new Books();
        book.setId(bookId);

        BorrowingRecord borrowingRecord = new BorrowingRecord();
        List<BorrowingRecord> borrowingRecords = List.of(borrowingRecord);

        when(booksRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(borrowingRecordRepository.findByBookIdAndActualReturnDateIsNull(bookId)).thenReturn(borrowingRecords);

        // Act
        booksService.deleteBookById(bookId);

        // Assert
        verify(borrowingRecordRepository, times(1)).deleteAll(borrowingRecords);
        verify(booksRepository, times(1)).delete(book);
    }

    
}
