package com.ticketflow.bookingservice.service;

import com.ticketflow.bookingservice.client.EventClient;
import com.ticketflow.bookingservice.dto.EventResponse;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private final EventClient eventClient;

    public BookingService(EventClient eventClient) {
        this.eventClient = eventClient;
    }

    public EventResponse validateAndGetEvent(Long eventId) {
        return eventClient.getById(eventId);
    }
}
