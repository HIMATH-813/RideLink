# Ride Service

The Ride Service manages ride requests, ride status updates, and driver
assignment for the RideLink platform. It is a Java Spring Boot application
that stores ride data in MongoDB.

## Requirements

- Java 21
- MongoDB (local or MongoDB Atlas)
- The Account Service, for identifying the passenger when a ride is created
- The Driver & Vehicle Service, for assigning an available driver

The Maven wrapper (`./mvnw`) is included, so a separate Maven installation is
not required.

## Configuration

Configure these values before starting the service:

| Setting | Required | Default | Description |
| --- | --- | --- | --- |
| `MONGODB_URI` | Yes | None | MongoDB connection URI used by Spring Data MongoDB |
| `SERVER_PORT` | No | `8083` | Port used by the Ride Service |
| `ACCOUNT_SERVICE_BASE_URL` | No | `http://localhost:8081` | Base URL of the Account Service |

For example, in a shell:

```bash
export MONGODB_URI='mongodb://localhost:27017/ridelink'
export SERVER_PORT=8083
export ACCOUNT_SERVICE_BASE_URL='http://localhost:8081'
```

Use your actual MongoDB URI and service URLs in each environment. Do not commit
credentials or secret connection strings to the repository.

## Start the services

1. Make sure MongoDB is available.
2. Start the Account Service (default port `8081`).
3. Start the Driver & Vehicle Service.
4. Start the Ride Service:

   ```bash
   cd ride-service
   ./mvnw spring-boot:run
   ```

To build and run the tests:

```bash
./mvnw clean verify
```

## API documentation

When the Ride Service is running, open Swagger UI at
[`http://localhost:8083/swagger-ui.html`](http://localhost:8083/swagger-ui.html).
The OpenAPI document is available at
[`http://localhost:8083/v3/api-docs`](http://localhost:8083/v3/api-docs).

## Ride endpoints

All routes below are relative to `http://localhost:8083`.

| Method | Path | Description |
| --- | --- | --- |
| `POST` | `/api/rides` | Create a ride request. The passenger ID is retrieved from the Account Service, and the new ride starts in the `REQUESTED` state. |
| `POST` | `/api/rides/{rideId}/assign` | Assign an available driver to a ride. |
| `GET` | `/api/rides/{rideId}` | Retrieve a ride by its ID. |
| `PATCH` | `/api/rides/{rideId}/status` | Update a ride's status. |

The status lifecycle includes `REQUESTED`, `ASSIGNED`, `ACCEPTED`,
`IN_PROGRESS`, `COMPLETED`, and `CANCELLED`.

## Passenger identity and authentication

When creating a ride, send the caller's JWT in the `Authorization` header:

```http
Authorization: Bearer <JWT>
```

The Ride Service forwards that header to the Account Service's
`GET /api/v1/accounts/me` endpoint. The account response contains the
authenticated user's ID in the `id` field; the Ride Service uses that value as
the passenger ID.

**Authentication note:** the current Ride Service security configuration
permits requests to all routes. Ride creation still needs a valid token for
the Account Service identity lookup to succeed. Swagger security annotations
document authentication but do not enforce it.
