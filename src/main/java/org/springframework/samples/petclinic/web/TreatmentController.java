package org.springframework.samples.petclinic.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Treatment;
import org.springframework.samples.petclinic.service.TreatmentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TreatmentController {
	
	
	private TreatmentService treatmentService;
	
	@Autowired
	public TreatmentController(TreatmentService treatmentService) {
		this.treatmentService = treatmentService;
	}
	
	@GetMapping(value = "/owners/{ownerId}/pets/{petId}/treatments")
	public String showTreatments(@PathVariable final int ownerId, @PathVariable final int petId, final Map<String, Object> model) {
		List<Treatment> treatments = this.treatmentService.findTreatmentsByPet(petId);
		List<Treatment> treatmentsDone = this.treatmentService.findTreatmentsDoneByPet(petId);
		
		model.put("treatments", treatments);
		model.put("treatmentsDone", treatmentsDone);
		return "treatments/listTreatments";
	}

}
