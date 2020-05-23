Feature: PetType managment
  As an admin I want to manage PetTypes.

Scenario: PetType update(Positive)
  Given:Im logged in the system as 'admin'
  When: I update one pet type
  Then: The pet type changes
  
Scenario: PetType update(Negative)
  Given:Im logged in the system as 'admin'
  When: I update one pet type with the same name
  Then: The pet type doesnt changes