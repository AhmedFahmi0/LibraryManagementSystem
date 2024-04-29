package com.libraryManagement.libraryManagement.exceptions;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.validation.ConstraintViolationException;


@ControllerAdvice
public class AppExceptionsHandler {

	private static final Logger logger = LoggerFactory.getLogger(AppExceptionsHandler.class);

	// Delete id not found
	@ExceptionHandler(IncorrectResultSizeDataAccessException.class)
	public ResponseEntity<Object> incorrectResultSizeDataAccessException(IncorrectResultSizeDataAccessException ex) {
		ApiError apiError = new ApiError(UUID.randomUUID().toString(), LocalDateTime.now(),
				ApiErrorCodeEnum.INTERNAL_SERVER_ERROR.name(),
				ApiErrorMessageKeyEnum.INCORRECT_RESULT_SIZE_DATA_ACCESS.name());
		// System.out.println("PUT ERRORRR!!!!!!!!!!!");
		logger.info("xxxxxxxxx   Exception : : : " + ex.getMessage());
		return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// Update id not found
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<Object> handleUpdatedElementNotFoundException(NoSuchElementException ex) {
		ApiError apiError = new ApiError(UUID.randomUUID().toString(), LocalDateTime.now(),
				ApiErrorCodeEnum.INTERNAL_SERVER_ERROR.name(), ApiErrorMessageKeyEnum.REFRENCE_ID_NOT_FOUND.name());
		logger.info("xxxxxxxxx   Exception : : : " + ex.getMessage());
		return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// Rest Validation Handler
	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<Object> handleJavaxValidationException(ConstraintViolationException ex) {

		List<Error> errors = new ArrayList<Error>();

		List<String> errorList = Arrays.asList(ex.getMessage().split("\\s*,\\s*"));

		for (String error : errorList) {
			String capitalizedMessage = capitalizeMessage(getValidationMessage(error));
			errors.add(getValidationErrorMessage(capitalizedMessage, getValidationField(error)));
		}

		ApiBadRequestError apiBadRequestError = new ApiBadRequestError(UUID.randomUUID().toString(),
				LocalDateTime.now(), ApiErrorCodeEnum.BAD_REQUEST.name(), errors);
		logger.info("xxxxxxxxx   Exception : : : " + ex.getMessage());
		return new ResponseEntity<>(apiBadRequestError, new HttpHeaders(), HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler({ NullPointerException.class })
	public ResponseEntity<Object> exceptionHandler(NullPointerException ex) {
		ex.printStackTrace();
		ApiError apiError = new ApiError(UUID.randomUUID().toString(), LocalDateTime.now(),
				ApiErrorCodeEnum.INTERNAL_SERVER_ERROR.name(), ApiErrorMessageKeyEnum.NULL_POINTER_EXCEPTION.name());
		logger.info("xxxxxxxxx   Exception : : : " + ex.getMessage());
		return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler({ RuntimeException.class })
	public ResponseEntity<Object> runTimeExceptionHandler(RuntimeException ex) {
		ex.printStackTrace();
		ApiError apiError = new ApiError(UUID.randomUUID().toString(), LocalDateTime.now(),
				ApiErrorCodeEnum.INTERNAL_SERVER_ERROR.name(), ApiErrorMessageKeyEnum.RUN_TIME_EXCEPTION.name());
		logger.info("xxxxxxxxx   Exception : : : " + ex.getMessage());
		return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);

	}

	// JDBC Connection Error
	@ExceptionHandler({ CannotCreateTransactionException.class })
	public ResponseEntity<Object> createTransactionExceptionHandler(CannotCreateTransactionException ex) {
		ApiError apiError = new ApiError(UUID.randomUUID().toString(), LocalDateTime.now(),
				ApiErrorCodeEnum.INTERNAL_SERVER_ERROR.name(), ApiErrorMessageKeyEnum.JDBC_CONNECTON_ERROR.name());
		logger.info("xxxxxxxxx   Exception : : : " + ex.getMessage());
		return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);

	}
	
	@ExceptionHandler({ DataIntegrityViolationException.class })
	public ResponseEntity<Object> handlePersistenceException(DataIntegrityViolationException ex) {
	    ApiError apiError = null;
	    Throwable cause = ex.getRootCause();

	    String errorMessage;
	    if (cause instanceof SQLIntegrityConstraintViolationException || cause instanceof SQLException) {
	        errorMessage = cause.getMessage();
	    } else if (cause instanceof ConstraintViolationException) {
	        errorMessage = ApiErrorMessageKeyEnum.DATABASE_CONSTRAINT_VIOLATION.name();
	    } else {
	        errorMessage = ApiErrorMessageKeyEnum.INTERNAL_SERVER_ERROR.name();
	    }

	    apiError = new ApiError(UUID.randomUUID().toString(), LocalDateTime.now(),
	            ApiErrorCodeEnum.UNPROCESSABLE_ENTITY.name(), errorMessage);

	    logger.info("xxxxxxxxx   Exception : : : " + ex.getMessage());

	    return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
	}


	@ExceptionHandler({ MethodArgumentNotValidException.class })
	public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
		List<Error> errors = new ArrayList<Error>();

		for (ObjectError error : ex.getBindingResult().getAllErrors()) {
			String capitalizedMessage = capitalizeMessage(error.getDefaultMessage());
			errors.add(getValidationErrorMessage(capitalizedMessage, ((FieldError) error).getField()));
		}

		ApiBadRequestError apiBadRequestError = new ApiBadRequestError(UUID.randomUUID().toString(),
				LocalDateTime.now(), ApiErrorCodeEnum.BAD_REQUEST.name(), errors);
		logger.info("xxxxxxxxx   Exception : : : " + ex.getMessage());
		return new ResponseEntity<>(apiBadRequestError, new HttpHeaders(), HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler({ BusinessLogicViolationException.class })
	public ResponseEntity<Object> handleBussinessLogicException(BusinessLogicViolationException ex) {
		ApiBusinessError apiBusinessError = new ApiBusinessError(UUID.randomUUID().toString(), LocalDateTime.now(),
				ApiErrorCodeEnum.BUSINESS_CONSTRAINT_VIOLATION.name(), ex.getMessage(), ex.getDetails());
		logger.info("xxxxxxxxx   Exception : : : " + ex.getMessage());
		return new ResponseEntity<>(apiBusinessError, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler({ BusinessLogicViolationWarning.class })
	public ResponseEntity<Object> handleBussinessLogicWarning(BusinessLogicViolationWarning ex) {
		ApiBusinessError apiBusinessError = new ApiBusinessError(UUID.randomUUID().toString(), LocalDateTime.now(),
				ApiErrorCodeEnum.BUSINESS_CONSTRAINT_WARNING.name(), ex.getMessage(), ex.getDetails());
		logger.warn("xxxxxxxxx   Warning : : : " + ex.getMessage());
		return new ResponseEntity<>(apiBusinessError, new HttpHeaders(), CustomHttpStatus.WARNING_CODE.value());
	}

	// Data Type Don't Match
	@ExceptionHandler({ MethodArgumentTypeMismatchException.class })
	public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
		List<Error> errors = new ArrayList<>();
		errors.add(new Error(getMissMatchedField(ex.getMessage()),
				ApiErrorMessageKeyEnum.PATH_PARAMETER_TYPE_MISS_MATCH.name(), ""));
		ApiBadRequestError apiBadRequestError = new ApiBadRequestError(UUID.randomUUID().toString(),
				LocalDateTime.now(), ApiErrorCodeEnum.BAD_REQUEST.name(), errors);
		logger.info("xxxxxxxxx   Exception : : : " + ex.getMessage());
		return new ResponseEntity<>(apiBadRequestError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	// Request Method Incorrect
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<Object> handleUnSupportedMethodTypeException(HttpRequestMethodNotSupportedException ex) {
		ApiError apiError = new ApiError(UUID.randomUUID().toString(), LocalDateTime.now(),
				ApiErrorCodeEnum.METHOD_NOT_ALLOWED.name(), ApiErrorMessageKeyEnum.METHOD_NOT_ALLOWED.name());
		logger.info("xxxxxxxxx   Exception : : : " + ex.getMessage());
		return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED);
	}

	@ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
	public ResponseEntity<Object> handleMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException ex) {
		ApiError apiError = new ApiError(UUID.randomUUID().toString(), LocalDateTime.now(),
				ApiErrorCodeEnum.MEDIA_TYPE_NOT_ACCEPTED.name(), ApiErrorMessageKeyEnum.MEDIA_TYPE_NOT_ACCEPTED.name());
		logger.info("xxxxxxxxx   Exception : : : " + ex.getMessage());
		return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE);
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<Object> handleMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
		ApiError apiError = new ApiError(UUID.randomUUID().toString(), LocalDateTime.now(),
				ApiErrorCodeEnum.MEDIA_TYPE_NOT_SUPPORTED.name(),
				ApiErrorMessageKeyEnum.MEDIA_TYPE_NOT_SUPPORTED.name());
		logger.info("xxxxxxxxx   Exception : : : " + ex.getMessage());
		return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
	}

	@ExceptionHandler({ HttpMessageNotReadableException.class })
	public ResponseEntity<Object> handleDateFormatExceptions(HttpMessageNotReadableException ex) {

		List<Error> errors = new ArrayList<Error>();
		errors.add(getJacksonValidationErrorMessage(ex.getMessage()));

		ApiBadRequestError apiBadRequestError = new ApiBadRequestError(UUID.randomUUID().toString(),
				LocalDateTime.now(), ApiErrorCodeEnum.BAD_REQUEST.name(), errors);
		logger.info("xxxxxxxxx   Exception : : : " + ex.getMessage());
		return new ResponseEntity<>(apiBadRequestError, new HttpHeaders(), HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
		ApiError apiError = new ApiError(UUID.randomUUID().toString(), LocalDateTime.now(),
				ApiErrorCodeEnum.ACCESS_IS_DENIED.name(), ApiErrorMessageKeyEnum.ACCESS_IS_DENIED.name());
		logger.info("xxxxxxxxx   Exception : : : " + ex.getMessage());
		return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> generalError(Exception ex) {
		ApiError apiError = new ApiError(UUID.randomUUID().toString(), LocalDateTime.now(),
				ApiErrorCodeEnum.INTERNAL_SERVER_ERROR.name(), ApiErrorMessageKeyEnum.GENERAL_ERROR.name());
		logger.info("xxxxxxxxx   Exception : : : " + ex.getMessage());
		return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private ApiError SQLIntegrityConstraintViolation(String exceptionMessage) {
		String message = null;
		String code = null;
		if (exceptionMessage.contains("ORA-00001")) {// UK
			message = getConstraintKeyFromExceptionMessage("U_", exceptionMessage);
			code = ApiErrorCodeEnum.UNIQUE_KEY_CONSTRAINT_VIOLATION.name();
		} else if (exceptionMessage.contains("ORA-01400")) {
			message = exceptionMessage.substring(exceptionMessage.indexOf("ORA-01400:") + ("ORA-01400:").length());
			code = ApiErrorCodeEnum.INTERNAL_SERVER_ERROR.name();
		} else if (exceptionMessage.contains("ORA-01407")) {// Not Null
			message = exceptionMessage.substring(exceptionMessage.indexOf("ORA-01407:") + ("ORA-01407:").length());
			code = ApiErrorCodeEnum.INTERNAL_SERVER_ERROR.name();
		} else if (exceptionMessage.contains("ORA-02291")) {// Create And FK Not Found
			message = getConstraintKeyFromExceptionMessage("FK_", exceptionMessage);
			code = ApiErrorCodeEnum.FOREIGN_KEY_CONSTRAINT_VIOLATION.name();
		} else if (exceptionMessage.contains("ORA-02292")) {// Delete And Child Record Found
			message = getConstraintKeyFromExceptionMessage("FK_", exceptionMessage);
			code = ApiErrorCodeEnum.FOREIGN_KEY_CONSTRAINT_VIOLATION.name();
		}
		return new ApiError(UUID.randomUUID().toString(), LocalDateTime.now(), code, message);
	}

	private String getConstraintKeyFromExceptionMessage(String prefix, String exceptionMessage) {
		Matcher matcher = Pattern.compile(prefix + "(\\w+)").matcher(exceptionMessage);
		return matcher.find() ? matcher.group() : "";
	}

	private String capitalizeMessage(String message) {
		return message.toUpperCase().replaceAll(" ", "_");
	}

	private Error getValidationErrorMessage(String capitalizedMessage, String field) {

		String params = "";

		if (capitalizedMessage.contains("OR_EQUAL_TO_")) { // handle @Min, @Max
			params = capitalizedMessage
					.substring(capitalizedMessage.indexOf("OR_EQUAL_TO_") + ("OR_EQUAL_TO_").length());
			capitalizedMessage = capitalizedMessage.substring(0, capitalizedMessage.indexOf(params)).concat("VALUE");
		} else if (capitalizedMessage.contains("SIZE_MUST_BE_BETWEEN_")) { // handle @Size
			params = capitalizedMessage.substring(
					capitalizedMessage.indexOf("SIZE_MUST_BE_BETWEEN_") + ("SIZE_MUST_BE_BETWEEN_").length());
			capitalizedMessage = capitalizedMessage.substring(0, capitalizedMessage.indexOf(params)).concat("VALUES");
			params = params.replace("AND", ",").replace("_", "");
		} else if (capitalizedMessage.contains("MUST_MATCH")) { // handle @Pattern
			params = capitalizedMessage.substring(capitalizedMessage.indexOf("MUST_MATCH_") + ("MUST_MATCH_").length());
			capitalizedMessage = capitalizedMessage.substring(0, capitalizedMessage.indexOf(params) - 1);
		}

		return new Error(field, capitalizedMessage, params);

	}

	private String getValidationField(String error) {
		Pattern pattern = Pattern.compile("\\.(.*):\\s");
		Matcher matcher = pattern.matcher(error);
		if (matcher.find())
			return matcher.group(1);
		return "";
	}

	private String getValidationMessage(String error) {
		Pattern pattern = Pattern.compile(":\\s(.*)");
		Matcher matcher = pattern.matcher(error);
		if (matcher.find())
			return matcher.group(1);
		return "";
	}

	private String getMissMatchedField(String errorMessage) {
		Pattern pattern = Pattern.compile(":\\s\\\"(.*)\\\"");
		Matcher matcher = pattern.matcher(errorMessage);
		if (matcher.find())
			return matcher.group(1);
		return "";
	}

	private Error getJacksonValidationErrorMessage(String message) {
		String field = "";
		Pattern pattern = Pattern.compile("\\..*\\[\"(.*)\"\\]");
		Matcher matcher = pattern.matcher(message);
		if (matcher.find())
			field = matcher.group(1);
		return new Error(field, ApiErrorMessageKeyEnum.MODEL_PARAMETER_TYPE_MISS_MATCH.name(), "");
	}

}
