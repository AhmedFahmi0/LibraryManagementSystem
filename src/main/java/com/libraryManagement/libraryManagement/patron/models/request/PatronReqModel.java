package com.libraryManagement.libraryManagement.patron.models.request;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PatronReqModel implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -1244838975065246543L;
	
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name cannot exceed 255 characters")
    private String name;

    @Size(max = 20, message = "Mobile number cannot exceed 20 characters")
    private String mobile;

    @Email(message = "Invalid email format")
    private String email;

}
