package com.libraryManagement.libraryManagement.books.models.request;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookReqModel implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = -8519969674127876759L;
	
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

    @NotBlank(message = "Author is required")
    @Size(max = 255, message = "Author cannot exceed 255 characters")
    private String author;

    @NotNull(message = "Publication date is required")
    private LocalDate publicationYear;

    @NotBlank(message = "ISBN is required")
    @Size(max = 13, min = 10, message = "ISBN must be between 10 and 13 characters")
    @Pattern(regexp = "^[0-9]{10,13}$", message = "ISBN must contain only digits")
    private String ISBN;

}
