package com.libraryManagement.libraryManagement.entities;

import java.io.Serializable;
import java.util.Date;

import com.libraryManagement.libraryManagement.auditing.entities.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "patrons")
@Data
public class Patron extends BaseEntity implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7340450282352863186L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "mobile")
    private String mobile;
	
    @Column(name = "email")
    private String email;
    
	@Column(name = "creation_date")
	private Date creationDate;
}
