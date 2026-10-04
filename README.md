# RideLink - Microservices-Based Ride-Sharing Platform

RideLink is a distributed, backend-only microservices application built for a ride-sharing platform using Java 21, Spring Boot 3.5.6, MongoDB, and GitHub Actions for CI/CD.

---

## 👥 Group Organization & Microservice Ownership

| Microservice | Primary Owner | Student ID | Responsibilities |
| :--- | :--- | :--- | :--- |
| **Account Service** | Member 1 | IT24103814 | Passenger/Driver registration, authentication, JWT token issuance, roles. |
| **Driver & Vehicle Service** | Member 2 | IT24103153 | Driver profiles, vehicle specs, availability toggle, coordinate tracking. |
| **Fare Service** | Member 3 | IT24102712 | Dynamic fare estimations, rule-based pricing, simulated payment records. |
| **Ride Management Service** | Member 4 | IT24102769 | Ride request creation, driver assignment, ride lifecycle management. |

---

## 🏗 System Architecture & Ports

Each service operates independently with its own MongoDB persistence boundary:

| Microservice | Base Port | Swagger UI Documentation |
| :--- | :--- | :--- |
| **Account Service** | `8081` | `http://localhost:8081/swagger-ui.html` |
| **Driver & Vehicle Service** | `8082` | `http://localhost:8082/swagger-ui.html` |
| **Fare Service** | `8083` | `http://localhost:8083/swagger-ui.html` |
| **Ride Management Service** | `8084` | `http://localhost:8084/swagger-ui.html` |

---

## 🛠 Tech Stack & Prerequisites

* **Java Version:** OpenJDK 21
* **Framework:** Spring Boot 3.5.6
* **Database:** MongoDB (Running locally on default port `27017` or Atlas URI)
* **CI/CD:** GitHub Actions (`.github/workflows/ci.yml`)
* **API Testing:** Postman Collection (`/Postman`)

---

## 🚀 Getting Started & Execution Order

### 1. Startup Order
To ensure proper interservice resolution during testing, start the services in the following order:
1. `account-service` (Port 8081)
2. `driver-service` (Port 8082)
3. `fare-service` (Port 8083)
4. `ride-service` (Port 8084)

### 2. Running Services Locally
Open separate terminal tabs for each service and run:

```bash
# Account Service
cd account-service
./mvnw spring-boot:run

# Driver Service
cd driver-service
./mvnw spring-boot:run

# Fare Service
cd fare-service
./mvnw spring-boot:run

# Ride Service
cd ride-service
./mvnw spring-boot:run
```

---

## 🧪 Testing Instructions

### Running Unit Tests
To run automated unit test suites for any microservice, execute the following in the respective service folder:

```bash
cd <service-folder>
./mvnw test
```

### Postman Integration Scenarios
A shared Postman Collection and Environment file are available in the `/Postman` directory. Import `RideLink.postman_collection.json` and `RideLink.postman_environment.json` into Postman to execute complete end-to-end workflows (both success and negative testing scenarios).

---

## 🔑 Sample Test Credentials & Data

Use these pre-configured test accounts for API verification and demonstration:

* **Passenger Account:**
  * **Email:** `passenger@ridelink.com`
  * **Password:** `Passenger@123`
  * **Role:** `PASSENGER`

* **Driver Account:**
  * **Email:** `driver@ridelink.com`
  * **Password:** `Driver@123`
  * **Role:** `DRIVER`

---

## 🔄 CI/CD Pipeline

Continuous Integration is managed via GitHub Actions (`.github/workflows/ci.yml`). On every `push` or `pull_request` targeting the `main` branch, the automated workflow:

1. Checks out the repository code.
2. Sets up JDK 21 environment.
3. Compiles and runs unit test suites across all 4 microservices.
4. Verifies system stability before allowing code integration.