package com.kh.evision.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private ResponseEntity<Map<String, String>> createResponseEntity(RuntimeException e, HttpStatus status) {
		
		Map<String, String> error = new HashMap();
		error.put("error-message", e.getMessage());
		return ResponseEntity.status(status).body(error);
		
	}
	
	@ExceptionHandler(FileUploadFailureException.class)
	public ResponseEntity<Map<String, String>> handleFileUploadFailure(FileUploadFailureException e) {
		return createResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(InvalidImgFormatException.class)
	public ResponseEntity<Map<String, String>> handleInvalidImgFormat(InvalidImgFormatException e) {
		return createResponseEntity(e, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}

}
