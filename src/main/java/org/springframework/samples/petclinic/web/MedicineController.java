package org.springframework.samples.petclinic.web;




import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.samples.petclinic.model.Medicine;


import org.springframework.samples.petclinic.service.MedicineService;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
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

	
	@GetMapping(value = "/medicines/new")
	public String initCreationForm(Map<String, Object> model) {
		Medicine medicine = new Medicine();
		model.put("medicine", medicine);
		return VIEWS_MEDICINE_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping(value = "/medicines/new")
	public String processCreationForm(@Valid Medicine medicine, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_MEDICINE_CREATE_OR_UPDATE_FORM;
		}
		else {
			this.medicineService.saveMedicine(medicine);
			
			return "redirect:/medicines/" + medicine.getId();
		}
	}
	
	@GetMapping("/medicines/{medicineId}")
	public ModelAndView showMedicine(@PathVariable("medicineId") int medicineId) {
		
		ModelAndView mav = new ModelAndView("medicines/medicineDetails");
		mav.addObject(this.medicineService.findMedicineById(medicineId));
		return mav;
	}



}
