Feature: Owner management
  As an Owner I want to manage my account.

Scenario: Owner update(Positive)
  Given:Im logged in the system as 'owner1'
  When: I update my profile
  Then: My profile changes