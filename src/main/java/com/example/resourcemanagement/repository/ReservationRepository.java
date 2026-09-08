package com.example.resourcemanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.resourcemanagement.entity.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> , JpaSpecificationExecutor<Reservation>{

	List<Reservation> findByUserId(Long id);

}
