package com.example.resourcemanagement.exception;

import org.springframework.validation.BindException;

public class ReservationNotFoundException extends RuntimeException {

	public ReservationNotFoundException(String message) {
		super(message);
	}

}
