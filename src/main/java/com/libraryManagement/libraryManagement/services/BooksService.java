package com.libraryManagement.libraryManagement.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.libraryManagement.libraryManagement.model.request.BookReqModel;
import com.libraryManagement.libraryManagement.projections.BookProjection;

@Service
public interface BooksService {

	BookProjection createBook(BookReqModel bookReqModel);

	BookProjection getBookById(Long bookId);

	BookProjection updateBookById(BookReqModel bookReqModel, Long bookId);

	void deleteBookById(Long bookId);

	List<BookProjection> getAllBooks(Integer pageSize, Integer pageIndex, String sortField, String sortOrder);

}
