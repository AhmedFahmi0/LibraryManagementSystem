package com.libraryManagement.libraryManagement.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.libraryManagement.libraryManagement.entities.Books;
import com.libraryManagement.libraryManagement.projections.BookProjection;

@Repository
public interface BooksRepository extends CrudRepository<Books, Long> {
	
	
	Page<BookProjection> findAll(Pageable pageable);
	
	BookProjection findBookById(Long bookId);

}
