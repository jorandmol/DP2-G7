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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
	private static final PetRegistrationStatus accepted = PetRegistrationStatus.ACCEPTED;
	private static final PetRegistrationStatus pending = PetRegistrationStatus.PENDING;
	private static final PetRegistrationStatus rejected = PetRegistrationStatus.REJECTED;

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
		if (securityAccessPetRequestAndProfile(ownerId, false)) {
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
		if (securityAccessPetRequestAndProfile(ownerId, false)) {

			Owner owner = this.ownerService.findOwnerById(ownerId);
			model.addAttribute("owner", owner);
			if (result.hasErrors()) {
				model.put("pet", pet);
				return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
			} else {
				try {
					pet.setStatus(pending);
					pet.setJustification("");
					pet.setActive(true);
					owner.addPet(pet);
					this.petService.savePet(pet);
				} catch (DuplicatedPetNameException ex) {
					result.rejectValue("name", "duplicate", "already exists");
					return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
				}
				return "redirect:/owner/requests";
			}

		} else {
			return REDIRECT_TO_OUPS;
		}
	}

	@GetMapping(value = "/owners/{ownerId}/pets/{petId}/edit")
	public String initUpdateForm(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId,
			ModelMap model) {
		boolean edit = true;
		Owner owner = this.ownerService.findOwnerById(ownerId);
		Pet petToUpdate = this.petService.findPetById(petId);
		Boolean isHisPetAcceptedAndAcctive = petToUpdate.getOwner().getId().equals(owner.getId()) && petToUpdate.isActive()
				&& petToUpdate.getStatus().equals(accepted);
		if (securityAccessPetRequestAndProfile(ownerId, edit) && isHisPetAcceptedAndAcctive) {
			Pet pet = this.petService.findPetById(petId);
			model.addAttribute("pet", pet);
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
		String authority = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.collect(Collectors.toList()).get(0).toString();

		Owner owner = this.ownerService.findOwnerById(ownerId);
		Pet petToUpdate = this.petService.findPetById(petId);

		boolean edit = true;
		Boolean isHisPetAcceptedAndAcctive = petToUpdate.getOwner().getId().equals(owner.getId()) && petToUpdate.isActive()
				&& petToUpdate.getStatus().equals(accepted);
		if (securityAccessPetRequestAndProfile(ownerId, edit) && isHisPetAcceptedAndAcctive) {

			model.addAttribute("owner", owner);
			model.addAttribute("edit", edit);

			if (result.hasErrors()) {
				model.put("pet", pet);
				return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
			} else {
				BeanUtils.copyProperties(pet, petToUpdate, "id", "owner", "stays", "appointments", "visits", "status", "justification",
						"active");
				try {
					this.petService.savePet(petToUpdate);
				} catch (DuplicatedPetNameException ex) {
					result.rejectValue("name", "duplicate", "already exists");
					return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
				}

				if (authority.equals("owner")) {
					return "redirect:/owner/pets";
				} else {
					return "redirect:/owners/" + owner.getId();
				}
			}
		} else {
			return REDIRECT_TO_OUPS;
		}
	}

	@GetMapping(value = "/owners/{ownerId}/pet/{petId}")
	public String showAndUpdatePetRequest(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId,
			ModelMap model) {
		
		Pet pet = this.petService.findPetById(petId);
		if (securityAccessPetRequestAndProfile(ownerId, false) || isAdmin() && pet.getStatus().equals(pending)) {
			if (pet.getStatus().equals(rejected)) {
				model.addAttribute("rejected", true);
			}
			List<PetRegistrationStatus> status = new ArrayList<>();
			status.add(rejected);
			status.add(accepted);
			model.addAttribute("status", status);
			model.addAttribute("pet", pet);
			model.addAttribute("petRequest", pet);
			return "pets/updatePetRequest";
		} else {
			return REDIRECT_TO_OUPS;
		}
	}

	@PostMapping("/owners/{ownerId}/pet/{petId}")
	public String AnswerPetRequest(@Valid Pet pet, BindingResult result, @PathVariable("ownerId") int ownerId,
			@PathVariable("petId") int petId, ModelMap model) {

		Pet petToUpdate = this.petService.findPetById(petId);
		Owner owner = this.ownerService.findOwnerById(ownerId);
		if (isAdmin() && petToUpdate.getStatus().equals(pending) && petToUpdate.getOwner().getId().equals(owner.getId())) {

			List<PetRegistrationStatus> status = new ArrayList<>();
			status.add(rejected);
			status.add(accepted);
			model.addAttribute("status", status);

			model.addAttribute("petRequest", petToUpdate);
			if (result.hasErrors()) {
				model.put("pet", pet);
				return "pets/updatePetRequest";
			} else {
				BeanUtils.copyProperties(pet, petToUpdate, "id", "owner", "visits", "name", "birthDate", "type",
						"active", "stays", "appointments");
				try {
					this.petService.savePet(petToUpdate);
				} catch (DuplicatedPetNameException ex) {
					result.rejectValue("name", "duplicate", "already exists");
					return "pets/updatePetRequest";
				}
				return "redirect:/requests";
			}

		} else {
			return REDIRECT_TO_OUPS;
		}
	}

	@GetMapping(value = { "/requests" })
	public String showPetRequests(ModelMap model) {

		List<Pet> petsRequests = this.petService.findPetsRequests(pending);
		model.addAttribute("pets", petsRequests);
		return "pets/requests";
	}

	@GetMapping(value = "/owner/requests")
	public String showMyPetRequests(ModelMap model) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Owner owner = this.ownerService.findOwnerByUsername(username);
		List<Pet> myPetsRequests = this.petService.findMyPetsRequests(pending, rejected, owner.getId());
		model.addAttribute("pets", myPetsRequests);
		return "pets/myRequests";
	}

	@GetMapping(value = "/owner/pets")
	public String showMyPetsActive(ModelMap model) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Owner owner = this.ownerService.findOwnerByUsername(username);
		List<Pet> myPets = this.petService.findMyPetsAcceptedByActive(accepted, true, owner.getId());
		model.addAttribute("disabled", this.petService.countMyPetsAcceptedByActive(accepted, false, owner.getId()) != 0);
		model.addAttribute("owner", owner);
		model.addAttribute("pets", myPets);
		return "pets/myPetsActive";
	}

	@GetMapping(value = "/owners/{ownerId}/pets/disabled")
	public String showMyPetsDisabled(@PathVariable("ownerId") int ownerId, ModelMap model) {
		
		Boolean havePetDisabled = this.petService.countMyPetsAcceptedByActive(accepted, false, ownerId) !=0;
		
		if (securityAccessPetRequestAndProfile(ownerId, true) && havePetDisabled) {

			Owner owner = this.ownerService.findOwnerById(ownerId);
			List<Pet> myPets = this.petService.findMyPetsAcceptedByActive(accepted, false, ownerId);
			model.put("owner", owner);
			model.put("pets", myPets);
			return "pets/myPetsDisabled";

		} else {
			return REDIRECT_TO_OUPS;
		}
	}
	
	@GetMapping(value = "/owners/{ownerId}/pets/{petId}/disable")
	public String processDisablePet(@PathVariable("ownerId") int ownerId, @PathVariable("ownerId") int petId, ModelMap model) throws DataAccessException, DuplicatedPetNameException {
		
		Boolean petIsActive = this.petService.findPetById(petId).isActive();
		Pet updatePet = this.petService.findPetById(petId);
		boolean hasStaysOrAppointments= this.petService.petHasStaysOrAppointmentsActive(petId);
		if (securityAccessPetRequestAndProfile(ownerId, true) && petIsActive) {
			if(hasStaysOrAppointments) {
				model.addAttribute("errorDisabled", "This pet has current confirmed stays or appointments");
				model.addAttribute("petIdWithError", petId);
			} else {
				updatePet.setActive(false);
				this.petService.savePet(updatePet);
			}
			return showMyPetsActive(model);
		} else {
			return REDIRECT_TO_OUPS;
		}
	}

	private Boolean isAdmin() {
		String authority = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.collect(Collectors.toList()).get(0).toString();
		return authority.equals("admin");
	}

	private boolean securityAccessPetRequestAndProfile(int ownerId, boolean edit) {
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
