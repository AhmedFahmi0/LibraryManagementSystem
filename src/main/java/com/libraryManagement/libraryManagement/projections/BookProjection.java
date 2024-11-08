package com.libraryManagement.libraryManagement.projections;

import java.time.LocalDate;

public interface BookProjection {
	
    Long getId();
    String getTitle();
    String getAuthor();
    LocalDate getPublicationYear();
    String getISBN();
    Boolean getAvailable();

}
