package org.springframework.samples.petclinic.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/owners/{ownerId}/pets/{petId}")
public class TreatmentController {
	
	@Autowired
	private PetService petService;
	
	@GetMapping(value = "/treatments")
	public String showTreatments(@PathVariable final int ownerId, @PathVariable final int petId, final Map<String, Object> model) {
		model.put("treatments", this.petService.findPetById(petId).getTreatments());
		return "treatments/listTreatments";
	}

}
