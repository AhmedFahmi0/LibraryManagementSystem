package com.libraryManagement.libraryManagement.User.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.libraryManagement.libraryManagement.User.entities.User;

public interface UserRepository extends CrudRepository<User, Long>{
	
	Optional<User> findByUsername(String username);

}
