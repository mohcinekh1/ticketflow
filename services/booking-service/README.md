# booking-service

Demonstrates synchronous service-to-service communication with OpenFeign.

`GET /api/bookings/test-event/{id}` calls `event-service` through Eureka and
returns its event response. Persistence and the complete booking lifecycle are
planned for the later Saga/Outbox steps.
