Feature: Product Inventory Management

  Scenario: Verify product sorting functionality
    Given User is logged in with valid credentials
    When User changes sort option to "Price (low to high)"
    Then All products should be sorted by price in ascending order
    When User changes sort option to "Name (Z to A)"
    Then All products should be sorted by name in reverse alphabetical order

  Scenario: Verify product detail page and navigation
    Given User is logged in with valid credentials
    When User clicks on the product at index 1
    Then Product detail page should display product name, description, and price
    And Product price should contain currency symbol "$"
    And Product description should not be empty
    When User clicks "Add to Cart" on detail page
    Then Cart badge count should increment to 1
    When User clicks "Back to Products" button
    Then User should be redirected to inventory.html page
    And Inventory page title should be "Products"

  Scenario: Verify product image and accessibility consistency
    Given User is logged in with valid credentials
    When User validates all product images on inventory page
    Then Each product image should have a valid src attribute
    And No product image should have broken link indicator "sl-404"
    And Each product image alt text should match the product name
    When User clicks on the first product image
    Then Product detail page should load for the clicked product
    And Detail page product name should match the clicked product name
