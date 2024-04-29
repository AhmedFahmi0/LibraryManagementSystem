package com.libraryManagement.libraryManagement.books.models.response;

import java.io.Serializable;

import lombok.Data;

@Data
public class BookResModel implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 5097141573295156068L;
	
    private Long id;
    private String title;
    private String author;
    private String publicationYear;
    private String ISBN;

}
