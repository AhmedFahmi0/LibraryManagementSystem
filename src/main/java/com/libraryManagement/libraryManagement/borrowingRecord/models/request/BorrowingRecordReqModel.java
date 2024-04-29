package com.libraryManagement.libraryManagement.borrowingRecord.models.request;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BorrowingRecordReqModel implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = -6327405169112126983L;
	
    @NotNull(message = "Book ID is required")
    private Long bookId;

    @NotNull(message = "Patron ID is required")
    private Long patronId;
	

}
