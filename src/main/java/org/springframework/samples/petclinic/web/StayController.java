package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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


	@ModelAttribute("stay")
	public Stay loadPetWithStay(@PathVariable("petId") int petId) {
		Pet pet = this.petService.findPetById(petId);
		Stay stay = new Stay();
		pet.addStay(stay);
		return stay;
	}

	@GetMapping(value = "/owners/*/pets/{petId}/stances/new")
	public String initNewStayForm(@PathVariable("petId") int petId, Map<String, Object> model) {
		return "pets/createOrUpdateStayForm";
	}

	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/stances/new")
	public String processNewStayForm(@Valid Stay stay, BindingResult result) {
		if (result.hasErrors()) {
			return "pets/createOrUpdateStayForm";
		}
		else {
			this.petService.saveStay(stay);
			return "redirect:/owners/{ownerId}";
		}
	}

	@GetMapping(value = "/owners/*/pets/{petId}/stances")
	public String showStances(@PathVariable int petId, Map<String, Object> model) {
		model.put("stances", this.petService.findPetById(petId).getStances());
		return "stayList";
	}

}
