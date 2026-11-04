package com.ticketflow.bookingservice.client;

import com.ticketflow.bookingservice.dto.EventResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "event-service")
public interface EventClient {

    @GetMapping("/api/events/{id}")
    EventResponse getById(@PathVariable("id") Long id);
}
