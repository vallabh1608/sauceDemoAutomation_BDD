Feature: Authentication and Session Management

  Scenario Outline: Login with various user profiles
    When User attempts login with username "<username>" and password "<password>"
    Then Login validation should result in "<result>"
    And If valid login, inventory page title should be "Products"
    And If invalid login, error message should contain "<errorMessage>"

    Examples: Valid Login Attempts
      | username      | password   | result      | errorMessage |
      | standard_user | secret_sauce | Valid Login |              |

    Examples: Invalid Login Attempts
      | username           | password   | result        | errorMessage |
      | locked_out_user    | secret_sauce | Invalid Login | Sorry, this user has been locked out. |
      | invalid_user       | secret_sauce | Invalid Login | Username and password do not match any user in this service |

  Scenario: Verify logout and session validation
    Given User is logged in with valid credentials
    When User clicks on menu button
    And User clicks on logout option
    Then Current URL should redirect to login page
    And Direct URL injection to inventory.html without auth should redirect to login
    And System should prevent unauthorized access to protected routes
