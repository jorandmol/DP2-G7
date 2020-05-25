Feature: Add visit
  As a vet, I want to add visits.

  Scenario: Add a visit succesfuly (positive)
    Given I am not logged in the system
    When Im logged in the system as "vet1"
    And I add a visit
    Then A registered message appears

  Scenario: Fail adding a visit (negative)
  	Given I am not logged in the system
    When Im logged in the system as "vet1"
    And I try to add a visit with errors
    Then Error message appears