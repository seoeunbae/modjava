# Technology Stack

This document outlines the technology stack for the Shopping Cart application.

| Category          | Technology                                | Version       | Notes                                            |
|-------------------|-------------------------------------------|---------------|--------------------------------------------------|
| Language          | Java                                      | 17            | As defined in the parent `pom.xml`.              |
| Framework         | Spring Boot                               | 3.4.0         | Core application framework.                      |
| Web               | Spring Web                                | (Inherited)   | For building web applications and REST APIs.     |
| Templating        | Thymeleaf                                 | (Inherited)   | Server-side Java template engine.                |
| Security          | Spring Security                           | (Inherited)   | For authentication and authorization.            |
| Data Access       | Spring Data JPA                           | (Inherited)   | For database interaction.                        |
| Database          | PostgreSQL                                | (Driver)      | The relational database system.                  |
| Cloud Integration | Google Cloud SQL for PostgreSQL           | (Inherited)   | For connecting to managed PostgreSQL on GCP.     |
| Build Tool        | Maven                                     |               | Dependency management and build automation.      |
| Testing           | JUnit 5, Mockito, Spring Boot Test        | (Inherited)   | For unit and integration testing.                |
| UI Testing        | Selenium                                  | 4.21.0        | For browser automation and end-to-end testing.   |
