package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Treatment;
import org.springframework.samples.petclinic.service.MedicineService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.TreatmentService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TreatmentController {
	
	@Autowired
	private TreatmentService treatmentService;
	
	@Autowired
	private MedicineService medicineService;
	
	@Autowired
	private OwnerService ownerService;
	
	@Autowired
	private PetService petService;
	
	@GetMapping(value = "/owners/{ownerId}/pets/{petId}/treatments")
	public String showTreatments(@PathVariable final int ownerId, @PathVariable final int petId, final Map<String, Object> model) {
		if (securityAccessRequestAppointment(ownerId, petId)) {
			List<Treatment> treatments = this.treatmentService.findTreatmentsByPet(petId);
			List<Treatment> treatmentsDone = this.treatmentService.findTreatmentsDoneByPet(petId);
			
			model.put("treatments", treatments);
			model.put("treatmentsDone", treatmentsDone);
			
			return "treatments/listTreatments";			
		} else {
			return "redirect:/oups";
		}
	}
	
	@GetMapping(value ="/owners/{ownerId}/pets/{petId}/treatments/new")
	public String initNewTreatmentForm(@PathVariable("ownerId") final int ownerId, @PathVariable("petId") final int petId, ModelMap model) {
		if (securityAccessRequestAppointment(ownerId, petId)) {
			Treatment treatment = new Treatment();
			Collection<Medicine> medicines = this.medicineService.findAll();
			treatment.setPet(this.petService.findPetById(petId));
						
			model.addAttribute("treatment", treatment);
			model.addAttribute("medicines", medicines);
			return "treatments/createOrUpdateTreatmentForm";
		} else {
			return "redirect:/oups";
		}
	}
	
	private boolean securityAccessRequestAppointment(int ownerId, int petId) {
		String authority = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.collect(Collectors.toList()).get(0).toString();
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		Owner owner = this.ownerService.findOwnerById(ownerId);
		Pet pet = this.petService.findPetById(petId);
		
		boolean isHisPet = false;
		String ownerUsername = null;
		if (owner != null) {
			isHisPet = owner.getPets().contains(pet);
			ownerUsername = owner.getUser().getUsername();
		}

		return authority.equals("owner") && username.equals(ownerUsername) && isHisPet;
	}

}
