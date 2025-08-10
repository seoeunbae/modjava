
# Technical Design: Spring Boot Shopping Cart

This document outlines the technical design for migrating the legacy J2EE Shopping Cart application to a modern Spring Boot application.

## 1. Project Structure

The project will be a multi-module Maven project with the following structure:

```
mod2.out/
├── pom.xml
├── web-app/ (Spring Boot Application)
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/example/shoppingcart/
│           │       ├── controller/
│           │       ├── service/
│           │       ├── repository/
│           │       ├── model/
│           │       └── ShoppingCartApplication.java
│           └── resources/
│               ├── static/
│               └── templates/
└── integration-tests/ (Integration Tests)
    ├── pom.xml
    └── src/
        └── test/
            └── java/
                └── com/example/shoppingcart/integration/
```

*   **web-app**: The main Spring Boot application module.
*   **integration-tests**: A separate module for end-to-end integration tests.

## 2. Key Libraries and Frameworks

*   **Spring Boot**: Core framework for the application.
*   **Spring Web**: For building the web layer, including REST controllers.
*   **Spring Data JPA**: For data access and persistence.
*   **Thymeleaf**: For server-side rendering of HTML templates.
*   **Spring Security**: For authentication and authorization.
*   **PostgreSQL Driver**: To connect to the PostgreSQL database.
*   **JUnit 5**: For unit testing.
*   **Mockito**: For mocking dependencies in unit tests.
*   **Testcontainers**: For running integration tests with a real PostgreSQL database.
*   **Selenium**: For browser automation in integration tests.

## 3. Architecture

The application will follow a layered architecture:

*   **Controller Layer**: Handles incoming HTTP requests, delegates to the service layer, and returns responses (either RESTful JSON or rendered HTML).
*   **Service Layer**: Contains the core business logic of the application.
*   **Repository Layer**: Responsible for data access and persistence, using Spring Data JPA repositories.
*   **Model Layer**: Contains the JPA entity classes that map to the database tables.

## 4. Database Migration

The existing MySQL database schema will be migrated to PostgreSQL. The schema defined in `gemini-docs/1-database-schema.md` will be used as the basis for the new PostgreSQL database. The `image` column in the `product` table will be changed from `LONGBLOB` to a `BYTEA` in PostgreSQL for storing image data.

## 5. Testing Strategy

*   **Unit Tests**: Each service and controller will have corresponding unit tests to verify its logic in isolation. Mockito will be used to mock dependencies.
*   **Integration Tests**: End-to-end integration tests will be written to verify complete user journeys. Testcontainers will be used to spin up a PostgreSQL database, and Selenium will be used for browser automation.
