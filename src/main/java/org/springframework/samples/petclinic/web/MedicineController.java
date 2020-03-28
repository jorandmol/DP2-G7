package org.springframework.samples.petclinic.web;




import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.MedicineService;

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
			List<String> messages = new ArrayList<>();
			Boolean invalidCode = this.medicineService.codeAlreadyExists(medicine.getCode());
			Boolean invalidDate = this.medicineService.pastDate(medicine.getExpirationDate());
			if(invalidCode) {
				messages.add("Code already in use");
			}
			if(invalidDate){
				messages.add("Date is before today");
			}
			if(invalidCode || invalidDate) {
				modelMap.addAttribute("messages", messages);
				return VIEWS_MEDICINE_CREATE_OR_UPDATE_FORM;
			} else {
			this.medicineService.saveMedicine(medicine);
			return "redirect:/medicines/" + medicine.getId();	
			}
		}
	}
	
	@GetMapping("/{medicineId}")
	public ModelAndView showMedicine(@PathVariable("medicineId") int medicineId) {
		
		ModelAndView mav = new ModelAndView("medicines/medicineDetails");
		mav.addObject(this.medicineService.findMedicineById(medicineId));
		return mav;
	}



}
