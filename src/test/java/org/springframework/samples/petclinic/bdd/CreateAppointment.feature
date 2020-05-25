Feature: Create appointment
  As an owner, I want to create appointments.

  Scenario: Create an appointment succesfuly (positive)
    Given I am not logged in the system
    When Im logged in the system as "owner1"
    And I create an appointment
    Then A new appointment appears

  Scenario: Fail creating an appointment (negative)
  	Given I am not logged in the system
    When Im logged in the system as "owner1"
    And I try to create an appointment with errors
    Then An error message will appear