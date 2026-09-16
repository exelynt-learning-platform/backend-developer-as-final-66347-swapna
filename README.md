# Resource Management System

A RESTful Resource Booking System built using Spring Boot, Spring Security, JWT, Spring Data JPA, and MySQL.

The application provides secure resource management and reservation functionality with role-based access control for ADMIN and USER roles.

---

## Features

### Authentication & Authorization

- User registration
- User login
- JWT-based authentication
- BCrypt password encryption
- Role-based authorization
- ADMIN and USER roles
- Stateless authentication using JWT

### Resource Management

ADMIN users can:

- Create resources
- View resources
- Update resources
- Delete resources

USER users can:

- View available resources

Each resource contains:

- Name
- Type
- Description
- Price

### Reservation Management

Users can:

- Create reservations
- View their own reservations

ADMIN users can:

- View all reservations
- Create reservations
- Update reservations
- Delete reservations
- Update reservation status

Supported reservation statuses:

- PENDING
- CONFIRMED
- CANCELLED

### Reservation Filtering

Reservations can be filtered by:

- Status
- Minimum price
- Maximum price

### Pagination & Sorting

Reservation APIs support:

- Page number
- Page size
- Sorting
- Multiple filtering parameters

Example:

```text
/reservation?page=0&size=10&sort=startTime,asc# backend-developer-as-final-66347-swapna
Final Project Assignment - This repository contains the complete final project code and documentation.
