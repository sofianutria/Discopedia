# 💿 Discopedia — Backend API 💿

Discopedia is a RESTful API designed to manage users, music records, and reviews. Users can create accounts, explore and create music records, and leave reviews. This backend is built with Java and Spring Boot, following modern practices such as DTOs, global exception handling, validation, and secure authentication mechanisms (JWT planned).

With Discopedia, music enthusiasts gain the ability to:

🎵 Manage Music Records – Create, update, explore, and delete music records.

👤 Manage Users – Register, authenticate, and manage users with role-based access (admin/user).

📝 Leave Reviews – Users can leave reviews for music records.

🔒 Secure Access – Planned JWT authentication for protected endpoints.

📈 Trace Activities – Full logging and validation ensure consistency and transparency.

# 📑 Table of Contents
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [CI/CD Pipeline](#cicd-pipeline)
- [Dockerhub](#docker-hub)
- [Configuration](#configuration)
- [Usage](#usage)
- [Endpoints](#endpoints)
- [Dependencies](#dependencies)
- [Testing](#testing)
- [Contributors](#contributors)

## Features

Discopedia provides a powerful set of features:

🥇 RESTful API with Spring Boot for managing users, music records, and reviews.

🔒 Authentication & planned JWT-based authorization for secure access.

🎯 Full CRUD operations for music records and reviews.

✅ DTOs and validation ensure consistent and reliable data.

⚠️ Centralized exception handling for clear error responses.

🧪 Tests covering core functionality (~70% coverage).

## Technologies Used

- Java 17+
- Spring Boot (Web, Data JPA, Security, Mail)
- Maven
- MySQL
- Git & GitHub

## Prerequisites

Before you start, ensure you have installed:

- Java 17 or higher
- Maven
- MySQL (running locally)
- IDE like IntelliJ IDEA or VS Code (optional)

## Installation

1. Clone the repository:
```
git clone https://github.com/sofianutria/Discopedia.git
cd discopedia
```
2. Configure the database:

- Ensure MySQL server is running.
- Create a database, e.g., discopedia_db.
- Update src/main/resources/application.properties with your credentials:
```
spring.datasource.url=jdbc:mysql://localhost:3306/discopedia_db?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true&useSSL=false
spring.datasource.username=your-username
spring.datasource.password=your-password
spring.jpa.hibernate.ddl-auto=update
```
3. Build and run the project:
```
./mvnw clean install
./mvnw spring-boot:run
```

4. Optional: Run with Docker
```
docker compose up -d
```
## CI/CD Pipeline
This project uses GitHub Actions to implement a complete CI/CD pipeline with three main stages:
Implemented Workflows
### 1. Build
File: .github/workflows/build.yml

Trigger: Push to any branch

Function: Compiles the project and verifies there are no compilation errors

### 2. Test

File: .github/workflows/test.yml

Trigger: Push to main and Pull Requests

Function: Executes all unit and integration tests

### 3. Release

File: .github/workflows/release.yml

Trigger: Push to main branch

Function: Builds Docker image and publishes it to Docker Hub

Registry: Docker Hub (sofianutria/discopedia)

## Docker Hub
Repository: sofianutria/discopedia

Images are automatically tagged with:

- latest: Latest stable version
- main: Latest version from main branch
- sha-XXXXXXX: Specific commit hash


## Configuration

Key configurations are in src/main/resources/application.properties:

- Database Settings – MySQL connection.
- JWT Security – Secret key for signing tokens (change for production).
- Email Service – Optional settings for notifications.

## Usage

Discopedia is a RESTful API. You can use Postman, cURL, or any HTTP client to interact with it.

Authentication: JWT is planned; endpoints may require roles for access.

## Endpoints

- User Endpoints:
- - POST /users/register – Register a new user
- - POST /users/login – Authenticate user and receive token
- - GET /users/{id} – Retrieve user info (role-based access)

- Music Record Endpoints:

- - POST /cd – Create a new music record

- - GET /cd – Get all music records

- - GET /cd/{id} – Get a specific music record

- - DELETE /cd/{id} – Delete a music record (admin only)

- Review Endpoints:

- - POST /reviews – Create a review for a music record

- - GET /reviews/{musicRecordId} – Get all reviews for a specific music record

## Dependencies

- spring-boot-starter-web – REST API with Spring MVC

- spring-boot-starter-data-jpa – JPA for database operations

- spring-boot-starter-security – Security features

- mysql-connector-j – MySQL JDBC driver

- jjwt-api, jjwt-impl, jjwt-jackson – JWT support (planned)

- lombok – Reduce boilerplate code

- spring-boot-starter-test – JUnit, Mockito, AssertJ for testing

- springdoc-openapi-starter-webmvc-ui – API documentation via Swagger

## Testing

The project includes unit and integration tests covering core functionalities (~70% coverage).

Run all tests with:
```
./mvnw test
```
## Contributors

This project is the result of passion for music and coding.

Main Contributor:

- Sofía Santos – Initial creator and maintainer

Contributions are welcome! You can:

- Open issues for bugs or feature requests

- Submit pull requests with improvements