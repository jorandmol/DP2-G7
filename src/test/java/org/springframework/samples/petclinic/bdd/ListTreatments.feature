Feature: List treatments
  As an owner, I want to list my pets treatments.

  Scenario: List treatments succesfuly (positive)
    Given I am not logged in the system
    When Im logged in the system as "owner1"
    And I list my pets treatments
    Then Two lists of current and expired treatments appear

  Scenario: Fail listing treatments (negative)
  	Given I am not logged in the system
    When Im logged in the system as "owner1"
    And I try to list other owner´s treatment
    Then I get redirected to error page