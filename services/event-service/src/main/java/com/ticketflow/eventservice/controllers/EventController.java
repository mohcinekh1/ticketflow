package com.ticketflow.eventservice.controllers;

import com.ticketflow.eventservice.dto.EventResponse;
import com.ticketflow.eventservice.service.EventCatalogService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventCatalogService service;

    public EventController(EventCatalogService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    EventResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping
    List<EventResponse> list() {
        return service.list();
    }
}
