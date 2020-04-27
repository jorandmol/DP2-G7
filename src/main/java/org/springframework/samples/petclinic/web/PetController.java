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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetRegistrationStatus;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class PetController {

	private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";
	private static final String REDIRECT_TO_OUPS = "redirect:/oups";

	private final PetService petService;
	private final OwnerService ownerService;

	@Autowired
	public PetController(PetService petService, OwnerService ownerService) {
		this.petService = petService;
		this.ownerService = ownerService;
	}

	@ModelAttribute("types")
	public Collection<PetType> populatePetTypes() {
		return this.petService.findPetTypes();
	}

	@InitBinder("owner")
	public void initOwnerBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("pet")
	public void initPetBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new PetValidator());
	}

	@GetMapping(value = "/owners/{ownerId}/pets/new")
	public String initCreationForm(@PathVariable("ownerId") int ownerId, ModelMap model) {
		if (securityAccessRequestProfile(ownerId, false)) {
			Pet pet = new Pet();
			Owner owner = this.ownerService.findOwnerById(ownerId);
			model.addAttribute("owner", owner);
			model.put("pet", pet);
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		} else {
			return REDIRECT_TO_OUPS;
		}
	}

	@PostMapping(value = "/owners/{ownerId}/pets/new")
	public String processCreationForm(@PathVariable("ownerId") int ownerId, @Valid Pet pet, BindingResult result,
			ModelMap model) {
		if (securityAccessRequestProfile(ownerId, false)) {

			Owner owner = this.ownerService.findOwnerById(ownerId);
			model.addAttribute("owner", owner);
			if (result.hasErrors()) {
				model.put("pet", pet);
				return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
			} else {
				try {
					pet.setStatus(PetRegistrationStatus.PENDING);
					pet.setJustification("");
					pet.setActive(true);
					owner.addPet(pet);
					this.petService.savePet(pet);
				} catch (DuplicatedPetNameException ex) {
					result.rejectValue("name", "duplicate", "already exists");
					return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
				}
				return "redirect:/owners/" + owner.getId();
			}

		} else {
			return REDIRECT_TO_OUPS;
		}
	}

	@GetMapping(value = "/owners/{ownerId}/pets/{petId}/edit")
	public String initUpdateForm(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId,
			ModelMap model) {
		boolean edit = true;
		if (securityAccessRequestProfile(ownerId, edit)) {
			Pet pet = this.petService.findPetById(petId);
			model.put("pet", pet);
			model.addAttribute("owner", pet.getOwner());
			model.addAttribute("edit", edit);
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;

		} else {
			return REDIRECT_TO_OUPS;
		}
	}

	/**
	 *
	 * @param pet
	 * @param result
	 * @param petId
	 * @param model
	 * @param owner
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/edit")
	public String processUpdateForm(@PathVariable("ownerId") int ownerId, @Valid Pet pet, BindingResult result,
			@PathVariable("petId") int petId, ModelMap model) {

		boolean edit = true;
		if (securityAccessRequestProfile(ownerId, edit)) {

			Owner owner = this.ownerService.findOwnerById(ownerId);
			model.addAttribute("owner", owner);
			model.addAttribute("edit", edit);
			if (result.hasErrors()) {
				model.put("pet", pet);
				return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
			} else {
				Pet petToUpdate = this.petService.findPetById(petId);
				BeanUtils.copyProperties(pet, petToUpdate, "id", "owner", "visits");
				try {
					this.petService.savePet(petToUpdate);
				} catch (DuplicatedPetNameException ex) {
					result.rejectValue("name", "duplicate", "already exists");
					return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
				}
				return "redirect:/owners/" + owner.getId();
			}
		} else {
			return REDIRECT_TO_OUPS;
		}
	}

	@GetMapping(value = { "/requests" })
	public String showPetRequests(Map<String, Object> model) {
		List<Pet> petsRequests = this.petService.findPetsRequests(PetRegistrationStatus.PENDING);
		model.put("pets", petsRequests);
		return "pets/requests";
	}

	@GetMapping(value = "/myRequests")
	public String showMyPetRequests(Map<String, Object> model) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Owner owner = this.ownerService.findOwnerByUsername(username);
		List<Pet> myPetsRequests = this.petService.findPetsRequests(PetRegistrationStatus.PENDING, owner.getId());
		model.put("pets", myPetsRequests);
		return "pets/requests";
	}

	private boolean securityAccessRequestProfile(int ownerId, boolean edit) {
		String authority = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.collect(Collectors.toList()).get(0).toString();
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		Owner owner = new Owner();
		if (authority.equals("owner")) {
			owner = this.ownerService.findOwnerById(ownerId);
		}

		return authority.equals("admin") && edit
				|| authority.equals("owner") && username.equals(owner.getUser().getUsername());
	}

}
