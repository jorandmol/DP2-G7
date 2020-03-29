package org.springframework.samples.petclinic.web;

import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.PetTypeService;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PetTypeValidator implements Validator {
	
	private static final String REQUIRED = "required";
	
	private PetTypeService petTypeService;

	@Override
	public void validate(Object obj, Errors errors) {
		PetType petType = (PetType) obj;
		String name = petType.getName();
		// name validation
		if (!StringUtils.hasLength(name) || name.length()>50 || name.length()<3) {
			errors.rejectValue("name", REQUIRED+" and between 3 and 50 characters", REQUIRED+" and between 3 and 50 character");
		}

		// type validation
		if (!this.petTypeService.typeNameDontExists(name)) {
			errors.rejectValue("name", "Pet type already exists","Pet type already exists" );
		}

	
	}

	/**
	 * This Validator validates *just* Pet instances
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return PetType.class.isAssignableFrom(clazz);
	}


}
