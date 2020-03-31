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

import org.springframework.samples.petclinic.model.Banner;
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
public class BannerValidator implements Validator {

	@Override
	public void validate(Object obj, Errors errors) {
		
		Banner banner = (Banner) obj;

		if (banner.getPicture().isEmpty() || banner.getPicture() == null) {
			errors.rejectValue("picture", "empty", "cannot be empty");
		}

		if (banner.getSlogan().isEmpty() || banner.getSlogan() == null) {
			errors.rejectValue("slogan", "empty", "cannot be empty");
		}

		if (banner.getTargetUrl().isEmpty() || banner.getTargetUrl() == null) {
			errors.rejectValue("targetUrl", "empty", "cannot be empty");
		}

		if (banner.getOrganizationName().isEmpty() || banner.getOrganizationName() == null) {
			errors.rejectValue("organizationName", "empty", "cannot be empty");
		}

		if (banner.getEndColabDate()== null) {
			errors.rejectValue("endColabDate", "required", "must be a date");
		}
		
		if (banner.getEndColabDate() != null && !banner.getEndColabDate().isAfter(LocalDate.now())) {
			errors.rejectValue("endColabDate", "end colaboration date must be in future", "end colaboration date must be in future");
		}
	}

	/**
	 * This Validator validates *just* Vet instances
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return Banner.class.isAssignableFrom(clazz);
	}

}
