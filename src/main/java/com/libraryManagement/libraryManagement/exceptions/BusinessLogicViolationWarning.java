package com.libraryManagement.libraryManagement.exceptions;

import java.util.List;
import java.util.Map;

public class BusinessLogicViolationWarning extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4700418777933807264L;

	private List<Map<String, String>> details;

	public BusinessLogicViolationWarning(String message) {
		super(message);
	}

	public BusinessLogicViolationWarning(String message, List<Map<String, String>> details) {
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
