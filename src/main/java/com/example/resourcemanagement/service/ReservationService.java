package com.example.resourcemanagement.service;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.resourcemanagement.dto.request.ReservationRequest;
import com.example.resourcemanagement.dto.response.ReservationResponse;
import com.example.resourcemanagement.entity.Reservation;
import com.example.resourcemanagement.entity.Resource;
import com.example.resourcemanagement.entity.User;
import com.example.resourcemanagement.enums.ReservationStatus;
import com.example.resourcemanagement.enums.Role;
import com.example.resourcemanagement.exception.ReservationNotFoundException;
import com.example.resourcemanagement.exception.ResourceNotFoundException;
import com.example.resourcemanagement.repository.ReservationRepository;
import com.example.resourcemanagement.repository.ResourceRepository;
import com.example.resourcemanagement.repository.UserRepository;
import com.example.resourcemanagement.specification.ReservationSpecification;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ResourceRepository resourceRepository;
    private final UserRepository userRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            ResourceRepository resourceRepository,
            UserRepository userRepository) {

        this.reservationRepository = reservationRepository;
        this.resourceRepository = resourceRepository;
        this.userRepository = userRepository;
    }

    // Convert Reservation entity to ReservationResponse DTO
    private ReservationResponse toResponse(Reservation reservation) {

    	return new ReservationResponse(
                reservation.getId(),
                reservation.getResource().getId(),
                reservation.getResource().getName(),
                reservation.getUser().getEmail(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getPrice(),
                reservation.getStatus()
        );
    }

    // CREATE RESERVATION
    public ReservationResponse createReservation(
            ReservationRequest request) {

        // Get logged-in user's authentication
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        // Get email from JWT
        String email = authentication.getName();

        // Find user from database
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Find resource
        Resource resource = resourceRepository
                .findById(request.getResourceId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Resource not found"));

        // Validate time
        if (!request.getEndTime()
                .isAfter(request.getStartTime())) {

            throw new RuntimeException(
                    "End time must be after the start time");
        }

        // Create reservation
        Reservation reservation = new Reservation();

        reservation.setStartTime(request.getStartTime());
        reservation.setEndTime(request.getEndTime());
        reservation.setResource(resource);
        reservation.setUser(user);

        // Price comes from resource
        reservation.setPrice(resource.getPrice());

        // New reservation starts as PENDING
        reservation.setStatus(ReservationStatus.PENDING);

        // Save reservation
        Reservation savedReservation =
                reservationRepository.save(reservation);

        // Return DTO
        return toResponse(savedReservation);
    }

    // GET RESERVATIONS
    public Page<ReservationResponse> getReservations(
            ReservationStatus status,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable) {

        // Get logged-in user
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Build filters
        Specification<Reservation> specification =
                Specification.where(
                        ReservationSpecification.hasStatus(status)
                )
                .and(
                        ReservationSpecification
                                .priceGreaterThanOrEqual(minPrice)
                )
                .and(
                        ReservationSpecification
                                .priceLessThanOrEqual(maxPrice)
                );

        // ADMIN can see all reservations
        if (user.getRole() == Role.ADMIN) {

            return reservationRepository
                    .findAll(specification, pageable)
                    .map(this::toResponse);
        }

        // USER can see only their own reservations
        specification = specification.and(
                ReservationSpecification.hasUserId(user.getId())
        );

        return reservationRepository
                .findAll(specification, pageable)
                .map(this::toResponse);
    }

    // UPDATE RESERVATION
    public ReservationResponse updateReservation(
            Long id,
            ReservationRequest request) {

        // Find existing reservation
        Reservation reservation =
                reservationRepository.findById(id)
                        .orElseThrow(() ->
                                new ReservationNotFoundException(
                                        "Reservation not found"));

        // Find new resource
        Resource resource =
                resourceRepository.findById(
                        request.getResourceId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Resource not found"));

        // Validate time
        if (!request.getEndTime()
                .isAfter(request.getStartTime())) {

            throw new RuntimeException(
                    "End time must be after start time");
        }

        // Update reservation
        reservation.setStartTime(request.getStartTime());
        reservation.setEndTime(request.getEndTime());
        reservation.setResource(resource);

        // Get price from resource
        reservation.setPrice(resource.getPrice());

        // Save
        Reservation savedReservation =
                reservationRepository.save(reservation);

        // Return DTO
        return toResponse(savedReservation);
    }

    // DELETE RESERVATION
    public void deleteReservation(Long id) {

        Reservation reservation =
                reservationRepository.findById(id)
                        .orElseThrow(() ->
                                new ReservationNotFoundException(
                                        "Reservation not found"));

        reservationRepository.delete(reservation);
    }

    // UPDATE RESERVATION STATUS
    public ReservationResponse updateReservationStatus(
            Long id,
            ReservationStatus status) {

        // Find reservation
        Reservation reservation =
                reservationRepository.findById(id)
                        .orElseThrow(() ->
                                new ReservationNotFoundException(
                                        "Reservation not found"));

        // Update status
        reservation.setStatus(status);

        // Save
        Reservation savedReservation =
                reservationRepository.save(reservation);

        // Return DTO
        return toResponse(savedReservation);
    }
}