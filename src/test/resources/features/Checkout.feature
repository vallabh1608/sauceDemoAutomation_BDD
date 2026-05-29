Feature: Checkout and Purchase Operations

  Scenario: Complete end-to-end purchase workflow
    Given User is logged in with valid credentials
    When User adds all 6 products to cart
    Then Cart badge count should equal total product count
    When User navigates to cart page
    And User initiates checkout process
    And User enters checkout details with firstname, lastname, and zipcode
    And User clicks continue button
    Then Order summary should display subtotal, tax, and total amounts
    And Total amount should equal subtotal plus tax
    When User clicks finish button
    Then Confirmation message should display "Thank you for your order!"
    And Order completion workflow should succeed
