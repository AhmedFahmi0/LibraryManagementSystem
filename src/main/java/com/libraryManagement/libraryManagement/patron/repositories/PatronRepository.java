package com.libraryManagement.libraryManagement.patron.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.libraryManagement.libraryManagement.patron.entities.Patron;

@Repository
public interface PatronRepository extends CrudRepository<Patron, Long> {

	Page<Patron> findAll(Pageable pageable);
}
