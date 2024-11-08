package com.libraryManagement.libraryManagement.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.libraryManagement.libraryManagement.entities.BorrowingRecord;
import com.libraryManagement.libraryManagement.projections.BorrowingRecordProjection;

@Repository
public interface BorrowingRecordRepository extends CrudRepository<BorrowingRecord, Long> {
	
BorrowingRecord findByBookIdAndPatronIdAndActualReturnDateIsNull(Long bookId, Long patronId);
	
	List<BorrowingRecord> findByBookIdAndActualReturnDateIsNotNull(Long bookId);
	List<BorrowingRecord> findByBookIdAndActualReturnDateIsNull(Long bookId);
	
	List<BorrowingRecord> findByPatronIdAndActualReturnDateIsNotNull(Long patronId);
	List<BorrowingRecord> findByPatronIdAndActualReturnDateIsNull(Long patronId);
	
	List<BorrowingRecord> findAllByActualReturnDateIsNullAndLateReturnIsFalse();
	
	BorrowingRecordProjection findBorrowingRecordById(Long borrowingRecordId);


}
