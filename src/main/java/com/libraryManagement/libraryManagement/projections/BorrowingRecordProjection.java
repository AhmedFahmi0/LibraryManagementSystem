package com.libraryManagement.libraryManagement.projections;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.libraryManagement.libraryManagement.entities.Books;
import com.libraryManagement.libraryManagement.entities.Patron;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public interface BorrowingRecordProjection {
		Long getId();
	   	BookProjection getBook();
	   	PatronProjection getPatron();
	    LocalDateTime getBorrowingDate();
	    LocalDateTime getActualReturnDate();
	    LocalDateTime getDueReturnDate();
	    
}