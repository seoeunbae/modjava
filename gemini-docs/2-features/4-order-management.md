
Feature: Order Management

  Scenario: Place an order
    Given a user has items in their shopping cart
    When they proceed to checkout
    And confirm the order
    Then a new order should be created

  Scenario: View order details
    Given a user has placed an order
    When they navigate to their order history
    Then they should see the details of their past orders

  Scenario: Track shipment
    Given an order has been shipped
    When the user views the order details
    Then they should see the shipping status
