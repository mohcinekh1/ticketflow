# api-gateway

> Single entry point for TicketFlow microservices.

## Concept illustrated
Spring Cloud Gateway routes client requests to the correct backend service.
This version uses Eureka service discovery and load-balanced routing.

## Overview
- **Port:** 8080
- **Depends on:** config-server, discovery-service and the routed services
- **Routes to:** customer-service, event-service and booking-service

| Client path | Destination |
|---|---|
| `/api/customers/**` | `customer-service` |
| `/api/events/**` | `event-service` |
| `/api/bookings/**` | `booking-service` |

## Run locally
Start `config-server` and `discovery-service` first:

    ./mvnw -pl services/config-server spring-boot:run

    ./mvnw -pl services/discovery-service spring-boot:run

Then start the business services:

    ./mvnw -pl services/customer-service spring-boot:run
    ./mvnw -pl services/event-service spring-boot:run
    ./mvnw -pl services/booking-service spring-boot:run

Then start the gateway:

    ./mvnw -pl services/api-gateway spring-boot:run

Test through the gateway:

    curl http://localhost:8080/api/customers
    curl http://localhost:8080/api/events/1
    curl http://localhost:8080/api/bookings/test-event/1

## Run with Docker
    docker compose -f infra/docker-compose.yml up --build api-gateway

## Configuration
The routes are loaded from `config-repo/api-gateway.yml`. The gateway discovers
the backend services through Eureka, for example:

    lb://customer-service

The local default Eureka URL is:

    EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://localhost:8761/eureka/

Docker Compose overrides it with:

    EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://discovery-service:8761/eureka/

The Config Server URL is configured with `CONFIG_SERVER_URL`.
