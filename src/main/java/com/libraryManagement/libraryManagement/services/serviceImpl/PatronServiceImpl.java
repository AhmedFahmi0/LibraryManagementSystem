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

import com.libraryManagement.libraryManagement.entities.BorrowingRecord;
import com.libraryManagement.libraryManagement.repositories.BorrowingRecordRepository;
import com.libraryManagement.libraryManagement.exceptions.ApiErrorMessageKeyEnum;
import com.libraryManagement.libraryManagement.exceptions.BusinessLogicViolationException;
import com.libraryManagement.libraryManagement.entities.Patron;
import com.libraryManagement.libraryManagement.model.mapper.PatronMapper;
import com.libraryManagement.libraryManagement.model.request.PatronReqModel;
import com.libraryManagement.libraryManagement.projections.PatronProjection;
import com.libraryManagement.libraryManagement.repositories.PatronRepository;
import com.libraryManagement.libraryManagement.services.PatronService;

import jakarta.transaction.Transactional;


@Component
@Transactional
public class PatronServiceImpl implements PatronService {
	
	@Autowired
	private PatronRepository patronRepository;
	
	@Autowired
	private PatronMapper patronMapper;
	
    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;
	
	@Override
	@Transactional
	@CacheEvict(value= {"getPatronById","getAllPatrons"},key="#root.getAllPatrons",allEntries=true)
	public PatronProjection createPatron(PatronReqModel patronReqModel) {
	    Patron patron = patronMapper.mapToPatron(patronReqModel);
	    patron = patronRepository.save(patron);
	    return patronRepository.findPatronById(patron.getId());
	}

	@Override
	@Cacheable(value="getPatronById", key="#patronId")
	public PatronProjection getPatronById(Long patronId) {
		return patronRepository.findPatronById(patronId);
	}
	
	@Override
	@Transactional
	@CacheEvict(value= {"getPatronById","getAllPatrons"},key="#root.updatePatronById",allEntries=true)
	public PatronProjection updatePatronById(PatronReqModel patronReqModel , Long patronId) {
		Patron patron = patronRepository.findById(patronId).get();
		Patron updatedPatron = patronMapper.mapToPatron(patron, patronReqModel);
		patronRepository.save(updatedPatron);
		return patronRepository.findPatronById(patronId);
	}
	
	
	@Override
	@Cacheable(value="getAllPatrons",key="#root.methodName + '_' + #pageSize + '_' + #pageIndex + '_' + #sortField + '_' + #sortOrder")
	public List<PatronProjection> getAllPatrons(Integer pageSize, Integer pageIndex, String sortField, String sortOrder) {
	    Pageable pageable = null;
	    if (sortField != null && !sortField.isBlank() && sortOrder != null && !sortOrder.isBlank()) {
	        pageable = PageRequest.of(pageIndex, pageSize,
					sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending()
							: Sort.by(sortField).descending());
	    } else {
	        pageable = PageRequest.of(pageIndex, pageSize);
	    }
	    
	    Page<PatronProjection> patronPage = patronRepository.findAll(pageable);
	    
	    return patronPage.getContent();
	}
	
	@Override
	@CacheEvict(value= {"getPatronById","getAllPatrons"},key="#root.deletePatronById",allEntries=true)
	public void deletePatronById(Long patronId) {
	    Optional<Patron> optionalPatron = patronRepository.findById(patronId);
	    if (optionalPatron.isPresent()) {
	        Patron patron = optionalPatron.get();
	        List<BorrowingRecord> borrowingRecords = borrowingRecordRepository.findByPatronIdAndActualReturnDateIsNotNull(patronId);
	        if (!borrowingRecords.isEmpty()) {
	            borrowingRecordRepository.deleteAll(borrowingRecords);
	            patronRepository.delete(patron);
	        } else {
	            List<BorrowingRecord> unreturnedRecords = borrowingRecordRepository.findByPatronIdAndActualReturnDateIsNull(patronId);
	            if (!unreturnedRecords.isEmpty()) {
	                throw new BusinessLogicViolationException(ApiErrorMessageKeyEnum.BCV_PATRON_HAS_UNRETURNED_BOOKS.name());
	            } else {
	                patronRepository.delete(patron);
	            }
	        }
	    } else {
	        throw new BusinessLogicViolationException(ApiErrorMessageKeyEnum.BCV_PATRON_NOT_FOUND.name());
	    }
	}



}
