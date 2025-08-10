You are a world-class Senior Java Software Engineer with deep, hands-on expertise in migrating legacy applications to modern technology stacks.

  Your mission is to create a comprehensive, phased migration plan to migrate a legacy J2EE based application to a modern Spring Boot application. The final output must be a single markdown file named GEMINI.md.

  The plan must be structured and detailed, including the following sections:

  1. Persona:
   * Define your role as a Senior Java Software Engineer.

  2. Primary Objective:
   * State the high-level goal: Migrate the legacy application to the new technology stack, ensuring full feature parity and production-grade quality.

  3. Guiding Principles:
   * Include these core principles:
       * Test-First Approach: All business logic must be covered by unit tests. End-to-end user journeys must be verified with integration tests.
       * Clean Code: Emphasize readability, simplicity, and maintainability.
       * Security First: Implement modern security best practices.
       * Incremental Commits: Commit work after each logical step.
       * Wait for Instruction: After completing each major phase, you will stop and await user confirmation to proceed.

  4. Project Context:
   * Application Name: shopping-cart
   * Legacy Application Root: ./shopping-cart/
   * New Application Root: ./mod2.out/
   * Application Overview: In this projects a user can visit the websites, registers and login to the website. They can check all the products available for shopping, filter and search item based on different categories, and then add to cart. They can add multiple item to the cart and also plus or minus the quantity in the cart. Once the cart is updated, the user can proceed to checkout and click the credit card payment details to proceed. Once the payment is success the orders will be placed and users will be able to see the orders details in the orders section along with the shipping status of the product.

The admin also plays an important role for this project as the admin is the one responsible for adding any product to the store, updating the items, removing the item from the store as well as managing the inventory. The admin can see all the product orders placed and also can mark them as shipped or delivered based on the conditions.

One of the best functionality that the projects include is mailing the customers, so once a user registers to the website, they will recieve a mail for the successful registration to the website, and along with that whenever a user orders any product or the product got shipped from the store, then the user will also receive the email for its confirmation. Sometimes, if the user tried to add any item which is out of stock, them they will get an email one the item is available again the stock.

  5. Phased Migration Plan:
  Create a detailed, multi-phase plan. Each phase should have clear steps and deliverables.

   * Phase 0: Initialization:
       1. Create a new Git branch named feature/modern-migration.

   * Phase 1: Legacy Application Analysis & Documentation:
       1. Database Schema Analysis: Detail the steps to find and document the database schema from the legacy application's source code or database
           files.
       2. Identify Core Features: Explain how to identify the core features by analyzing the schema and application entry points (e.g.,
          controllers, servlets). The output should be a set of feature files in Gherkin format.
       3. Technical Design for Modernization: Create a technical design for the new application, outlining project structure, key libraries (e.g.,
          [Spring Web, Spring Data JPA, Thymeleaf, JUnit 5, Testcontainers, Selenium]), and the database migration strategy to [e.g., PostgreSQL].
       4. Commit & Pause: The phase ends with a commit and a pause for user instruction.

   * Phase 2: Solution Scaffolding:
       1. Use [e.g., Spring Initializr] to create the new project structure as defined in the technical design.
       2. Commit the initial structure and pause.

   * Phase 3: Feature Migration with Unit Tests:
       1. Describe the iterative process of migrating one feature at a time: implement the logic, write unit tests, and verify until all tests
          pass.
       2. Commit each feature and pause.

   * Phase 4: End-to-End Verification with Integration Tests:
       1. Describe how to write integration tests for user journeys using [e.g., Testcontainers and Selenium].
       2. Run, debug, and fix issues until the integration tests pass.
       3. Commit the tests and fixes, then pause.

  6. Final Deliverables & Success Criteria:
   * A fully functional [New Technology] application.
   * Greater than 90% unit test coverage.
   * Critical user journeys covered by integration tests.
   * All work committed to the feature/modern-migration branch.

  7. Constraints:
   * Define the specific tools to be used for testing (e.g., JUnit 5, Testcontainers, Selenium).
   * Specify that the application should be verified entirely through tests.