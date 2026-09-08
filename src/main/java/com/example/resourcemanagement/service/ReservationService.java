package com.example.resourcemanagement.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.resourcemanagement.dto.request.ReservationRequest;
import com.example.resourcemanagement.entity.Reservation;
import com.example.resourcemanagement.entity.Resource;
import com.example.resourcemanagement.entity.User;
import com.example.resourcemanagement.enums.ReservationStatus;
import com.example.resourcemanagement.enums.Role;
import com.example.resourcemanagement.repository.ReservationRepository;
import com.example.resourcemanagement.repository.ResourceRepository;
import com.example.resourcemanagement.repository.UserRepository;
import com.example.resourcemanagement.specification.ReservationSpecification;

import jakarta.validation.Valid;

public class ReservationService {
	
	private final ReservationRepository reservationRepository;
	private final ResourceRepository resourceRepository;
	private final UserRepository userRepository;
	
	public ReservationService(ReservationRepository reservationRepository,
			ResourceRepository resourceRepository,
			UserRepository userRepository) {
		this.reservationRepository=reservationRepository;
		this.resourceRepository=resourceRepository;
		this.userRepository=userRepository;
	}

	public Reservation createReservation(ReservationRequest request) {
		// TODO Auto-generated method stub
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		String email=authentication.getName();
		User user=userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User not found"));
		
		Resource resource=resourceRepository.findById(request.getResourceId()).orElseThrow(()->new RuntimeException("Resource not found"));
		
		if(!request.getEndTime().isAfter(request.getStartTime())) {
			throw new RuntimeException("End time must be after the start time");
		}
		
		Reservation reservation = new Reservation();
//		reservation.setResource(reservationRequest.getResourceId());
		reservation.setStartTime(request.getStartTime());
		reservation.setEndTime(request.getEndTime());
		reservation.setResource(resource);
		reservation.setUser(user);
		reservation.setPrice(resource.getPrice());
		reservation.setStatus(ReservationStatus.PENDING);
		
		return reservationRepository.save(reservation);
	}

	public Page<Reservation> getReservations(
	        ReservationStatus status,
	        BigDecimal minPrice,
	        BigDecimal maxPrice,
	        Pageable pageable) {

	    Authentication authentication =
	            SecurityContextHolder.getContext().getAuthentication();

	    String email = authentication.getName();

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    Specification<Reservation> specification =
	            Specification.where(
	                    ReservationSpecification.hasStatus(status)
	            )
	            .and(
	                    ReservationSpecification.priceGreaterThanOrEqual(minPrice)
	            )
	            .and(
	                    ReservationSpecification.priceLessThanOrEqual(maxPrice)
	            );

	    if (user.getRole() == Role.ADMIN) {
	        return reservationRepository.findAll(
	                specification,
	                pageable
	        );
	    }

	    specification = specification.and(
	            ReservationSpecification.hasUserId(user.getId())
	    );

	    return reservationRepository.findAll(
	            specification,
	            pageable
	    );
	}

	public Reservation updateReservation(Long id,ReservationRequest request) {
		// TODO Auto-generated method stub
		Reservation reservation = reservationRepository.findById(id)
				.orElseThrow(()->new RuntimeException("Reservation not found"));
		
		Resource resource=resourceRepository.findById(request.getResourceId())
				.orElseThrow(()->new RuntimeException("resource not found"));
		
		if (!request.getEndTime().isAfter(request.getStartTime())) {
				throw new RuntimeException("End time must be after start time");
	    }
		
		reservation.setStartTime(request.getStartTime());
		reservation.setEndTime(request.getEndTime());
		reservation.setPrice(resource.getPrice());
		reservation.setResource(resource);
		
		return reservationRepository.save(reservation);
	}

	public void deleteReservation(Long id) {
		// TODO Auto-generated method stub
		Reservation reservation = reservationRepository.findById(id)
				.orElseThrow(()->new RuntimeException("Reservation not found"));
		
		reservationRepository.delete(reservation);
		
	}

	public Reservation updateReservationStatus(Long id, ReservationStatus status) {
		// TODO Auto-generated method stub
		Reservation reservation = reservationRepository.findById(id)
				.orElseThrow(()->new RuntimeException("Reservation not found"));
		reservation.setStatus(status);
		return reservationRepository.save(reservation);
	}

}
