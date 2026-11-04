package com.ticketflow.bookingservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ticketflow.bookingservice.client.EventClient;
import com.ticketflow.bookingservice.dto.EventResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    EventClient eventClient;

    @InjectMocks
    BookingService bookingService;

    @Test
    void getsEventThroughFeignClient() {
        EventResponse event = new EventResponse(1L, "TicketFlow Live", "Casablanca", 150);
        when(eventClient.getById(1L)).thenReturn(event);

        EventResponse result = bookingService.validateAndGetEvent(1L);

        assertThat(result).isEqualTo(event);
        verify(eventClient).getById(1L);
    }
}
