package com.libraryManagement.libraryManagement.exceptions;

import java.util.List;
import java.util.Map;

public class BusinessLogicViolationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6789100015046853635L;
	
	private List<Map<String, String>> details;

	public BusinessLogicViolationException(String message) {
		super(message);
	}

	public BusinessLogicViolationException(String message, List<Map<String, String>> details) {
		super(message);
		this.setDetails(details);
	}

	public List<Map<String, String>> getDetails() {
		return details;
	}

	public void setDetails(List<Map<String, String>> details) {
		this.details = details;
	}

}
