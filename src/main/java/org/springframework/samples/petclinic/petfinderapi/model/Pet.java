package org.springframework.samples.petclinic.petfinderapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "animal",
    "pagination"
})
@JsonIgnoreProperties(value = {"pagination"})
public class Pet {
	Animal AnimalObject;

	// Getter Methods

	public Animal getAnimal() {
		return AnimalObject;
	}

	// Setter Methods

	public void setAnimal(Animal animalObject) {
		this.AnimalObject = animalObject;
	}

}
