
Feature: Shopping Cart

  Scenario: Add a product to the cart
    Given a user is viewing a product
    When they click the "Add to Cart" button
    Then the product should be added to their shopping cart

  Scenario: View shopping cart
    Given a user has items in their shopping cart
    When they navigate to the cart page
    Then they should see the list of products in their cart

  Scenario: Update product quantity in the cart
    Given a user is on the shopping cart page
    When they change the quantity of a product
    And click the update button
    Then the cart should be updated with the new quantity

  Scenario: Remove a product from the cart
    Given a user has a product in their shopping cart
    When they remove the product from the cart
    Then the product should be removed from their shopping cart
