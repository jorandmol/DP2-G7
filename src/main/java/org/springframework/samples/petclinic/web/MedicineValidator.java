/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.jboss.logging.Logger;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * <code>Validator</code> for <code>Medicine</code> forms.
 * <p>
 * We're not using Bean Validation annotations here because it is easier to define such
 * validation rule in Java.
 * </p>
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 */
public class MedicineValidator implements Validator {

	private static final String REQUIRED = "required";
	
	@Override
	public void validate(Object obj, Errors errors) {
		Medicine medicine = (Medicine) obj;
		String code = medicine.getCode();
		String name = medicine.getName();
		String description = medicine.getDescription();
			// expirationDate validation
		if (medicine.getExpirationDate() == null) {
			errors.rejectValue("expirationDate", REQUIRED,"Must be a date");
		}
		if (medicine.getExpirationDate() != null && medicine.getExpirationDate().isBefore(LocalDate.now())) {
			errors.rejectValue("expirationDate", "Expiration date must be in future", "Expiration date must be in future");
		}
		if (!StringUtils.hasLength(code)) {
			errors.rejectValue("code", REQUIRED, "Must not be empty");
		}
		if (!code.matches("^[A-Z]{3}\\-\\d{3,9}$")) {
			errors.rejectValue("code", "Must match the pattern ABC-123(456)", "Must match the pattern ABC-123(456)");
		}
		// name validation
		if (!StringUtils.hasLength(name)) {
			errors.rejectValue("name", REQUIRED,"Must not be empty");
		}
		// description validation
		if (!StringUtils.hasLength(description)) {
			errors.rejectValue("description", REQUIRED,"Must not be empty");
		}
	}

	/**
	 * This Validator validates *just* Medicine instances
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return Medicine.class.isAssignableFrom(clazz);
	}
}
