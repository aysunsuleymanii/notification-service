#  Notification Service (Event-Driven System)

A **production-style event-driven backend system** built with **Spring Boot, Apache Kafka, PostgreSQL, and Docker**.

This project demonstrates how to design a scalable system where requests are processed asynchronously using a message broker.

---

# Overview

This system simulates a real-world notification flow:

<img width="854" height="122" alt="Untitled Diagram" src="https://github.com/user-attachments/assets/4c5938b0-2392-462d-aaa1-965a27a74734" />


Instead of processing requests synchronously, events are sent to Kafka and handled asynchronously.

---

#  Tech Stack

* Java 21
* Spring Boot
* Apache Kafka
* PostgreSQL
* Docker & Docker Compose
* Lombok
* Spring Data JPA

---

# Architecture

## Event Flow

1. Client sends request to API
2. API publishes event to Kafka
3. Kafka stores the event
4. Consumer reads the event
5. Consumer processes and saves it to database

---

## Components

### Producer (API)
* Receives HTTP requests
* Sends events to Kafka
* Does NOT process business logic
### Kafka (Message Broker)
* Stores events
* Delivers them to consumers
### Consumer
* Reads events from Kafka
* Processes them
* Saves data to PostgreSQL
### Database (PostgreSQL)
* Stores final notification data

---

# Getting Started

## 1. Clone the project

```bash
git clone https://github.com/aysunsuleymanii/notification-service
cd notification-service
```

---

## 2. Build the application

```bash
./mvnw clean package -DskipTests
```

---

## 3. Run the system

```bash
docker compose up --build
```

---

## 4. Verify containers

```bash
docker ps
```

You should see:

* app
* kafka
* zookeeper
* postgres

---

# API Testing

## Create Notification

```bash
curl -X POST http://localhost:8080/notifications \
-H "Content-Type: application/json" \
-d '{"userId":"123","message":"Hello Kafka"}'
```

---

## Expected Behavior

* Request is accepted
* Event is sent to Kafka
* Consumer processes event
* Data is saved in PostgreSQL

---

# Check Database

```bash
docker exec -it notification-service-postgres-1 psql -U postgres -d notifications
```

```sql
SELECT * FROM notification;
```

---

# Example Data

```json
{
  "id": "uuid",
  "userId": "123",
  "message": "Hello Kafka",
  "status": "PENDING",
  "createdAt": "timestamp"
}
```

---

# Docker Setup

## Services

* **app** → Spring Boot application
* **kafka** → message broker
* **zookeeper** → Kafka coordinator
* **postgres** → database

---

## How it works

* Containers communicate via Docker network
* App connects using:

  * `kafka:9092`
  * `postgres:5432`

---

# Key Concepts

## Event-Driven Architecture

* API does NOT process requests directly
* Events are sent to Kafka
* Consumers handle processing

---

## Decoupling

* Producer and consumer are independent
* System is scalable and flexible

---

## Asynchronous Processing

* Faster API response
* Better performance under load


