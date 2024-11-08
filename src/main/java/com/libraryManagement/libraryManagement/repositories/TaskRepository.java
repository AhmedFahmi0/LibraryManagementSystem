package com.libraryManagement.libraryManagement.repositories;

import org.springframework.data.repository.CrudRepository;

import com.libraryManagement.libraryManagement.entities.Task;

public interface TaskRepository extends CrudRepository<Task, Long> {

}
