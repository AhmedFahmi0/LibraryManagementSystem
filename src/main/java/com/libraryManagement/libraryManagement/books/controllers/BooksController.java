package com.libraryManagement.libraryManagement.books.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.libraryManagement.libraryManagement.books.models.request.BookReqModel;
import com.libraryManagement.libraryManagement.books.models.response.BookResModel;
import com.libraryManagement.libraryManagement.books.services.BooksService;

import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;

@RequestMapping("/api/books")

@RestController
public class BooksController {
	
	@Autowired 
	private BooksService booksService;
	
	@ApiOperation("create book")
	@PostMapping
	public ResponseEntity<BookResModel> createBook(@Valid @RequestBody BookReqModel bookReqModel) {
		return new ResponseEntity<>(booksService.createBook(bookReqModel), 
      HttpStatus.OK);
		
	}
	

	@ApiOperation("get book by id")
	@GetMapping("/{id}")
	public ResponseEntity<BookResModel> getBookById(
			@PathVariable("id") Long bookId) {
		return new ResponseEntity<>(booksService.getBookById(bookId), 
				HttpStatus.OK);
	}
	
	@ApiOperation("get book by id")
	@PutMapping("/{id}")
	public ResponseEntity<BookResModel> updateBookById(
			@Valid @RequestBody BookReqModel bookReqModel,
			@PathVariable("id") Long bookId) {
		return new ResponseEntity<>(booksService.updateBookById(bookReqModel,bookId), 
				HttpStatus.OK);
	}
	
	
	@ApiOperation("get all books") 
	@GetMapping
	public ResponseEntity<List<BookResModel>> getAllBooks(
			@RequestParam(defaultValue = "10",required = false)  Integer pageSize,
			@RequestParam(defaultValue = "0",required = false)  Integer pageIndex,
			@RequestParam(required = false) String sortField, 
			@RequestParam(required = false) String sortOrder) {
		return new ResponseEntity<>(booksService.getAllBooks(pageSize,pageIndex, sortField, sortOrder), 
				HttpStatus.OK);
		
	}
	
	
	@ApiOperation("delete book by id")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBookById(
			@PathVariable("id") Long bookId) {
		booksService.deleteBookById(bookId);
		return new ResponseEntity<>(HttpStatus.OK);
		
	}


}
