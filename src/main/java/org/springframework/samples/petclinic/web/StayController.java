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

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.exceptions.DateNotAllowed;
import org.springframework.samples.petclinic.service.exceptions.MaximumStaysReached;
import org.springframework.samples.petclinic.service.exceptions.StayAlreadyConfirmed;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
public class StayController {

	private final PetService petService;
	
	private final OwnerService ownerService;

	@Autowired
	public StayController(PetService petService, OwnerService ownerService) {
		this.petService = petService;
		this.ownerService = ownerService;
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
	 * Called before each and every @GetMapping or @PostMapping annotated method. 2
	 * goals: - Make sure we always have fresh data - Since we do not use the
	 * session scope, make sure that Pet object always has an id (Even though id is
	 * not part of the form fields)
	 * 
	 * @param petId
	 * @return Pet
	 */

	
	private Boolean securityAccessRequest(Integer ownerId, Integer petId) {
		Boolean res = false;
		String authority = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.collect(Collectors.toList()).get(0).toString();
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		Owner owner = this.ownerService.findOwnerById(ownerId);
		Pet pet = this.petService.findPetById(petId);
		Boolean isHisPet = owner.getPets().contains(pet);

		if ((authority.equals("owner") && username.equals(owner.getUser().getUsername()) && isHisPet)) {
			res = true;
		}
		return res;
	}
	
	@GetMapping(value = "/owners/{ownerId}/pets/{petId}/stays")
	public String initStayList(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId, ModelMap model) {
		// Esta lista también puede ser accedida por el administrador
		String authority = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.collect(Collectors.toList()).get(0).toString();
		
		if(this.securityAccessRequest(ownerId, petId) || authority.equals("admin")) {
			model.put("stays", this.petService.findStaysByPetId(petId));
			model.put("pet", this.petService.findPetById(petId));
			return "pets/staysList";
		} else {
			return "redirect:/oups";
		}
	}

	@GetMapping(value = "/owners/{ownerId}/pets/{petId}/stays/new")
	public String initNewStayForm(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId, Map<String, Object> model) {
		if(this.securityAccessRequest(ownerId, petId)) {
			Stay stay = new Stay();
			Pet pet = this.petService.findPetById(petId);
			pet.addStay(stay);
			model.put("stay", stay);
			return "pets/createOrUpdateStayForm";
		} else {
			return "redirect:/oups";
		}
	}

	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/stays/new")
	public String processNewStayForm(@Valid Stay stay, BindingResult result, @PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId) {
		if(this.securityAccessRequest(ownerId, petId)) {
			if (result.hasErrors()) {
				return "pets/createOrUpdateStayForm";
			} else {
				try {
					Pet pet = this.petService.findPetById(petId);
					stay.setPet(pet);
					this.petService.saveStay(stay);
				} catch (MaximumStaysReached ex) {
					result.rejectValue("releaseDate", "There exists already a Stay", "There exists already a Stay");
					return "pets/createOrUpdateStayForm";
				}
	
				return "redirect:/owners/{ownerId}/pets/{petId}/stays";
			}
		} else {
			return "redirect:/oups";
		}
	}

	@GetMapping(value = "/owners/{ownerId}/pets/{petId}/stays/{stayId}/delete")
	public ModelAndView processDeleteForm(@PathVariable("stayId") int stayId, @PathVariable("ownerId") int ownerId,
			@PathVariable("petId") int petId, ModelMap model) {
		Pet pet = petService.findPetById(petId);
		Stay stay = petService.findStayById(stayId);
		ModelAndView mav = new ModelAndView("pets/staysList");
		mav.addObject("stays", this.petService.findPetById(petId).getStays());
		mav.addObject("pet", this.petService.findPetById(petId));
		
		Boolean isYourStay = stay.getPet().getOwner().getId().equals(ownerId);
		if(this.securityAccessRequest(ownerId, petId) && isYourStay) {
			try {
				pet.deleteStay(stay);
				this.petService.deleteStay(stay);
			} catch (StayAlreadyConfirmed ex) {
				mav.addObject("errors", "This stay is already confirmed");
			}
			return mav;
		} else {
			return new ModelAndView("exception");
		}
	}
	
	
	@GetMapping(value = "/owners/{ownerId}/pets/{petId}/stays/{stayId}/edit")
	public String initStayEditForm(@PathVariable("stayId") final int stayId, @PathVariable("petId") final int petId, @PathVariable("ownerId") final int ownerId, final ModelMap modelMap) {		
		if (securityAccessRequest(ownerId, petId)) {
			Stay stay = this.petService.findStayById(stayId);
			modelMap.put("stay", stay);
			modelMap.put("edit", true);
			return "pets/createOrUpdateStayForm";			
		} else {
			return "redirect:/oups";
		}
	}

	

	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/stays/{stayId}/edit")
	public String processStayEditForm(@Valid final Stay stay, final BindingResult result, @PathVariable("petId") final int petId, @PathVariable("ownerId") final int ownerId,
			@PathVariable("stayId") final int stayId, final ModelMap modelMap)  {	
		if (securityAccessRequest(ownerId, petId)) {
			modelMap.put("edit", true);
			if (result.hasErrors()) {
				return "pets/createOrUpdateStayForm";
			} else {
				try {
					Stay stayToUpdate = this.petService.findStayById(stayId);
					BeanUtils.copyProperties(stayToUpdate, stay , "registerDate" , "releaseDate","status");
					this.petService.editStay(stay);
				} catch (MaximumStaysReached | DateNotAllowed | StayAlreadyConfirmed e) {
					if(e.getClass().equals(MaximumStaysReached.class)) {
						result.rejectValue("releaseDate", "There exists already a Stay", "There exists already a Stay");
					}else if(e.getClass().equals(DateNotAllowed.class)) {
						result.rejectValue("releaseDate", "Change the dates", "Change the dates");
					}else if(e.getClass().equals(StayAlreadyConfirmed.class)) {
						result.rejectValue("releaseDate", "Stay already confirmed or rejected by admin", "Stay already confirmed or rejected by admin");
					}
					
					return "pets/createOrUpdateStayForm";
				}
			}
			return "redirect:/owners/{ownerId}/pets/{petId}/stays";			
		} else {
			return "redirect:/oups";
		}
	}


}
