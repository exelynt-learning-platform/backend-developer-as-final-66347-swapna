package com.example.resourcemanagement.exception;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Map<String, String>> handleTypeMismatch(
	        MethodArgumentTypeMismatchException exception) {

	    return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(Map.of(
	                    "message",
	                    "Invalid value for parameter: "
	                            + exception.getName()
	            ));
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Map<String,String>> handleAccessDenied(AccessDeniedException exception){
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message","You do not have permission to access this resource"));
	}
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<Map<String, String>> handleAuthentication(
	        AuthenticationException exception) {

	    return ResponseEntity
	            .status(HttpStatus.UNAUTHORIZED)
	            .body(Map.of(
	                    "message",
	                    "Authentication failed"
	            ));
	}
}
