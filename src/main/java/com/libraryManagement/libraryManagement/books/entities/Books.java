package com.libraryManagement.libraryManagement.books.entities;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "books")
@Data
public class Books implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -3335829666377057360L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 255)
    private String author;

    @Column(name = "publication_year")
    private LocalDate publicationYear;

    @Column(name = "isbn",nullable = false, unique = true)
    private String ISBN;
    
    @Column(name = "available")
    private Boolean available = true;

}
