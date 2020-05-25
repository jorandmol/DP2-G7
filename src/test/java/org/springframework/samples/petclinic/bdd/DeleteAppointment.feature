Feature: Delete appointment
  As an owner, I want to delete appoitments.

  Scenario: Delete an appointment succesfuly (positive)
    Given I am not logged in the system
    When Im logged in the system as "owner1"
    And I delete an appointment
    Then The number of appointments decrease in one

  Scenario: Fail deleting an appointment (negative)
  	Given I am not logged in the system
    When Im logged in the system as "owner1"
    And I try to delete an appointment with 2 days or left to go
    Then An error message appears