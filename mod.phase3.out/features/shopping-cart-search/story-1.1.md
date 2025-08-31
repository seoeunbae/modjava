# Story 1.1: Implement Backend Search Logic
*   **As a** developer,
*   **I want** to create a robust method in the `ProductRepository` that searches for products by name and description,
*   **so that** the application has a reliable way to query the database for products matching a search term.
*   **Acceptance Criteria**:
    1.  A method exists in the `ProductRepository` that accepts a search string and returns a `List<Product>`.
    2.  The search is case-insensitive.
    3.  The method is covered by a unit test that verifies it returns correct results.
*   **Integration Verification**:
    1.  The new repository method does not break any existing repository methods or tests.
