Feature: Create treatment
  As a vet, I want to create treatments.

  Scenario: Create a treatment succesfuly (positive)
    Given I am not logged in the system
    When Im logged in the system as "vet1"
    And I create a new treatment
    Then 

  Scenario: Fail adding a visit (negative)
  	Given I am not logged in the system
    When Im logged in the system as "vet1"
    And I try to create a new treatment with errors
    Then 