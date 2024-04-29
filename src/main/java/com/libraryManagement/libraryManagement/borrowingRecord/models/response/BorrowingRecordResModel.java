package com.libraryManagement.libraryManagement.borrowingRecord.models.response;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingRecordResModel implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -371276903866023007L;
	
    private Long id;
    private Long bookId;
    private Long patronId;
    private LocalDateTime borrowingDate;
    private LocalDateTime returnDate;

}
