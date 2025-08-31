# Coding Standards

This document outlines the coding standards and conventions for the Shopping Cart application.

### General Conventions

*   **Language**: All backend code is written in Java 17.
*   **Dependency Injection**: The project uses Spring's dependency injection (`@Autowired`) to manage components.
*   **Logging**: SLF4J is the standard logging facade. All new code should use `LoggerFactory.getLogger()` to create a logger instance for its class.
*   **Validation**: Jakarta Bean Validation (`@Valid`) is used for validating request payloads.

### Naming Conventions

*   **Packages**: `com.shoppingcart.*`
*   **Classes**: PascalCase (e.g., `ProductController`).
*   **Methods**: camelCase (e.g., `getAllProducts`).
*   **Constants**: UPPER_SNAKE_CASE (e.g., `LOGGER`).

### Controller Layer

*   Controllers should be annotated with `@Controller`.
*   Request mappings should be clearly defined using `@GetMapping`, `@PostMapping`, etc., with descriptive paths.
*   Controllers should delegate business logic to service classes and not contain it directly.
*   Use `Model` to pass data to the Thymeleaf templates.

### Service Layer

*   Services should be annotated with `@Service`.
*   They contain the core business logic of the application.

### Data Access Layer

*   Repositories should extend Spring Data's `JpaRepository`.
*   Custom query methods should follow Spring Data's query creation from method name conventions where possible.

### Code Formatting

*   The existing code follows standard Java formatting conventions (e.g., indentation, brace style). All new contributions should match this style to maintain consistency.
