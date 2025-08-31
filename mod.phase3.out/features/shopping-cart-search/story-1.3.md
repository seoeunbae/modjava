# Story 1.3: Verify End-to-End Search Functionality
*   **As a** QA tester,
*   **I want** to perform an end-to-end test of the search feature,
*   **so that** I can verify it is working correctly from the UI to the database and is secure.
*   **Acceptance Criteria**:
    1.  Typing a search term in the UI and submitting returns the correct, filtered list of products.
    2.  Searching for a term that does not match any product displays the "No products found" message.
    3.  The feature is secure against basic SQL injection attempts.
*   **Integration Verification**:
    1.  A new integration test is created that covers the full, end-to-end search flow.
    2.  Manually verify that all other user actions (e.g., "Add to Cart", "View") still work correctly from the search results page.
