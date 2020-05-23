Feature: Pet update
  As a owner I want to manage my pet.

Scenario: Pet update(Positive)
  Given:Im logged in the system as 'owner2'
  When: I unsubscribe my pet
  Then: My pet dissapears
  
 Scenario: Pet update(Negative)
  Given:Im logged in the system as 'owner2'
  When: I unsubscribe my pet with visits
  Then: My pet doesnt dissapears