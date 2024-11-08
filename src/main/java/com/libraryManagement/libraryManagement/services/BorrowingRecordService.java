package com.libraryManagement.libraryManagement.services;

import org.springframework.stereotype.Service;


import com.libraryManagement.libraryManagement.projections.BorrowingRecordProjection;

@Service
public interface BorrowingRecordService {

	BorrowingRecordProjection borrowBook(Long bookId, Long patronId);

	BorrowingRecordProjection returnBook(Long bookId, Long patronId);

}
