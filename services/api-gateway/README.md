# api-gateway

> Single entry point for TicketFlow microservices.

## Concept illustrated
Spring Cloud Gateway routes client requests to the correct backend service.
This version uses Eureka service discovery and load-balanced routing.

## Overview
- **Port:** 8080
- **Depends on:** discovery-service, customer-service
- **Routes to:** `lb://customer-service`

| Client path | Destination |
|---|---|
| `/api/customers/**` | `customer-service` |

## Run locally
Start `discovery-service` first:

    ./mvnw -pl services/discovery-service spring-boot:run

Start `customer-service` first:

    ./mvnw -pl services/customer-service spring-boot:run

Then start the gateway:

    ./mvnw -pl services/api-gateway spring-boot:run

Test through the gateway:

    curl http://localhost:8080/api/customers

## Run with Docker
    docker compose -f infra/docker-compose.yml up --build api-gateway

## Configuration
The gateway discovers `customer-service` through Eureka:

    lb://customer-service

The local default Eureka URL is:

    EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://localhost:8761/eureka/

Docker Compose overrides it with:

    EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://discovery-service:8761/eureka/
