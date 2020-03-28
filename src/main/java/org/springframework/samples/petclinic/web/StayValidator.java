package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.repository.StayRepository;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class StayValidator implements Validator {
	
	private static final String REQUIRED = "required";
	
	private PetService petService;

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

		if(stay.getRegisterDate() != null && stay.getReleaseDate() != null) {

			LocalDate registerDate = stay.getRegisterDate();
			LocalDate releaseDate = stay.getReleaseDate();
			LocalDate now = LocalDate.now();

			if(registerDate.isBefore(now)) {
				errors.rejectValue("registerDate", "Register date must be equal of after now", "Register date must be equal of after now");
			}

			if(releaseDate.isBefore(now)) {
				errors.rejectValue("releaseDate", "Release date must be equal or after now", "Release date must be equal or after now");
			}

			if(releaseDate.isBefore(registerDate)) {
				errors.rejectValue("releaseDate", "Release date must be equal or after register date", "Release date must be equal or after register date");
			}
			
//			if(this.petService.existsStayInThatDates(stay)) {
//				errors.rejectValue("releaseDate", "There's already a stay in those dates", "There's already a stay in those dates");
//
//			}
		}	

	}

}
