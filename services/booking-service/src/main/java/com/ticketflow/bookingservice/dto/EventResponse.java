package com.ticketflow.bookingservice.dto;

public record EventResponse(
        Long id,
        String title,
        String location,
        int availableSeats) {
}
