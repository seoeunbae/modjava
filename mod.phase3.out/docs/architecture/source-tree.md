# Source Tree and Module Organization

This document describes the source code structure of the Shopping Cart application.

### Repository Structure

*   **Type**: Polyrepo (structured as a multi-module Maven project).
*   **Package Manager**: Maven

### Project Structure (Actual)

The project is organized into three main modules:

```text
shopping-cart/
├── data-access/     # Handles all database interactions (JPA Repositories).
├── service/         # Contains the core business logic and services.
└── web-app/         # The main web application, containing controllers, templates, and static assets.
```

### Key Modules and Their Purpose

*   **`data-access`**: This module is responsible for the persistence layer. It defines the JPA entities and repositories (`ProductRepository`, `UserRepository`, etc.) for interacting with the PostgreSQL database.
*   **`service`**: This module contains the application's business logic. It acts as an intermediary between the `web-app` and `data-access` layers, orchestrating data and performing core application functions.
*   **`web-app`**: This is the user-facing module. It contains the Spring MVC controllers that handle HTTP requests, the Thymeleaf templates that render the UI, and all static resources like CSS and JavaScript. It depends on the `service` module to perform its operations.
