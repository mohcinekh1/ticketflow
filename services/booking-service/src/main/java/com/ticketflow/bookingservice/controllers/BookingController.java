package com.ticketflow.bookingservice.controllers;

import com.ticketflow.bookingservice.dto.EventResponse;
import com.ticketflow.bookingservice.service.BookingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/test-event/{id}")
    EventResponse testEvent(@PathVariable Long id) {
        return bookingService.validateAndGetEvent(id);
    }
}
