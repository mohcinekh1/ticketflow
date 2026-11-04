package com.ticketflow.eventservice.service;

import com.ticketflow.eventservice.dto.EventResponse;
import com.ticketflow.eventservice.exceptions.EventNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EventCatalogService {

    private static final List<EventResponse> EVENTS = List.of(
            new EventResponse(1L, "TicketFlow Live", "Casablanca", 150),
            new EventResponse(2L, "Java Community Day", "Rabat", 80),
            new EventResponse(3L, "Cloud Architecture Workshop", "Marrakech", 40));

    public EventResponse get(Long id) {
        return EVENTS.stream()
                .filter(event -> event.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new EventNotFoundException(id));
    }

    public List<EventResponse> list() {
        return EVENTS;
    }
}
