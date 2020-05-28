Feature: Accept or reject a stay
  As an admin, I want to accept or reject a pending stay.

  Scenario: Accept a stay (positive)
    Given I am not logged in the system
    When Im logged in the system as "admin1"
    And I accept a pending stay request
    Then The stay is accepted

  Scenario: Reject a stay (positive)
    Given I am not logged in the system
    When Im logged in the system as "admin1"
    And I reject a pending stay request
    Then The stay is rejected

	Scenario: Reject an accepted stay (negative)
	    Given I am not logged in the system
	    When Im logged in the system as "admin1"
	    And I try to reject an accepted stay
	    Then I get an error

