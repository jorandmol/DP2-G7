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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class TreatmentController {

    private static final String VIEWS_TREATMENT_LIST = "treatments/listTreatments";
    private static final String VIEWS_TREATMENT_FORM = "treatments/createOrUpdateTreatmentForm";
    private static final String REDIRECT_TO_OUPS = "redirect:/oups";

	@Autowired
	private TreatmentService treatmentService;

	@Autowired
	private MedicineService medicineService;

	@Autowired
	private OwnerService ownerService;

	@Autowired
	private PetService petService;
	
	@ModelAttribute("medicines")
	public Collection<Medicine> loadMedicines() {
		return this.medicineService.findAll();
	}

	@GetMapping(value = "/owners/{ownerId}/pets/{petId}/treatments")
	public String showTreatments(@PathVariable("ownerId") final int ownerId, @PathVariable("petId") final int petId, final Map<String, Object> model) {
		if (securityAccessRequestAppointment(ownerId, petId)) {
			List<Treatment> treatments = this.treatmentService.findCurrentTreatmentsByPet(petId);
			List<Treatment> treatmentsDone = this.treatmentService.findExpiredTreatmentsByPet(petId);

			model.put("treatments", treatments);
			model.put("treatmentsDone", treatmentsDone);

			return VIEWS_TREATMENT_LIST;
		} else {
			return REDIRECT_TO_OUPS;
		}
	}

	@GetMapping(value = "/owners/{ownerId}/pets/{petId}/treatments/new")
	public String initNewTreatmentForm(@PathVariable("ownerId") final int ownerId, @PathVariable("petId") final int petId, ModelMap model) {
		if (securityAccessRequestAppointment(ownerId, petId)) {
			Treatment treatment = new Treatment();
			treatment.setPet(this.petService.findPetById(petId));
			model.addAttribute("treatment", treatment);
			return VIEWS_TREATMENT_FORM;
		} else {
			return REDIRECT_TO_OUPS;
		}
	}

	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/treatments/new")
    public String processNewTreatmentForm(@Valid final Treatment treatment, final BindingResult result, @PathVariable("ownerId") final int ownerId, @PathVariable("petId") final int petId, final ModelMap modelMap) {
        if (securityAccessRequestAppointment(ownerId, petId)) {
            if (result.hasErrors()) {
                return VIEWS_TREATMENT_FORM;
            } else {
                Pet pet = this.petService.findPetById(petId);
                treatment.setPet(pet);
                this.treatmentService.saveTreatment(treatment);
                return "redirect:/owners/{ownerId}/pets/{petId}/treatments";
            }
        } else {
            return REDIRECT_TO_OUPS;
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
