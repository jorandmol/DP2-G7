Feature: Update Pet Type
  As an admin, I want to update pet types.

  Scenario: Update pet types (positive)
    Given I am not logged in the system
    When Im logged in the system as "admin1"
    And Update a pet type correctly
    Then Pet type name changes

  Scenario: Update pet type with same name(negative)
 Given I am not logged in the system
    When Im logged in the system as "admin1"
    And Update a pet type with the same name
    Then Pet type name doesnt changes

