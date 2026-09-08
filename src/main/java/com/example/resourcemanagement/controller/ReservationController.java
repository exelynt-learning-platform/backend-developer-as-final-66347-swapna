package com.example.resourcemanagement.controller;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.resourcemanagement.dto.request.ReservationRequest;
import com.example.resourcemanagement.entity.Reservation;
import com.example.resourcemanagement.enums.ReservationStatus;
import com.example.resourcemanagement.service.ReservationService;

import jakarta.validation.Valid;




@RestController
@RequestMapping("/reservation")
public class ReservationController {
	private final ReservationService reservationService;
	
	public ReservationController(ReservationService reservationService) {
		this.reservationService=reservationService;
	}
	
	@PostMapping
	public ResponseEntity<Reservation> createReservation(
			@Valid @RequestBody ReservationRequest reservationRequest) {
		//TODO: process POST request
		Reservation reservation=reservationService.createReservation(reservationRequest);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
	}
	
	@GetMapping
	public ResponseEntity<Page<Reservation>> getReservations(

	        @RequestParam(required = false) ReservationStatus status,
	        @RequestParam(required = false) BigDecimal minPrice,
	        @RequestParam(required = false) BigDecimal maxPrice,
	        @PageableDefault(
	                size = 10,
	                sort = "startTime",
	                direction = Sort.Direction.ASC
	        )
	        Pageable pageable) {

	    Page<Reservation> reservations =
	            reservationService.getReservations(
	                    status,
	                    minPrice,
	                    maxPrice,
	                    pageable
	            );

	    return ResponseEntity.ok(reservations);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Reservation> updateReservation(@PathVariable Long id, 
			@Valid @RequestBody ReservationRequest request) {
		//TODO: process PUT request
		Reservation reservation = reservationService.updateReservation(id ,request);
		
		return ResponseEntity.ok(reservation);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteReservation(@PathVariable Long id){
		reservationService.deleteReservation(id);
		return ResponseEntity.noContent().build();
	}
	
	@PatchMapping("/{id}/status/{status}")
	public ResponseEntity<Reservation> updateReservationStatus(@PathVariable Long id,
			@PathVariable ReservationStatus status) {
		Reservation reservation = reservationService.updateReservationStatus(id,status);
		return ResponseEntity.ok(reservation);
	}
	
	
}
