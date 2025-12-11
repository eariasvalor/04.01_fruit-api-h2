# 🍎 Fruit API - REST API with Spring Boot and H2

![Java](https://img.shields.io/badge/Java-21-orange?style=flat&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?style=flat&logo=springboot)
![Maven](https://img.shields.io/badge/Maven-3.9-blue?style=flat&logo=apachemaven)
![H2](https://img.shields.io/badge/H2-Database-blue?style=flat)
![Docker](https://img.shields.io/badge/Docker-Ready-blue?style=flat&logo=docker)

REST API for managing a fruit shop inventory developed with **Spring Boot** and **H2 in-memory database**. Project created following **TDD (Test-Driven Development)** methodology with **MVC architecture**.

---

## 📋 Table of Contents

- [Description](#-description)
- [Technologies](#-technologies)
- [Architecture](#-architecture)
- [Endpoints](#-endpoints)
- [Installation](#-installation)
- [Execution](#-execution)
- [Tests](#-tests)
- [Docker](#-docker)
- [Project Structure](#-project-structure)
- [Validations](#-validations)
- [Error Handling](#-error-handling)

---

## 🎯 Description

This backend application allows **managing a fruit shop inventory**, recording for each entry:
- Product **name**
- **Weight** in kilograms

The project implements a **complete CRUD** (Create, Read, Update, Delete) following development best practices:

✅ **Outside-In TDD** with integration tests  
✅ **MVC Architecture** (Model-View-Controller)  
✅ **DTO separation** (Request/Response)  
✅ **Bean Validation** for data validation  
✅ **Global exception handling**  
✅ **H2 in-memory database**  
✅ **Dockerized** with multi-stage build  

---

## 🛠️ Technologies

### Core
- **Java 21** (LTS)
- **Spring Boot 3.x**
- **Maven** - Dependency management

### Spring Modules
- **Spring Web** - REST API
- **Spring Data JPA** - Persistence
- **Spring Boot Actuator** - Health checks
- **Spring Boot DevTools** - Development

### Database
- **H2 Database** - In-memory SQL database

### Testing
- **JUnit 5** - Testing framework
- **Spring Boot Test** - Integration tests
- **MockMvc** - Controller tests
- **Mockito** - Mocking

### Validation
- **Jakarta Validation** - Data validation

### Utilities
- **Lombok** - Boilerplate reduction

### DevOps
- **Docker** - Containerization
- **Docker Multi-stage Build** - Image optimization

---

## 🏗️ Architecture

The project follows the **MVC (Model-View-Controller)** pattern with the following layers:

```
┌─────────────────────────────────────────────────────────┐
│                    CLIENT (Postman, cURL)               │
└────────────────────┬────────────────────────────────────┘
                     │ HTTP Requests
                     ▼
┌─────────────────────────────────────────────────────────┐
│              CONTROLLER LAYER                           │
│  • FruitController                                      │
│  • Handles HTTP requests                                │
│  • Validates input data (@Valid)                        │
│  • Returns ResponseEntity with proper HTTP codes        │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│              SERVICE LAYER                              │
│  • FruitService                                         │
│  • Business logic                                       │
│  • Transactions (@Transactional)                        │
│  • Entity ↔ DTO mapping                                 │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│              REPOSITORY LAYER                           │
│  • FruitRepository (JpaRepository)                      │
│  • Data access                                          │
│  • Automatically implemented by Spring Data JPA         │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│              DATABASE LAYER                             │
│  • H2 Database (in-memory)                              │
│  • Table: fruits                                        │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│              EXCEPTION HANDLING                          │
│  • GlobalExceptionHandler (@RestControllerAdvice)       │
│  • Centralized error handling                           │
│  • Returns consistent ErrorResponse                     │
└─────────────────────────────────────────────────────────┘
```

### Additional Components

- **DTOs (Data Transfer Objects)**
  - `FruitRequestDTO`: Input data (validated)
  - `FruitResponseDTO`: Output data
  
- **Mapper**
  - `FruitMapper`: Entity ↔ DTO conversion

- **Exceptions**
  - `FruitNotFoundException`: Fruit not found
  - `GlobalExceptionHandler`: Centralized error handling

---

## 🌐 Endpoints

Base URL: `http://localhost:9000`

### 1. Create Fruit

**Endpoint:** `POST /fruits`

**Request Body:**
```json
{
  "name": "Apple",
  "weightInKilos": 5
}
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "name": "Apple",
  "weightInKilos": 5
}
```

**Errors:**
- `400 Bad Request` - Invalid data (empty name, negative weight)

---

### 2. Get All Fruits

**Endpoint:** `GET /fruits`

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "Apple",
    "weightInKilos": 5
  },
  {
    "id": 2,
    "name": "Banana",
    "weightInKilos": 3
  }
]
```

**If no fruits:** `200 OK` with empty array `[]`

---

### 3. Get Fruit by ID

**Endpoint:** `GET /fruits/{id}`

**Response:** `200 OK`
```json
{
  "id": 1,
  "name": "Apple",
  "weightInKilos": 5
}
```

**Errors:**
- `404 Not Found` - Fruit doesn't exist
```json
{
  "status": 404,
  "message": "Fruit not found with id: 999",
  "timestamp": "2025-12-11T10:30:00"
}
```

---

### 4. Update Fruit

**Endpoint:** `PUT /fruits/{id}`

**Request Body:**
```json
{
  "name": "Green Apple",
  "weightInKilos": 10
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "name": "Green Apple",
  "weightInKilos": 10
}
```

**Errors:**
- `404 Not Found` - Fruit doesn't exist
- `400 Bad Request` - Invalid data

---

### 5. Delete Fruit

**Endpoint:** `DELETE /fruits/{id}`

**Response:** `204 No Content`

**Errors:**
- `404 Not Found` - Fruit doesn't exist

---

### 6. Health Check (Actuator)

**Endpoint:** `GET /actuator/health`

**Response:** `200 OK`
```json
{
  "status": "UP"
}
```

---

## 🚀 Installation

### Prerequisites

- **Java 21** or higher ([Download](https://adoptium.net/))
- **Maven 3.9+** ([Download](https://maven.apache.org/download.cgi))
- **Git** ([Download](https://git-scm.com/))

### Clone Repository

```bash
git clone https://github.com/your-username/04.01_fruit-api-h2.git
cd 04.01_fruit-api-h2
```

### Build Project

```bash
# With Maven Wrapper (recommended)
./mvnw clean install

# Or with Maven installed
mvn clean install
```

---

## ▶️ Execution

### Option 1: Run with Maven

```bash
./mvnw spring-boot:run
```

### Option 2: Run JAR

```bash
# Build
./mvnw clean package

# Run
java -jar target/04.01_fruit-api-h2-0.0.1-SNAPSHOT.jar
```

### Option 3: From IDE

Run main class:
```
cat.itacademy.s04.t02.n01.FruitApiH2Application
```

### Verify It Works

```bash
curl http://localhost:9000/actuator/health
```

You should see:
```json
{"status":"UP"}
```

---

## 🧪 Tests

The project is developed with **TDD (Test-Driven Development)** following the **Outside-In** approach.

### Run All Tests

```bash
./mvnw test
```

### Run Tests with Coverage

```bash
./mvnw test jacoco:report
```

Report will be at: `target/site/jacoco/index.html`

### Test Types

#### 1. Integration Tests (`FruitControllerIntegrationTest`)

- Test the complete application flow
- Use `@SpringBootTest` + `@AutoConfigureMockMvc`
- Real H2 database
- Automatic rollback with `@Transactional`

**Example:**
```java
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class FruitControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testCreateFruit_Success() throws Exception {
        mockMvc.perform(post("/fruits")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Apple\",\"weightInKilos\":5}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Apple"));
    }
}
```

#### 2. Service Unit Tests (`FruitServiceTest`)

- Test isolated business logic
- Use `@ExtendWith(MockitoExtension.class)`
- Mock Repository

#### 3. Mapper Tests (`FruitMapperTest`)

- Verify Entity ↔ DTO conversions

### Outside-In TDD Methodology

Development followed the cycle:

```
1. 🔴 RED      → Write test (fails)
2. 🟢 GREEN    → Implement minimal code (passes)
3. 🔵 REFACTOR → Improve code (tests still pass)
4. 💾 COMMIT   → Save progress
```

**Implementation order:**
1. POST /fruits
2. GET /fruits
3. GET /fruits/{id}
4. PUT /fruits/{id}
5. DELETE /fruits/{id}

---

## 🐳 Docker

### Multi-Stage Dockerfile

The project includes an **optimized Dockerfile** with two-stage build:

**Stage 1 - BUILD:**
- Uses `maven:3.9.6-amazoncorretto-21`
- Compiles the application
- Generates the `.jar` file

**Stage 2 - RUNTIME:**
- Uses `amazoncorretto:21-alpine` (lightweight)
- Only copies the `.jar`
- Runs with non-root user
- Includes health check

**Benefit:** Final image ~180MB vs ~850MB (78% reduction)

### Build Image

```bash
docker build -t fruit-api:1.0 .
```

### Run Container

```bash
# Foreground
docker run -p 9000:9000 fruit-api:1.0

# Background
docker run -d -p 9000:9000 --name fruit-api fruit-api:1.0
```

### Environment Variables

```bash
docker run -d \
  -p 9000:9000 \
  -e JAVA_OPTS="-Xms512m -Xmx1024m" \
  -e SPRING_PROFILES_ACTIVE=prod \
  --name fruit-api \
  fruit-api:1.0
```

### Verify Container Health

```bash
# View logs
docker logs -f fruit-api

# Health check
docker inspect --format='{{json .State.Health}}' fruit-api

# Access container
docker exec -it fruit-api sh
```

---

## 📁 Project Structure

```
fruit-api-h2/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── cat/itacademy/s04/t02/n01/
│   │   │       ├── FruitApiH2Application.java      # Main class
│   │   │       ├── controllers/
│   │   │       │   └── FruitController.java        # REST Controller
│   │   │       ├── services/
│   │   │       │   └── FruitService.java           # Business logic
│   │   │       ├── repository/
│   │   │       │   └── FruitRepository.java        # Data access
│   │   │       ├── model/
│   │   │       │   └── Fruit.java                  # JPA Entity
│   │   │       ├── dto/
│   │   │       │   ├── FruitRequestDTO.java        # Input DTO
│   │   │       │   └── FruitResponseDTO.java       # Output DTO
│   │   │       ├── mapper/
│   │   │       │   └── FruitMapper.java            # Entity ↔ DTO
│   │   │       └── exceptions/
│   │   │           ├── FruitNotFoundException.java # Custom exception
│   │   │           ├── ErrorResponse.java          # Error response
│   │   │           └── GlobalExceptionHandler.java # Global handling
│   │   └── resources/
│   │       ├── application.properties              # Configuration
│   │       └── application-prod.properties         # Prod config
│   └── test/
│       └── java/
│           └── cat/itacademy/s04/t02/n01/
│               └── controllers/
│                   └── FruitControllerIntegrationTest.java
├── Dockerfile                                      # Multi-stage build
├── .dockerignore                                   # Docker exclusions
├── pom.xml                                         # Maven dependencies
└── README.md                                       # This file
```

---

## ✅ Validations

The project implements validations with **Bean Validation**:

### FruitRequestDTO

```java
public class FruitRequestDTO {
    
    @NotBlank(message = "Name cannot be empty")
    private String name;
    
    @Positive(message = "Weight must be greater than 0")
    private int weightInKilos;
}
```

### Validation Rules

| Field | Validation | Error Message |
|-------|-----------|---------------|
| `name` | Not empty | "Name cannot be empty" |
| `weightInKilos` | Greater than 0 | "Weight must be greater than 0" |

### Validation Error Example

**Request:**
```json
{
  "name": "",
  "weightInKilos": -5
}
```

**Response:** `400 Bad Request`
```json
{
  "status": 400,
  "message": "Name cannot be empty, Weight must be greater than 0",
  "timestamp": "2025-12-11T10:30:00"
}
```

---

## ⚠️ Error Handling

### GlobalExceptionHandler

Handles all exceptions centrally:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(FruitNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFruitNotFound(...) {
        // Returns 404 Not Found
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(...) {
        // Returns 400 Bad Request
    }
}
```

### HTTP Status Codes

| Code | Situation |
|------|-----------|
| 200 OK | Successful operation (GET, PUT) |
| 201 Created | Resource created (POST) |
| 204 No Content | Successful deletion (DELETE) |
| 400 Bad Request | Validation failed |
| 404 Not Found | Resource not found |
| 500 Internal Server Error | Server error |

### ErrorResponse Structure

```json
{
  "status": 404,
  "message": "Fruit not found with id: 999",
  "timestamp": "2025-12-11T10:30:00"
}
```

---

## 🔧 Configuration

### application.properties

```properties
# H2 Database
spring.datasource.url=jdbc:h2:mem:fruitdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# H2 Console (for debugging)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Actuator
management.endpoints.web.exposure.include=health,info
management.endpoint.health.show-details=when-authorized
```

### Access H2 Console (Development)

1. Run the application
2. Go to: http://localhost:8080/h2-console
3. JDBC URL: `jdbc:h2:mem:fruitdb`
4. Username: `sa`
5. Password: (empty)

---

## 📊 Usage Examples

### Using cURL

```bash
# 1. Create fruit
curl -X POST http://localhost:9000/fruits \
  -H "Content-Type: application/json" \
  -d '{"name":"Apple","weightInKilos":5}'

# 2. Get all
curl http://localhost:9000/fruits

# 3. Get by ID
curl http://localhost:9000/fruits/1

# 4. Update
curl -X PUT http://localhost:9000/fruits/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Green Apple","weightInKilos":10}'

# 5. Delete
curl -X DELETE http://localhost:9000/fruits/1
```

### Using Postman

1. Import collection from: `docs/Fruit-API.postman_collection.json` (if you create it)
2. Execute requests from the interface

---

## 🎓 Project Learnings

This project allows learning and applying:

✅ **REST API** with Spring Boot  
✅ **Complete CRUD** with proper HTTP verbs  
✅ **MVC Architecture** in layers  
✅ **Persistence** with Spring Data JPA  
✅ **H2 in-memory database**  
✅ **DTOs** to separate layers  
✅ **Validations** with Bean Validation  
✅ **Global exception handling**  
✅ **Outside-In TDD** with integration tests  
✅ **Docker** with multi-stage build  
✅ **Health checks** with Actuator  
✅ **Security** (non-root user in Docker)  
✅ **Development best practices**  
