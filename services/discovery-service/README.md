# discovery-service

> Eureka service registry for TicketFlow microservices.

## Concept illustrated
This service is the registry where microservices announce themselves. Other
services can then discover them by name instead of using fixed host and port
values.

## Overview
- **Port:** 8761
- **Technology:** Netflix Eureka Server
- **Dashboard:** `http://localhost:8761`

## Run locally
    ./mvnw -pl services/discovery-service spring-boot:run

## Run with Docker
    docker compose -f infra/docker-compose.yml up --build discovery-service

## Eureka clients
After this service starts, the other services can register with:

    http://localhost:8761/eureka/
