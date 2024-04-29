package com.libraryManagement.libraryManagement.exceptions;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

	private String id;

	private LocalDateTime timestamp;

	private String code;

	private String messageKey;

}
