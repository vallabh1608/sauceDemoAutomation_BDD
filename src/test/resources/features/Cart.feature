Feature: Shopping Cart Operations

  Scenario: Verify cart badge updates on add and remove operations
    Given User is logged in with valid credentials
    When User adds 3 products to cart iteratively
    Then Cart badge count should display 3
    And Each added product button text should change to "Remove"
    When User removes the first product from inventory
    Then Cart badge count should decrement to 2
    When User navigates to cart page
    Then Cart should contain exactly 2 products
    And Cart product names should match the added products (excluding the removed one)

  Scenario: Verify cart persistence and removal functionality
    Given User is logged in with valid credentials
    When User adds 2 products to cart
    Then Cart badge count should display 2
    When User navigates through product detail page and back to inventory
    Then Cart badge should still display 2
    And Cart contents should persist in cache memory
    When User navigates to cart page
    And User removes the first item from cart
    Then Cart should contain exactly 1 product
    And Remaining product name and price should remain unchanged
    And Remaining product should match the second added product

  Scenario: Verify cross-user cart isolation and data boundaries
    Given User logs in as "standard_user" with password
    When User adds 2 products to cart
    Then Cart badge count should display 2
    When User logs out and clears all cookies and storage
    And User logs in as "problem_user" with password
    When User navigates to cart page
    Then Cart should be completely empty with 0 items
    When User adds 1 product from inventory
    And User logs out and clears all cookies and storage
    And User logs in as "standard_user" again with password
    When User navigates to cart page
    Then Cart should be empty with 0 items
    And Data isolation prevents cross-user cart contamination
