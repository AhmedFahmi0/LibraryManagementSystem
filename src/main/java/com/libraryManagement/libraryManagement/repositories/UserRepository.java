package com.libraryManagement.libraryManagement.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.libraryManagement.libraryManagement.entities.User;

public interface UserRepository extends CrudRepository<User, Long>{
	
	Optional<User> findByUsername(String username);

}
