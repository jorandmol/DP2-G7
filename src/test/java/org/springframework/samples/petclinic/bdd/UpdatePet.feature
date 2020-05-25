Feature: Update a Pet
  As a owner, I want to turn down my pet.

  Scenario: Turn down a pet (positive)
    Given I am not logged in the system
    When Im logged in the system as "owner2"
    And Disable my pet
    Then My pet dissapears

  Scenario: Turn down a pet with events in progress(negative)
 Given I am not logged in the system
    When Im logged in the system as "owner2"
    And Disable my pet with events
    Then My pet doesnt dissapears

