package com.libraryManagement.libraryManagement.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.libraryManagement.libraryManagement.entities.Books;
import com.libraryManagement.libraryManagement.model.request.BookReqModel;


@Mapper(componentModel = "spring")
public interface BooksMapper {
	
	Books mapToBooks(BookReqModel bookReqModel);
	
	Books mapToBooks(@MappingTarget Books books, BookReqModel bookReqModel);

}
