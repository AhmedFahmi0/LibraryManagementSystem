package com.libraryManagement.libraryManagement.entities;

import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tasks")
@Data
public class Task {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_name")
    private String taskName;

    @Column(name = "execute_date")
    private LocalDate executeDate;
    
	@Column(name = "creation_date")
	private Date creationDate;
    
    @PrePersist
    public void prePersist() {
        this.creationDate = new Date();
    }

}
