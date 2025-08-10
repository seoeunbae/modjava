
Feature: User Authentication

  Scenario: User Registration
    Given a user is on the registration page
    When they enter their details
    And click the register button
    Then a new user account should be created
    And the user should be redirected to the login page

  Scenario: User Login
    Given a user is on the login page
    When they enter their email and password
    And click the login button
    Then the user should be authenticated
    And they should be redirected to their home page

  Scenario: User Logout
    Given a logged-in user
    When they click the logout button
    Then the user session should be invalidated
    And they should be redirected to the login page
