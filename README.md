# Test project for Tenpo company

## Description

Test project for the Tenpo company:

* REST API
* Redis caching
* PostgreSQL persistence
* Swagger documentation
* Dockerized deployment

## Technologies

* Java 21
* Spring Boot 3.2.3
* Redis
* PostgreSQL
* Docker
* Gradle
* JUnit & Mockito

## Getting Started

### Prerequisites

* Docker
* Docker Compose

### Build & Run with Docker

```bash
docker-compose up --build
```

### API Documentation

Once running, access Swagger UI at:

```
http://localhost:8080/swagger-ui/index.html
```

## Endpoints

### 1. Calculate Percentage

```
POST /api/calculate
{
  "num1": 10,
  "num2": 20
}
```

Returns sum and result with external percentage applied.

### 2. History

```
GET /api/history
```

Returns history of previous calculation calls.

## Tests

Run tests:

```bash
gradle test
```

## Author

Christian Giovani Cachaya Bolivar
