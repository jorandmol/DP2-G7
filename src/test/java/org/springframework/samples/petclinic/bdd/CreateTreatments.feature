Feature: Create treatment
  As a vet, I want to create treatments.

  Scenario: Create a treatment succesfuly (positive)
    Given I am not logged in the system
    When Im logged in the system as "vet1"
    And I create a new treatment
    Then A new treatment appears

  Scenario: Fail creating a treatment (negative)
  	Given I am not logged in the system
    When Im logged in the system as "vet1"
    And I try to create a new treatment with errors
    Then An error appears