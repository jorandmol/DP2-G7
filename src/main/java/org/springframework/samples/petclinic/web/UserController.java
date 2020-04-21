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

import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
public class UserController {

	private static final String VIEWS_OWNER_CREATE_FORM = "users/createOwnerForm";

	private final OwnerService ownerService;
	
	private final VetService vetService;

	@Autowired
	public UserController(OwnerService ownerService, VetService vetService) {
		this.ownerService = ownerService;
		this.vetService = vetService;
	}
	
	@InitBinder("owner")
	public void initPetBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new OwnerValidator());
	}
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/users/new")
	public String initCreationForm(Map<String, Object> model) {
		Owner owner = new Owner();
		model.put("owner", owner);
		return VIEWS_OWNER_CREATE_FORM;
	}

	@PostMapping(value = "/users/new")
	public String processCreationForm(@Valid Owner owner, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("owner", owner);
			return VIEWS_OWNER_CREATE_FORM;
		}
		else {
			//creating owner, user, and authority
			try {
				this.ownerService.saveOwner(owner);
			} catch (Exception ex) {

				if (owner.getUser().getUsername().isEmpty())
					result.rejectValue("user.username", "empty");

				if (ex.getClass().equals(DataIntegrityViolationException.class))
					result.rejectValue("user.username", "duplicate");

				return VIEWS_OWNER_CREATE_FORM;
			}
			
			return "redirect:/";
		}
	}

	@GetMapping(value= { "/users/profile" })
	public String findUser() {
		
		String authority = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
				.stream().collect(Collectors.toList()).get(0).toString();
		System.out.println(authority);
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		String url = "";
		
		if(authority.equals("owner")) {
			Owner owner= this.ownerService.findOwnerByUsername(username);
			url = "redirect:/owners/" + owner.getId();
		}else {
			if(authority.equals("veterinarian")) {
				Vet vet= this.vetService.findVetByUsername(username);
				url = "redirect:/vets/" + vet.getId();
			}
		}
		return url;
	}

		
}
