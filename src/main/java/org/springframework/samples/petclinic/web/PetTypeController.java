package org.springframework.samples.petclinic.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.PetTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pet-type")
public class PetTypeController {
	
	private final PetTypeService petTypeService;

	@Autowired
	public PetTypeController(PetTypeService petTypeService) {
		this.petTypeService = petTypeService;
	}
	
	@GetMapping()
	public String listTypes(ModelMap modelMap) {
		Iterable<PetType> pt = this.petTypeService.findAll();
		modelMap.addAttribute("petTypes", pt);
		return "pet-type/typeList";
	}
	
	@GetMapping("/new")
	public String addType(ModelMap modelMap) {
		modelMap.addAttribute("petType", new PetType());
		return "pet-type/typeForm";
	}
	
	@PostMapping("/save")
	public String savePetType(@Valid PetType petType, BindingResult result, ModelMap modelMap) {
		if(result.hasErrors()) {
			modelMap.addAttribute("petType", petType);
			modelMap.addAttribute("message", "Errors");
			return "pet-type/typeForm";
		} else {
			if(!this.petTypeService.typeAlreadyExists(petType.getName())) {
				modelMap.addAttribute("message", "Pet type already exists");
				return "pet-type/typeForm";
			}
			petTypeService.savePetType(petType);
			modelMap.addAttribute("message", "New pet type successfully added!");
			return listTypes(modelMap);
		}
	}
}
