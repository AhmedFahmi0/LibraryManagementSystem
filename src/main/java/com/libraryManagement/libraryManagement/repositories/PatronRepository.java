package com.libraryManagement.libraryManagement.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.libraryManagement.libraryManagement.entities.Patron;
import com.libraryManagement.libraryManagement.projections.BookProjection;
import com.libraryManagement.libraryManagement.projections.PatronProjection;

@Repository
public interface PatronRepository extends CrudRepository<Patron, Long> {

	Page<PatronProjection> findAll(Pageable pageable);
	
	PatronProjection findPatronById(Long patronId);
}
