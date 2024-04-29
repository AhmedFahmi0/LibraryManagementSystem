package com.libraryManagement.libraryManagement.exceptions;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiBadRequestError {

	private String id;

	private LocalDateTime timestamp;

	private String code;

	private List<Error> errors;

}
