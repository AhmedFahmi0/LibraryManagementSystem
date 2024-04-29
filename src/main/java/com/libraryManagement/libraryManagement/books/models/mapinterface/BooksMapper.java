package com.libraryManagement.libraryManagement.books.models.mapinterface;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.libraryManagement.libraryManagement.books.entities.Books;
import com.libraryManagement.libraryManagement.books.models.request.BookReqModel;
import com.libraryManagement.libraryManagement.books.models.response.BookResModel;


@Mapper(componentModel = "spring")
public interface BooksMapper {
	
	Books mapToBooks(BookReqModel bookReqModel);
	
	Books mapToBooks(@MappingTarget Books books, BookReqModel bookReqModel);
	
	BookResModel mapToBookResModel(Books books);
 
}
