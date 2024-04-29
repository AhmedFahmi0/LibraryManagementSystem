package com.libraryManagement.libraryManagement.borrowingRecord.services;

import org.springframework.stereotype.Service;

import com.libraryManagement.libraryManagement.borrowingRecord.models.response.BorrowingRecordResModel;

@Service
public interface BorrowingRecordService {

	BorrowingRecordResModel borrowBook(Long bookId, Long patronId);

	BorrowingRecordResModel returnBook(Long bookId, Long patronId);

}
