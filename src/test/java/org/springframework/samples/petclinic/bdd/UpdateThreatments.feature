Feature: Update Threatment
  As a vet, I want to update threatments.

  Scenario: Update threatments (positive)
    Given I am not logged in the system
    When Im logged in the system as "vet1"
    And Update a threatment correctly
    Then Threatment data changes

  Scenario: Update threatments with no changes (negative)
 Given I am not logged in the system
    When Im logged in the system as "vet1"
    And Update a threatment with no changes
    Then Threatment data doesnt changes
