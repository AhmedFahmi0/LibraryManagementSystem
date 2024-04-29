package com.libraryManagement.libraryManagement.borrowingRecord.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.libraryManagement.libraryManagement.borrowingRecord.entites.BorrowingRecord;

@Repository
public interface BorrowingRecordRepository extends CrudRepository<BorrowingRecord, Long> {
	
	BorrowingRecord findByBookIdAndPatronIdAndReturnDateIsNull(Long bookId, Long patronId);
	
	List<BorrowingRecord> findByBookIdAndReturnDateIsNotNull(Long bookId);
	List<BorrowingRecord> findByBookIdAndReturnDateIsNull(Long bookId);
	
	List<BorrowingRecord> findByPatronIdAndReturnDateIsNotNull(Long patronId);
	List<BorrowingRecord> findByPatronIdAndReturnDateIsNull(Long patronId);

}
