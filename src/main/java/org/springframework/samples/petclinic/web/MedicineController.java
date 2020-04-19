package org.springframework.samples.petclinic.web;




import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.service.MedicineService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedMedicineCodeException;
import org.springframework.samples.petclinic.service.exceptions.PastMedicineDateException;
import org.springframework.samples.petclinic.service.exceptions.WrongMedicineCodeException;
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
			} catch (DuplicatedMedicineCodeException | PastMedicineDateException | IllegalArgumentException | WrongMedicineCodeException ex) {
				 Logger.getLogger(MedicineService.class.getName()).log(Level.SEVERE, null, ex);
				 if(ex.getClass().equals(DuplicatedMedicineCodeException.class)) {
					 result.rejectValue("name", "duplicate", "already exists");
				 }
				 if(ex.getClass().equals(PastMedicineDateException.class)) {
					 result.rejectValue("name", "past", "past date");
				 }
				 if(ex.getClass().equals(WrongMedicineCodeException.class)) {
					 result.rejectValue("name", "pattern", "Must match pattern");
				 }
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



}