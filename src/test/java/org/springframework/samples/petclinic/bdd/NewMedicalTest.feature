Feature: Create a medical test
  As an admin, I want to create a medical test.

  Scenario: Create a medical test (positive)
    Given I am not logged in the system
    When Im logged in the system as "admin1"
    And I see medical tests and I want to create a new one
    And I introduce correct data
    Then There are more medical tests

  Scenario: Create a medical test with empty description (negative)
  	Given I am not logged in the system
    When Im logged in the system as "admin1"
    And I see medical tests and I want to create a new one
    And I introduce incorrect data
    Then Not created because the description is empty

