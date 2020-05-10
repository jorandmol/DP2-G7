package org.springframework.samples.petclinic.web;




import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.service.MedicineService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedMedicineCodeException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/medicines")
public class MedicineController {
	
	private static final String VIEWS_MEDICINE_CREATE_OR_UPDATE_FORM = "medicines/createOrUpdateMedicineForm";

	private final MedicineService medicineService;
	
	@Autowired
	public MedicineController(MedicineService medicineService) {
		this.medicineService = medicineService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("medicine")
	public void initPetBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new MedicineValidator());
	}
	
	@GetMapping()
	public String listMedicines(ModelMap modelMap) {
		Iterable<Medicine> med = this.medicineService.findAll();
		modelMap.addAttribute("medicines", med);
		return "medicines/medicinesList";
	}
	
	@GetMapping(value = "/new")
	public String initCreationForm(Map<String, Object> model) {
		Medicine medicine = new Medicine();
		model.put("medicine", medicine);
		return VIEWS_MEDICINE_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping(value = "/new")
	public String processCreationForm(@Valid Medicine medicine, BindingResult result, ModelMap modelMap) {
		if (result.hasErrors()) {
			return VIEWS_MEDICINE_CREATE_OR_UPDATE_FORM;
		}
		else {
			try {
				this.medicineService.saveMedicine(medicine);
			} catch (DuplicatedMedicineCodeException ex) {
				result.rejectValue("code", "duplicate", "Already exists");
				return VIEWS_MEDICINE_CREATE_OR_UPDATE_FORM;
			}
			return "redirect:/medicines";	
		}
	}
		
	@GetMapping("/{medicineId}")
	public ModelAndView showMedicine(@PathVariable("medicineId") int medicineId) {
		
		ModelAndView mav = new ModelAndView("medicines/medicineDetails");
		mav.addObject(this.medicineService.findMedicineById(medicineId));
		return mav;
	}

	@GetMapping(value = "/{medicineId}/edit")
	public String initEditForm(@PathVariable("medicineId") int medicineId, Map<String, Object> modelMap) {
		Medicine medicine = this.medicineService.findMedicineById(medicineId);
		modelMap.put("medicine", medicine);
		modelMap.put("edit", true);
		return VIEWS_MEDICINE_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping(value = "/{medicineId}/edit")
	public String processEditForm(@Valid Medicine medicine, BindingResult result, @PathVariable("medicineId") int medicineId, Map<String, Object> modelMap) {
		modelMap.put("edit", true);
		if (result.hasErrors()) {
			return VIEWS_MEDICINE_CREATE_OR_UPDATE_FORM;
		} else {
			try {
				medicine.setId(medicineId);
				this.medicineService.editMedicine(medicine);
			} catch (DuplicatedMedicineCodeException e) {
				result.rejectValue("code", "duplicate", "Already exists");
				return VIEWS_MEDICINE_CREATE_OR_UPDATE_FORM;
			}
		}
		return "redirect:/medicines";
	}

}
