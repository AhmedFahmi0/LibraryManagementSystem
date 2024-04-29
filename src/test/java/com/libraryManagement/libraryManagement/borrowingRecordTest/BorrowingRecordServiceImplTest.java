package com.libraryManagement.libraryManagement.borrowingRecordTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.libraryManagement.libraryManagement.books.entities.Books;
import com.libraryManagement.libraryManagement.books.repositories.BooksRepository;
import com.libraryManagement.libraryManagement.borrowingRecord.entites.BorrowingRecord;
import com.libraryManagement.libraryManagement.borrowingRecord.models.mapinterface.BorrowingRecordMapper;
import com.libraryManagement.libraryManagement.borrowingRecord.models.response.BorrowingRecordResModel;
import com.libraryManagement.libraryManagement.borrowingRecord.repositories.BorrowingRecordRepository;
import com.libraryManagement.libraryManagement.borrowingRecord.services.impl.BorrowingRecordServiceImpl;
import com.libraryManagement.libraryManagement.exceptions.BusinessLogicViolationException;
import com.libraryManagement.libraryManagement.patron.entities.Patron;
import com.libraryManagement.libraryManagement.patron.repositories.PatronRepository;


@SpringBootTest
public class BorrowingRecordServiceImplTest {
	
	@Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @Mock
    private BorrowingRecordMapper borrowingRecordMapper;

    @Mock
    private BooksRepository booksRepository;

    @Mock
    private PatronRepository patronRepository;

    @InjectMocks
    private BorrowingRecordServiceImpl borrowingRecordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBorrowBook_BookAvailable() {
        long bookId = 1L;
        long patronId = 1L;
        Books book = new Books();
        book.setAvailable(true);
        Patron patron = new Patron();
        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowingDate(LocalDateTime.now());
        BorrowingRecordResModel expected = new BorrowingRecordResModel();
        when(booksRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(patronRepository.findById(patronId)).thenReturn(Optional.of(patron));
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(borrowingRecord);
        when(borrowingRecordMapper.mapToBorrowingRecordResModel(borrowingRecord)).thenReturn(expected);

        BorrowingRecordResModel actual = borrowingRecordService.borrowBook(bookId, patronId);

        assertEquals(expected, actual);
    }

    @Test
    void testBorrowBook_BookNotAvailable() {
        long bookId = 1L;
        long patronId = 1L;
        Books book = new Books();
        book.setAvailable(false);
        when(booksRepository.findById(bookId)).thenReturn(Optional.of(book));

        assertThrows(BusinessLogicViolationException.class, () -> borrowingRecordService.borrowBook(bookId, patronId));
    }

    @Test
    void testReturnBook_BookAlreadyReturned() {
        long bookId = 1L;
        long patronId = 1L;
        when(borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(bookId, patronId)).thenReturn(null);

        assertThrows(BusinessLogicViolationException.class, () -> borrowingRecordService.returnBook(bookId, patronId));
    }

    @Test
    void testReturnBook_BookNotAlreadyReturned() {
        long bookId = 1L;
        long patronId = 1L;
        Books book = new Books();
        book.setAvailable(false);
        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        when(borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(bookId, patronId)).thenReturn(borrowingRecord);

        borrowingRecordService.returnBook(bookId, patronId);

        assertEquals(true, book.getAvailable());
    }

}
