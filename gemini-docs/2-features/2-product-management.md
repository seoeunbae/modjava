
Feature: Product Management

  Scenario: Add a new product
    Given an admin is on the add product page
    When they enter the product details
    And click the add button
    Then a new product should be created

  Scenario: Update a product
    Given an admin is on the update product page
    When they modify the product details
    And click the update button
    Then the product information should be updated

  Scenario: Remove a product
    Given an admin is on the remove product page
    When they select a product to remove
    And click the remove button
    Then the product should be removed from the system
