**Prompt for Generating End-to-End Test Code**

**Persona:** You are a world-class Senior QA Automation Engineer specializing in creating robust, maintainable, and reliable end-to-end tests for modern Java applications.

**Objective:** Write a complete and precise end-to-end integration test for the modernized Spring Boot `shopping-cart` application. This test must simulate a critical user journey: a customer successfully finding a product, adding it to their cart, and completing the checkout process.

**Project Context:**
-   **Application Root:** `./mod.phase3.out/`
-   **Test Module:** The test code should be placed within the `integration-tests` Maven module.
-   **Target URL:** The application is assumed to be running and accessible at `http://localhost:8080`.

**Technical Requirements:**
1.  **Frameworks:** Use **JUnit 5** as the primary testing framework.
2.  **Browser Automation:** Use **Selenium WebDriver** to control the web browser and simulate user interactions.
3.  **Test Dependencies:** Employ **Testcontainers** to manage a PostgreSQL database instance, ensuring the test runs in a clean, isolated environment. The test should initialize the database with necessary seed data (e.g., a test user account, product catalog).
4.  **Design Pattern:** Implement the **Page Object Model (POM)** to create a scalable and maintainable test structure. Each page of the application (e.g., LoginPage, HomePage, ProductPage, CartPage, CheckoutPage) should have its own corresponding Page Object class.
5.  **Assertions:** Use a comprehensive set of assertions to validate the application's state at each step of the journey.

**User Journey to Automate (Happy Path):**
1.  **Setup:**
    -   Start the Testcontainer with a PostgreSQL database.
    -   Initialize the database with schema and seed data, including at least one product and one test user (e.g., `testuser@example.com` / `password123`).
2.  **Login:**
    -   Navigate to the login page.
    -   Enter the test user's credentials.
    -   Submit the login form.
    -   Assert that the login was successful and the user is redirected to the home page.
3.  **Add to Cart:**
    -   From the home page, search for or navigate to a specific product.
    -   Click the "Add to Cart" button for that product.
    -   Assert that a confirmation message is displayed.
4.  **Verify Cart:**
    -   Navigate to the shopping cart page.
    -   Assert that the correct product, quantity, and price are displayed in the cart.
5.  **Checkout:**
    -   Click the "Proceed to Checkout" button.
    -   Fill in the required shipping and payment information (using dummy data).
    -   Submit the order.
6.  **Verify Order Confirmation:**
    -   Assert that the user is redirected to an order confirmation page.
    -   Assert that an order number is displayed and the page contains the correct order summary.
7.  **Teardown:**
    -   The browser instance should be closed.
    -   The Testcontainer should be shut down automatically after the test completes.

**Final Output:**
-   Provide the complete, compilable Java code for the test, including all necessary Page Object classes.
-   Place the main test file at: `mod.phase3.out/integration-tests/src/test/java/com/example/shoppingcart/e2e/UserCheckoutJourneyTest.java`.
-   Place Page Object classes in an appropriate package, such as `com.example.shoppingcart.e2e.pages`.
-   Include comments where the logic is complex, especially for selectors and test data setup.