package com.ticketflow.eventservice.dto;

public record EventResponse(
        Long id,
        String title,
        String location,
        int availableSeats) {
}
