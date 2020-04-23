package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Treatment;
import org.springframework.samples.petclinic.model.TreatmentHistory;
import org.springframework.samples.petclinic.model.TreatmentHistoryDTO;
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
    private static final String REDIRECT_TO_TREATMENT_SHOW = "redirect:/vets/pets/{petId}/treatments/{treatmentId}";
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
	public String showTreatments(@PathVariable("ownerId") final int ownerId, @PathVariable("petId") final int petId, final ModelMap model) {
	    if (securityAccessRequestTreatment(ownerId, petId)) {
            return getViewsTreatmentList(petId, model);
		} else {
			return REDIRECT_TO_OUPS;
		}
	}

	@GetMapping(value = "/vets/pets/{petId}/treatments")
    public String showTreatments(@PathVariable("petId") final int petId, final ModelMap model) {
	    if (getAuthority().equals("veterinarian")) {
	        model.put("isVet", true);
	        model.put("petId", petId);
	        return getViewsTreatmentList(petId, model);
        } else {
	        return REDIRECT_TO_OUPS;
        }
    }

	@GetMapping(value = "/vets/pets/{petId}/treatments/new")
	public String initNewTreatmentForm(@PathVariable("petId") final int petId, final ModelMap model) {
        Treatment treatment = new Treatment();
        treatment.setPet(this.petService.findPetById(petId));
        model.addAttribute("treatment", treatment);
        return VIEWS_TREATMENT_FORM;
	}

	@PostMapping(value = "/vets/pets/{petId}/treatments/new")
    public String processNewTreatmentForm(@Valid final Treatment treatment, final BindingResult result, @PathVariable("petId") final int petId, final ModelMap modelMap) {
        if (result.hasErrors()) {
            return VIEWS_TREATMENT_FORM;
        } else {
            Pet pet = this.petService.findPetById(petId);
            treatment.setPet(pet);
            this.treatmentService.saveTreatment(treatment);
            return "redirect:/vets/pets/{petId}/treatments";
        }
    }
	
	@GetMapping(value = "/vets/pets/{petId}/treatments/{treatmentId}")
	public String getTreatmentShow(@PathVariable("petId") final int petId, @PathVariable("treatmentId") final int treatmentId, final ModelMap modelMap) {
		if (isAccesibleTreatment(petId, treatmentId)) {
			Treatment treatment = this.treatmentService.findById(treatmentId);
			List<TreatmentHistory> treatmentHistory = this.treatmentService.findHistoryByTreatment(treatmentId);
			modelMap.put("petId", petId);
			modelMap.put("treatment", treatment);
			modelMap.put("treatmentHistory", getFinalTreatmentHistory(treatmentHistory));
			if (treatment.getTimeLimit().isAfter(LocalDate.now())) {
				modelMap.put("isEditableTreatment", true);
			}
			return "treatments/showTreatment";
		} else {
			return REDIRECT_TO_OUPS;
		}
	}

    @GetMapping(value = "/vets/pets/{petId}/treatments/{treatmentId}/edit")
    public String initTreatmentEditForm(@PathVariable("petId") final int petId, @PathVariable("treatmentId") final int treatmentId, final ModelMap modelMap) {
        if (isEditableTreatment(petId, treatmentId)) {
        	Treatment treatment = this.treatmentService.findById(treatmentId);
        	modelMap.put("treatment", treatment);
        	modelMap.put("edit", true);
        	Collection<Medicine> noTreatmentMedicines = new ArrayList<Medicine>(loadMedicines());
        	noTreatmentMedicines.removeAll(treatment.getMedicines());
        	modelMap.put("treatmentMedicines", treatment.getMedicines());
        	modelMap.put("otherMedicines", noTreatmentMedicines);
        	return VIEWS_TREATMENT_FORM;        	
        } else {
        	return REDIRECT_TO_OUPS;
        }
    }

    @PostMapping(value = "/vets/pets/{petId}/treatments/{treatmentId}/edit")
    public String processTreatmentEditForm(@Valid final Treatment treatment, final BindingResult result, @PathVariable("petId") final int petId,
            @PathVariable("treatmentId") final int treatmentId, final ModelMap modelMap) {
    	if (isEditableTreatment(petId, treatmentId)) {
	    	if (result.hasErrors()) {
	            return VIEWS_TREATMENT_FORM;
	        } else if (hasChanges(treatment)) {
	            this.treatmentService.editTreatment(treatment);
	        }
	        return REDIRECT_TO_TREATMENT_SHOW;
    	} else {
        	return REDIRECT_TO_OUPS;
        }
    }
    
    @GetMapping(value = "/vets/pets/{petId}/treatments/{treatmentId}/history/{treatmentHistoryId}/delete")
	public String deleteTreatmentHistoryRegister(@PathVariable("treatmentHistoryId") final int treatmentHistoryId, @PathVariable("treatmentId") final int treatmentId, @PathVariable("petId") final int petId) {
		if (isAccesibleTreatment(petId, treatmentId)) {
			TreatmentHistory register = this.treatmentService.findTreatmentHistoryById(treatmentHistoryId);
			this.treatmentService.deleteTreatmentHistoryRegister(register);
			return REDIRECT_TO_TREATMENT_SHOW;
		} else {
			return REDIRECT_TO_OUPS;
		}
	}

    private String getViewsTreatmentList(final int petId, final ModelMap model) {
    	List<Treatment> treatments = this.treatmentService.findCurrentTreatmentsByPet(petId);
    	List<Treatment> treatmentsDone = this.treatmentService.findExpiredTreatmentsByPet(petId);
    	
    	model.put("treatments", treatments);
    	model.put("treatmentsDone", treatmentsDone);
    	
    	return VIEWS_TREATMENT_LIST;
    }
    
	private boolean securityAccessRequestTreatment(int ownerId, int petId) {
		String authority = getAuthority();
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
	
	private boolean isEditableTreatment(int petId, int treatmentId) {
		Treatment treatment = this.treatmentService.findById(treatmentId);
		boolean isCurrantTreatment = treatment.getTimeLimit().isAfter(LocalDate.now());
		boolean isAccessibleTreatment = isAccesibleTreatment(petId, treatmentId);
		boolean res = false;
		if (isCurrantTreatment && isAccessibleTreatment) {
			res = true;
		}
		return res;	
	}
	
	private boolean isAccesibleTreatment(int petId, int treatmentId) {
		boolean res = false;
		Treatment treatment = this.treatmentService.findById(treatmentId);
		if (treatment != null) {
			res = treatment.getPet().getId() == petId;
		}
		return res;
	}
	
	private boolean hasChanges(Treatment treatment) {
		Treatment original = this.treatmentService.findById(treatment.getId());
		return !(treatment.getName().equals(original.getName()) &&
				treatment.getDescription().equals(original.getDescription()) &&
				treatment.getTimeLimit().equals(original.getTimeLimit()) &&
				treatment.getMedicines().equals(original.getMedicines()));
	}

	private String getAuthority() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
            .collect(Collectors.toList()).get(0).getAuthority();
    }

    private List<TreatmentHistoryDTO> getFinalTreatmentHistory(List<TreatmentHistory> treatmentHistory) {
    	List<TreatmentHistoryDTO> res = new ArrayList<>();
    	for (int i = 0; i < treatmentHistory.size(); i++) {
    		List<String> medicines = treatmentHistory.get(i).getMedicineList();
    		TreatmentHistoryDTO treatment = new TreatmentHistoryDTO(treatmentHistory.get(i), medicines);
    		res.add(treatment);    		
    	}    	
    	return res;
    }
}
