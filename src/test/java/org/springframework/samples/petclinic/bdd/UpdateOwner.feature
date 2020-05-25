Feature: Update an Owner
  As a owner, I want to change my profile.

  Scenario: Update a owner (positive)
    Given I am not logged in the system
    When Im logged in the system as "owner1"
    And I enter owner data correctly
    Then My profile changes

  Scenario: Update a owner with bad pass (negative)
 Given I am not logged in the system
    When Im logged in the system as "owner1"
    And I enter a bad password
    Then My profile doesnt changes
