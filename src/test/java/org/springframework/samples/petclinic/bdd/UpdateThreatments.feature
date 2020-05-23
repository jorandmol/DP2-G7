Feature: Threatment management
  As a vet I want to manage threatments.

Scenario: Threatment update(Positive)
  Given:Im logged in the system as 'vet1'
  When: I update one threatment
  Then: the threatment changes