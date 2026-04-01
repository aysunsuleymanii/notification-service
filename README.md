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
* Distributes them to multiple consumers
* Enables asynchronous processing
### Consumers
#### Notification Consumer  
Saves notifications to PostgreSQL
Handles:
* Deduplication (Redis)
* Rate limiting (Redis)
* Retry logic
* Dead Letter Queue (DLQ)

#### EmailConsumer
* Simulates sending email notifications

#### PushConsumer
* Simulates push notifications

#### InAppConsumer
* Simulates in-app notifications

### Database (PostgreSQL)
* Stores final notification data

### Redis
Deduplication (prevent duplicate events)
Rate limiting (prevent spam)

---


## Key Features

### Deduplication (Idempotency)
Each event has a unique `eventId`.
Stored in Redis: ```event:{eventId}```
Prevents duplicate processing, if the event is already processed:
- Duplicate event is ignored

### Rate Limiting
Limits the number of requests per user.
Redis key: ```rate:{userId}```
Behavior:
- First requests → accepted
- After threshold → blocked
Example: ```Rate limit exceeded for user: 123```

### Retry Mechanism
If processing fails:
``` notifications -> notifications-retry -> notifications-dlq```
- Retries up to 3 times
- Uses Kafka retry topic


### Dead Letter Queue (DLQ)
After maximum retries:
Message is sent to: ```notifications-dlq```
Used for:
- Debugging failures
- Manual recovery

### Fanout (Publish-Subscribe)
One event is delivered to multiple independent consumers:
- Email
- Push
- In-App
Each uses a different consumer group:
- `email-group`
- `push-group`
- `inapp-group`
Ensures all consumers receive the same event

### User Preferences API
Users can dynamically control how they receive notifications.

### Endpoints
```
GET /preferences/{userId}
POST /preferences
PUT /preferences/{userId}
```

#### Create Preferences

```bash
curl -X POST http://localhost:8080/preferences \
-H "Content-Type: application/json" \
-d '{
  "userId": "100",
  "emailEnabled": true,
  "pushEnabled": true,
  "inAppEnabled": false
}'
```

#### Get Preferenes
```
curl http://localhost:8080/preferences/100
```

#### Update Preferences
```
curl -X PUT http://localhost:8080/preferences/100 \
-H "Content-Type: application/json" \
-d '{
  "emailEnabled": false,
  "pushEnabled": true,
  "inAppEnabled": true
}'
```

---

# Getting Started

## 1. Clone the project

```bash
git clone https://github.com/aysunsuleymanii/notification-service
cd notification-service
```

---

## 2. Run all the services

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
* redis

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

# Test Rate Limiting
A **rate limiter** is implemented to prevent users from spamming notifications.
When the limit is exceeded:
- Requests are **blocked**
- Logs will show: `Rate limit exceeded for user: xxxx`

## Send Mutliple Notifications  
Use this script to simulate spam requests:
```
for i in {1..10}
do
  curl -X POST http://localhost:8080/notifications \
  -H "Content-Type: application/json" \
  -d "{\"eventId\":\"$i\",\"userId\":\"12121212\",\"message\":\"spam test\"}"
done
```
## Expected Behavior
First few requests -> accepted  
Remaining requests -> blocked by rate limiter  

# Check Database
```bash
docker exec -it notification-service-postgres-1 psql -U postgres -d notifications
```

```sql
SELECT * FROM notification;
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
