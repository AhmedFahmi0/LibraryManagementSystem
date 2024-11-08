package com.libraryManagement.libraryManagement.patronTests;

import com.libraryManagement.libraryManagement.entities.Patron;
import com.libraryManagement.libraryManagement.entities.BorrowingRecord;
import com.libraryManagement.libraryManagement.exceptions.ApiErrorMessageKeyEnum;
import com.libraryManagement.libraryManagement.exceptions.BusinessLogicViolationException;
import com.libraryManagement.libraryManagement.model.mapper.PatronMapper;
import com.libraryManagement.libraryManagement.model.request.PatronReqModel;
import com.libraryManagement.libraryManagement.projections.PatronProjection;
import com.libraryManagement.libraryManagement.repositories.PatronRepository;
import com.libraryManagement.libraryManagement.repositories.BorrowingRecordRepository;
import com.libraryManagement.libraryManagement.services.PatronService;
import com.libraryManagement.libraryManagement.services.serviceImpl.PatronServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PatronServiceImplTest {

    @Mock
    private PatronRepository patronRepository;

    @Mock
    private PatronMapper patronMapper;

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @Mock
    private CacheManager cacheManager;

    @InjectMocks
    private PatronServiceImpl patronService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

   
    @Test
    void testCreatePatron() {
        // Arrange
        PatronReqModel patronReqModel = new PatronReqModel();
        Patron patron = new Patron();
        patron.setId(1L);
        PatronProjection patronProjection = mock(PatronProjection.class);

        when(patronMapper.mapToPatron(patronReqModel)).thenReturn(patron);
        when(patronRepository.save(patron)).thenReturn(patron);
        when(patronRepository.findPatronById(1L)).thenReturn(patronProjection);

        
        PatronProjection result = patronService.createPatron(patronReqModel);

        
        assertNotNull(result);
        verify(patronRepository, times(1)).save(patron);
        verify(patronRepository, times(1)).findPatronById(1L);
    }

  
    @Test
    void testGetPatronById() {
        // Arrange
        Long patronId = 1L;
        PatronProjection patronProjection = mock(PatronProjection.class);

        when(patronRepository.findPatronById(patronId)).thenReturn(patronProjection);

        
        PatronProjection result = patronService.getPatronById(patronId);

        
        assertNotNull(result);
        verify(patronRepository, times(1)).findPatronById(patronId);
    }

 
    @Test
    void testUpdatePatronById() {
        // Arrange
        Long patronId = 1L;
        PatronReqModel patronReqModel = new PatronReqModel();
        Patron existingPatron = new Patron();
        existingPatron.setId(patronId);
        Patron updatedPatron = new Patron();
        updatedPatron.setId(patronId);
        PatronProjection patronProjection = mock(PatronProjection.class);

        when(patronRepository.findById(patronId)).thenReturn(Optional.of(existingPatron));
        when(patronMapper.mapToPatron(existingPatron, patronReqModel)).thenReturn(updatedPatron);
        when(patronRepository.save(updatedPatron)).thenReturn(updatedPatron);
        when(patronRepository.findPatronById(patronId)).thenReturn(patronProjection);

        
        PatronProjection result = patronService.updatePatronById(patronReqModel, patronId);

        
        assertNotNull(result);
        verify(patronRepository, times(1)).findById(patronId);
        verify(patronRepository, times(1)).save(updatedPatron);
    }

  
    @Test
    void testGetAllPatrons() {
        // Arrange
        int pageSize = 10;
        int pageIndex = 0;
        String sortField = "name";
        String sortOrder = "asc";
        Pageable pageable = PageRequest.of(pageIndex, pageSize, 
                    sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());
        
        PatronProjection patronProjection = mock(PatronProjection.class);
        List<PatronProjection> patronProjectionList = List.of(patronProjection);
        Page<PatronProjection> patronPage = new PageImpl<>(patronProjectionList);

        when(patronRepository.findAll(pageable)).thenReturn(patronPage);

        
        List<PatronProjection> result = patronService.getAllPatrons(pageSize, pageIndex, sortField, sortOrder);

        
    }
}
       
