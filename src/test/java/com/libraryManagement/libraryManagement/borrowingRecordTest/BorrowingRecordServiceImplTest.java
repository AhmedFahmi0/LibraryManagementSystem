package com.libraryManagement.libraryManagement.borrowingRecordTest;

import com.libraryManagement.libraryManagement.entities.Books;
import com.libraryManagement.libraryManagement.entities.BorrowingRecord;
import com.libraryManagement.libraryManagement.entities.Patron;
import com.libraryManagement.libraryManagement.exceptions.ApiErrorMessageKeyEnum;
import com.libraryManagement.libraryManagement.exceptions.BusinessLogicViolationException;
import com.libraryManagement.libraryManagement.projections.BorrowingRecordProjection;
import com.libraryManagement.libraryManagement.repositories.BorrowingRecordRepository;
import com.libraryManagement.libraryManagement.repositories.BooksRepository;
import com.libraryManagement.libraryManagement.repositories.PatronRepository;
import com.libraryManagement.libraryManagement.services.TaskService;
import com.libraryManagement.libraryManagement.services.serviceImpl.BorrowingRecordServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BorrowingRecordServiceImplTest {

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @Mock
    private BooksRepository booksRepository;

    @Mock
    private PatronRepository patronRepository;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private BorrowingRecordServiceImpl borrowingRecordService;

    private Long bookId;
    private Long patronId;
    private Books book;
    private Patron patron;
    private BorrowingRecord borrowingRecord;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        bookId = 1L;
        patronId = 1L;
        book = new Books();
        book.setId(bookId);
        book.setAvailable(true);

        patron = new Patron();
        patron.setId(patronId);

        borrowingRecord = new BorrowingRecord();
        borrowingRecord.setId(1L);
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowingDate(LocalDateTime.now());
        borrowingRecord.setDueReturnDate(borrowingRecord.getBorrowingDate().plusWeeks(2));
    }

    @Test
    void testBorrowBook_whenBookIsAvailable() {
        
        book.setAvailable(true);  

        
        when(booksRepository.findById(bookId)).thenReturn(Optional.of(book)); 
        when(patronRepository.findById(patronId)).thenReturn(Optional.of(patron)); 

        
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(borrowingRecord); 

       
        BorrowingRecordProjection mockProjection = mock(BorrowingRecordProjection.class);
        when(borrowingRecordRepository.findBorrowingRecordById(borrowingRecord.getId())).thenReturn(mockProjection);

        
        BorrowingRecordProjection result = borrowingRecordService.borrowBook(bookId, patronId); 

        verify(booksRepository, times(1)).save(book);  

       
        verify(borrowingRecordRepository, times(1)).save(any(BorrowingRecord.class));
    }





    
    @Test
    void testBorrowBook_whenBookIsNotAvailable() {
      
        book.setAvailable(false);
        when(booksRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(patronRepository.findById(patronId)).thenReturn(Optional.of(patron));

        BusinessLogicViolationException exception = assertThrows(
            BusinessLogicViolationException.class,
            () -> borrowingRecordService.borrowBook(bookId, patronId)
        );
        assertEquals(ApiErrorMessageKeyEnum.BCV_BOOK_IS_NOT_AVAILABLE.name(), exception.getMessage());
    }

   
    @Test
    void testBorrowBook_whenBookIsNotFound() {
        
        when(booksRepository.findById(bookId)).thenReturn(Optional.empty());

        
        BusinessLogicViolationException exception = assertThrows(
            BusinessLogicViolationException.class,
            () -> borrowingRecordService.borrowBook(bookId, patronId)
        );
        assertEquals(ApiErrorMessageKeyEnum.BCV_BOOK_NOT_FOUND.name(), exception.getMessage());
    }

  
    @Test
    void testBorrowBook_whenPatronIsNotFound() {
       
        when(booksRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(patronRepository.findById(patronId)).thenReturn(Optional.empty());

        BusinessLogicViolationException exception = assertThrows(
            BusinessLogicViolationException.class,
            () -> borrowingRecordService.borrowBook(bookId, patronId)
        );
        assertEquals(ApiErrorMessageKeyEnum.BCV_PATRON_NOT_FOUND.name(), exception.getMessage());
    }

    
    @Test
    void testReturnBook_whenBookHasBeenBorrowed() {
       
        when(borrowingRecordRepository.findByBookIdAndPatronIdAndActualReturnDateIsNull(bookId, patronId))
            .thenReturn(borrowingRecord); 
        when(borrowingRecordRepository.save(any(BorrowingRecord.class)))
            .thenReturn(borrowingRecord);  
        when(booksRepository.save(any(Books.class)))
            .thenReturn(book);  
        when(borrowingRecordRepository.findBorrowingRecordById(borrowingRecord.getId()))
            .thenReturn(mock(BorrowingRecordProjection.class)); 

        
        BorrowingRecordProjection result = borrowingRecordService.returnBook(bookId, patronId);

        
        assertNotNull(result);  
        verify(booksRepository, times(1)).save(book);  
        verify(borrowingRecordRepository, times(1)).save(borrowingRecord);  
    }


   
    @Test
    void testReturnBook_whenBookHasAlreadyBeenReturned() {
      
        borrowingRecord.setActualReturnDate(LocalDateTime.now());
        when(borrowingRecordRepository.findByBookIdAndPatronIdAndActualReturnDateIsNull(bookId, patronId))
            .thenReturn(null);

 
        BusinessLogicViolationException exception = assertThrows(
            BusinessLogicViolationException.class,
            () -> borrowingRecordService.returnBook(bookId, patronId)
        );
        assertEquals(ApiErrorMessageKeyEnum.BCV_BOOK_HAS_ALREADY_BEEN_RETURNED.name(), exception.getMessage());
    }

   
    @Test
    void testScheduledTask() {
      
        BorrowingRecord lateBorrowingRecord = new BorrowingRecord();
        lateBorrowingRecord.setDueReturnDate(LocalDateTime.now().minusDays(1)); 
        lateBorrowingRecord.setLateReturn(false);
        List<BorrowingRecord> borrowingRecords = List.of(lateBorrowingRecord);
        when(borrowingRecordRepository.findAllByActualReturnDateIsNullAndLateReturnIsFalse()).thenReturn(borrowingRecords);
        when(borrowingRecordRepository.saveAll(borrowingRecords)).thenReturn(borrowingRecords);
        when(taskService.generateTaskName(anyString())).thenReturn("Update Late Return Status");
        
        
        borrowingRecordService.scheduledTask();

        
        assertTrue(lateBorrowingRecord.getLateReturn());  
        verify(taskService, times(1)).saveTask("Update Late Return Status");
    }

    
    @Test
    void testScheduledTask_whenNoLateReturns() {
    	List<BorrowingRecord> borrowingRecords = List.of();
        when(borrowingRecordRepository.findAllByActualReturnDateIsNullAndLateReturnIsFalse()).thenReturn(borrowingRecords);

        
        borrowingRecordService.scheduledTask();

        
        verify(borrowingRecordRepository, times(1)).findAllByActualReturnDateIsNullAndLateReturnIsFalse();
    }
}
