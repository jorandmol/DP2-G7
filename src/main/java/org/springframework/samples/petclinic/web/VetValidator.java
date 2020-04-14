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

import org.springframework.samples.petclinic.model.Vet;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * <code>Validator</code> for <code>Pet</code> forms.
 * <p>
 * We're not using Bean Validation annotations here because it is easier to
 * define such validation rule in Java.
 * </p>
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 */
public class VetValidator implements Validator {

	@Override
	public void validate(Object obj, Errors errors) {

		Vet vet = (Vet) obj;

		String password = vet.getUser().getPassword();

		if (vet.getFirstName().isEmpty() || vet.getFirstName() == null) {
			errors.rejectValue("firstName", "empty", "cannot be empty");
		}

		if (vet.getLastName().isEmpty() || vet.getLastName() == null) {
			errors.rejectValue("lastName", "empty", "cannot be empty");
		}

		if (vet.getTelephone().isEmpty() || vet.getTelephone() == null) {
			errors.rejectValue("telephone", "empty", "cannot be empty");
		}

		if (!vet.getTelephone().matches("^[0-9]{9,9}$")) {
			errors.rejectValue("telephone", "phone.format", "The phone must be made up of 9 numbers");
		}

		if (vet.getAddress().isEmpty() || vet.getAddress() == null) {
			errors.rejectValue("address", "empty", "cannot be empty");
		}

		if (vet.getCity().isEmpty() || vet.getCity() == null) {
			errors.rejectValue("city", "empty", "cannot be empty");
		}

		if (password.isEmpty() || password == null) {
			errors.rejectValue("user.password", "empty", "cannot be empty");
		}

		if (!password.matches("^(?=(.*\\d))(?=(.*[A-Za-z]))(?=(.*[\\pP]))([^\\s]){10,}$|^$")) {
			errors.rejectValue("user.password", "password.format",
					"the password must contain at least one digit, one punctuation symbol, one letter and a minimum length of 10 characters");
		}

	}

	/**
	 * This Validator validates *just* Vet instances
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return Vet.class.isAssignableFrom(clazz);
	}

}
