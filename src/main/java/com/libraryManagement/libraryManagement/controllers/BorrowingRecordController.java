package com.libraryManagement.libraryManagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.libraryManagement.libraryManagement.projections.BorrowingRecordProjection;
import com.libraryManagement.libraryManagement.services.BorrowingRecordService;

import io.swagger.annotations.ApiOperation;

@RequestMapping("/api/borrow/{bookId}/patron/{patronId}")

@RestController
public class BorrowingRecordController {

	@Autowired 
	private BorrowingRecordService BorrowingRecordService;
	
    @ApiOperation("Allow a patron to borrow a book")
    @PostMapping
    public ResponseEntity<BorrowingRecordProjection> borrowBook(
    		@PathVariable Long bookId, @PathVariable Long patronId) {
        return new ResponseEntity<>(BorrowingRecordService.borrowBook(bookId, patronId)
        		,HttpStatus.OK);
    }

    @ApiOperation("Record the return of a borrowed book by a patron")
    @PutMapping
    public ResponseEntity<BorrowingRecordProjection> returnBook(
    		@PathVariable Long bookId, @PathVariable Long patronId) {
        return new ResponseEntity<>(BorrowingRecordService.returnBook(bookId, patronId)
        		,HttpStatus.OK);
    } 
}
