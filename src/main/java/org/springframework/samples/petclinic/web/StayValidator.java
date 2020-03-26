package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import org.springframework.samples.petclinic.model.Stay;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class StayValidator implements Validator {
	
	private static final String REQUIRED = "required";

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return Stay.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Stay stay = (Stay) target;
		
		if (stay.getRegisterDate() == null) {
			errors.rejectValue("registerDate", REQUIRED, REQUIRED);
		}

		if (stay.getReleaseDate() == null) {
			errors.rejectValue("releaseDate", REQUIRED, REQUIRED);
		}

		if(stay.getRegisterDate() != null || stay.getReleaseDate() != null) {

			LocalDate registerDate = stay.getRegisterDate();
			LocalDate releaseDate = stay.getReleaseDate();
			LocalDate now = LocalDate.now();

			if(stay.getRegisterDate() != null && registerDate.isBefore(now)) {
				errors.rejectValue("registerDate", REQUIRED, "Register date must be equal of after now");
			}

			if(stay.getReleaseDate() != null && releaseDate.isBefore(now)) {
				errors.rejectValue("releaseDate", REQUIRED, "Release date must be equal or after now");
			}

			if(stay.getRegisterDate() != null && stay.getReleaseDate() != null && releaseDate.isBefore(registerDate)) {
				errors.rejectValue("releaseDate", REQUIRED, "Release date must be equal or after register date");
			}
		}	

	}

}
