# Workflow: E2E Test Generation

**Source:** `GEMINI-phase4.md`

This workflow guides a QA agent in generating a single, comprehensive end-to-end test for a critical user journey.

## 1. Activate Persona
- **Action**: User activates the QA persona.
- **Command**: `*qa`

## 2. Design Test Scenarios
- **Agent**: `*qa`
- **Action**: Execute the `test-design` task for the "User Checkout Journey".
- **Input**: The user journey description from `GEMINI-phase4.md`.

## 3. Implement Page Object Model (POM)
- **Agent**: `*dev` or `*qa`
- **Action**: For each page (Login, Home, Cart, etc.), create a corresponding Java Page Object class.

## 4. Implement Test Class
- **Agent**: `*qa`
- **Action**: Create the main test class (`UserCheckoutJourneyTest.java`) that uses the Page Objects to execute the user journey.
- **Reference**: The technical requirements in `GEMINI-phase4.md` (JUnit 5, Selenium, Testcontainers).

## 5. Execute and Verify
- **Agent**: `*qa`
- **Action**: Run the newly created test and debug until it passes.
- **Output**: A passing end-to-end test and a final report.
