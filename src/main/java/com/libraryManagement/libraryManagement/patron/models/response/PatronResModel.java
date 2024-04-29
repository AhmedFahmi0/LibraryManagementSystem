package com.libraryManagement.libraryManagement.patron.models.response;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatronResModel implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1348022208500758844L;
    private Long id;
    private String name;
    private String mobile;
    private String email;
    private Date creationDate;

}
