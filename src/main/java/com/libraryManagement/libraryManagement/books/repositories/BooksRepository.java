package com.libraryManagement.libraryManagement.books.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.libraryManagement.libraryManagement.books.entities.Books;

@Repository
public interface BooksRepository extends CrudRepository<Books, Long> {
	
	
	Page<Books> findAll(Pageable pageable);

}
