package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.MedicalTest;
import org.springframework.samples.petclinic.service.MedicalTestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/medical-tests")
public class MedicalTestController {

	private static final String VIEWS_MEDICAL_TEST_CREATE_OR_UPDATE_FORM = "medical-tests/createOrUpdateMedicalTestForm";

	private final MedicalTestService medicalTestService;
	
	@Autowired
	public MedicalTestController(MedicalTestService medicalTestService) {
		this.medicalTestService = medicalTestService;
	}
	
	@GetMapping()
	public String showMedicalTestsList(Map<String, Object> model) {
		Collection<MedicalTest> medicalTests = this.medicalTestService.findMedicalTests();
		model.put("medicalTests", medicalTests);
		return "medical-tests/medicalTestsList";
	}
	
	@GetMapping(value = "/new")
	public String initCreationForm(Map<String, Object> model) {
		MedicalTest medicalTest = new MedicalTest();
		model.put("medicalTest", medicalTest);
		return VIEWS_MEDICAL_TEST_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping(value = "/new")
	public String processCreationFrom(@Valid MedicalTest medicalTest, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.put("medicalTest", medicalTest);
			return VIEWS_MEDICAL_TEST_CREATE_OR_UPDATE_FORM;
		} else {
			this.medicalTestService.saveMedicalTest(medicalTest);
		}
		return "redirect:/medical-tests";
	}
}
