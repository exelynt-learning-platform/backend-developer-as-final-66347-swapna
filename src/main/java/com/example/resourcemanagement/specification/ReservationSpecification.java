package com.example.resourcemanagement.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.example.resourcemanagement.entity.Reservation;
import com.example.resourcemanagement.entity.User;
import com.example.resourcemanagement.enums.ReservationStatus;

public class ReservationSpecification {

    public static Specification<Reservation> hasStatus(
            ReservationStatus status) {

        return (root, query, criteriaBuilder) ->
                status == null
                        ? null
                        : criteriaBuilder.equal(
                                root.get("status"),
                                status
                        );
    }

    public static Specification<Reservation> priceGreaterThanOrEqual(
            BigDecimal minPrice) {

        return (root, query, criteriaBuilder) ->
                minPrice == null
                        ? null
                        : criteriaBuilder.greaterThanOrEqualTo(
                                root.get("price"),
                                minPrice
                        );
    }

    public static Specification<Reservation> priceLessThanOrEqual(
            BigDecimal maxPrice) {

        return (root, query, criteriaBuilder) ->
                maxPrice == null
                        ? null
                        : criteriaBuilder.lessThanOrEqualTo(
                                root.get("price"),
                                maxPrice
                        );
    }

    public static Specification<Reservation> belongsToUser(
            User user) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("user"),
                        user
                );
    }
    public static Specification<Reservation> hasUserId(Long userId) {

        return (root, query, criteriaBuilder) ->
                userId == null
                        ? null
                        : criteriaBuilder.equal(
                                root.get("user").get("id"),
                                userId
                        );
    }
}
