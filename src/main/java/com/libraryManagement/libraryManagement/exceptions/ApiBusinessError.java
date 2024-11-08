package com.libraryManagement.libraryManagement.exceptions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiBusinessError {

	private String id;

	private LocalDateTime timestamp;

	private String code;

	private String messageKey;

	private List<Map<String, String>> details;

}
