package com.example.resourcemanagement.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String , String>> handleResourceNotFound(ResourceNotFoundException exception){
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(Map.of("message",exception.getMessage()));
	}
	
	@ExceptionHandler(ReservationNotFoundException.class)
	public ResponseEntity<Map<String , String>> handleReservationNotFound(ReservationNotFoundException exception){
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(Map.of("message",exception.getMessage()));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String , String>> handlevalidationException(MethodArgumentNotValidException exception){
		
		Map<String, String> errors = new HashMap<>();

	    for (FieldError error :
	            exception.getBindingResult().getFieldErrors()) {

	        errors.put(
	                error.getField(),
	                error.getDefaultMessage()
	        );
	    }
	    
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(errors);
	}
}
