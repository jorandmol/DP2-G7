/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.MedicalTest;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.AppointmentService;
import org.springframework.samples.petclinic.service.MedicalTestService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
public class VisitController {

	private static final String REDIRECT_TO_OUPS = "redirect:/oups";
	
	private final VisitService visitService;

	private final PetService petService;

	private final MedicalTestService medicalTestService;

	private final AppointmentService appointmentService;
	
	private final OwnerService ownerService;

	@Autowired
	public VisitController(final VisitService visitService, final PetService petService, final MedicalTestService medicalTestService,
			final AppointmentService appointmentService, final OwnerService ownerService) {
		this.visitService = visitService;
		this.petService = petService;
		this.medicalTestService = medicalTestService;
		this.appointmentService = appointmentService;
		this.ownerService = ownerService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@ModelAttribute("tests")
	public Collection<MedicalTest> populateMedicalTests() {
		return this.medicalTestService.findMedicalTests();
	}

	private Boolean securityAccessRequestVisit(Integer petId) {
		Boolean res = false;
		Appointment appointment = this.appointmentService.findAppointmentByDate(petId, LocalDate.now());
		String vetUsername = appointment.getVet().getUser().getUsername();

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Integer numberOfVisits = this.visitService.countVisitsByDate(petId, LocalDate.now());

		if (numberOfVisits == 0 && vetUsername.equals(username)) {
			res = true;
		}
		return res;
	}
	
	private boolean isAdmin() {
		String authority = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.collect(Collectors.toList()).get(0).toString();
		return authority.equals("admin");
	}
	
	private Boolean securityAccessRequestProfile(int ownerId) {
		String authority = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.collect(Collectors.toList()).get(0).toString();
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Owner owner = new Owner();
		
		if (authority.equals("owner"))
			owner = this.ownerService.findOwnerById(ownerId);

		return authority.equals("admin") || authority.equals("owner") && username.equals(owner.getUser().getUsername());
	}

	/**
	 * Called before each and every @GetMapping or @PostMapping annotated method. 2
	 * goals: - Make sure we always have fresh data - Since we do not use the
	 * session scope, make sure that Pet object always has an id (Even though id is
	 * not part of the form fields)
	 * 
	 * @param petId
	 * @return Pet
	 */
	

	// Spring MVC calls method loadPetWithVisit(...) before initNewVisitForm is
	// called
	@GetMapping(value = "/owners/*/pets/{petId}/visits/new")
	public String initNewVisitForm(@PathVariable("petId") final int petId, final Map<String, Object> model) {
		if (securityAccessRequestVisit(petId) || isAdmin()) {
			Pet pet = this.petService.findPetById(petId);
			Visit visit = new Visit();
			model.put("visit", visit);
			pet.addVisit(visit);
			return "pets/createOrUpdateVisitForm";
		} else {
			return REDIRECT_TO_OUPS;
		}
	}

	// Spring MVC calls method loadPetWithVisit(...) before processNewVisitForm is
	// called
	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/visits/new")
	public String processNewVisitForm(@PathVariable("petId") final int petId, @Valid final Visit visit,
			final BindingResult result) {
		int vetId = 0;
		if (isAdmin()) {
			Appointment ap = this.appointmentService.findAppointmentByDate(petId, visit.getDate());
			vetId = ap.getVet().getId();
		}
		if (securityAccessRequestVisit(petId) || isAdmin()) {
			Pet pet = this.petService.findPetById(petId);
			pet.addVisit(visit);
			if (result.hasErrors()) {
				return "pets/createOrUpdateVisitForm";
			} else {
				this.visitService.saveVisit(visit);
				return isAdmin()?"redirect:/appointments/"+vetId:"redirect:/appointments";
			}
		} else {
			return REDIRECT_TO_OUPS;
		}
	}

	@GetMapping(value = "/owners/{ownerId}/pets/{petId}/visits/{visitId}")
	public String showVisit(@PathVariable final int ownerId, @PathVariable final int visitId, final Map<String, Object> model) {
		if (securityAccessRequestProfile(ownerId)) {
			Visit visit = this.visitService.findVisitById(visitId);
			model.put("visit", visit);
			return "visits/visitDetails";
		} else {
			return REDIRECT_TO_OUPS;
		}
	}

}
