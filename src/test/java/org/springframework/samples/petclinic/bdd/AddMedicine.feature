Feature: Add a new medicine
  As an admin, I want to add a new medicine to the system.

  Scenario: Add a new medicine (positive)
    Given I am not logged in the system
    When Im logged in the system as "admin1"
    And I see medicines and I want to add more
    And I introduce correctly its data
    Then There are more medicines

  Scenario: Add a medicine with used code (negative)
  	Given I am not logged in the system
    When Im logged in the system as "admin1"
    And I see medicines and I want to add more
    And I introduce correctly its data but its code is repeated
    Then There are the same number of medicines

