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

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
public class StayController {

	private final PetService petService;

	@Autowired
	public StayController(PetService petService) {
		this.petService = petService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("stay")
	public void initPetBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new StayValidator());
	}

	/**
	 * Called before each and every @GetMapping or @PostMapping annotated method. 2 goals:
	 * - Make sure we always have fresh data - Since we do not use the session scope, make
	 * sure that Pet object always has an id (Even though id is not part of the form
	 * fields)
	 * @param petId
	 * @return Pet
	 */
	
	@GetMapping(value = "/owners/*/pets/{petId}/stays")
	public String initStayList(@PathVariable("petId") int petId, Map<String, Object> model) {
		model.put("stays", this.petService.findPetById(petId).getStays());
		model.put("pet", this.petService.findPetById(petId));
		return "pets/staysList";
	}
	
	@ModelAttribute("stay")
	public Stay loadPetWithStay(@PathVariable("petId") int petId) {
		Pet pet = this.petService.findPetById(petId);
		Stay stay = new Stay();
		pet.addStay(stay);
		return stay;
	}
	
	// Spring MVC calls method loadPetWithStay(...) before initNewStayForm is called
	@GetMapping(value = "/owners/{ownerId}/pets/{petId}/stays/new")
	public String initNewStayForm(@PathVariable("petId") int petId, Map<String, Object> model) {
		return "pets/createOrUpdateStayForm";
	}

	// Spring MVC calls method loadPetWithStay(...) before processNewStayForm is called
	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/stays/new")
	public String processNewStayForm(@Valid Stay stay, BindingResult result) {
		if (result.hasErrors()) {
			return "pets/createOrUpdateStayForm";
		}
		else {
			this.petService.saveStay(stay);
			return "redirect:/owners/{ownerId}/pets/{petId}/stays";
		}
	}
	
	@GetMapping(value = "/owners/{ownerId}/pets/{petId}/stays/{stayId}/delete")
	public String processDeleteForm(@PathVariable("stayId") int stayId, @PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId) {
		Pet pet = petService.findPetById(petId);
		Stay stay = petService.findStayById(stayId);
		pet.deleteStay(stay);
		this.petService.deleteStay(stay);
		return "redirect:/owners/{ownerId}/pets/{petId}/stays";
	}

}
