Feature: Create a stay request
  As an owner, I want to create a request for a stay.

  Scenario: Create a stay request (positive)
    Given I am not logged in the system
    When Im logged in the system as "owner1"
    And I see stays and I want to create a new one
    Then There are more stays

  Scenario: Create a stay request in same dates (negative)
  	Given I am not logged in the system
    When Im logged in the system as "owner1"
    And I see stays and I want to create a new one
    Then There is a stay request in those dates

