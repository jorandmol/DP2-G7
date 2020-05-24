Feature: Accept or reject a pet request
  As an admin, I want to accept or reject a pet request.

  Scenario: Accept a pet request (positive)
    Given I am not logged in the system
    When Im logged in the system as "admin1"
    And There are pet requests
    And I want to accept that request
    Then Is accepted so there are less pet requests

  Scenario: Reject a pet request with no justification (negative)
  	Given I am not logged in the system
    When Im logged in the system as "admin1"
    And There are pet requests
    And I want to reject other pet request
    Then Not rejected because the justification is needed

