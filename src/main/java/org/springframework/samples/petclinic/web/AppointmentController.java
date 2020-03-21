
package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.AppointmentService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AppointmentController {

	@Autowired
	private AppointmentService	appointmentService;

	@Autowired
	private PetService			petService;

	@Autowired
	private VetService 			vetService;


	@ModelAttribute("appointment")
	public Appointment loadPetWithAppointment(@PathVariable("petId") final int petId) {
		Pet pet = this.petService.findPetById(petId);
		Appointment appointment = new Appointment();
		appointment.addPet(pet);
		return appointment;
	}

	@ModelAttribute("vets")
	public Collection<Vet> loadVets() {
		return this.vetService.findVets();
	}

	@GetMapping(value = "/owners/*/pets/{petId}/appointments/new")
	public String initNewAppointmentForm(@PathVariable("petId") final int petId, final Map<String, Object> model) {
		return "pets/createOrUpdateAppointmentForm";
	}

	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/appointments/new")
	public String processNewAppointmentForm(@Valid final Appointment appointment, final BindingResult result) {
		if (result.hasErrors()) {
			return "pets/createOrUpdateAppointmentForm";
		} else {
			this.appointmentService.saveAppointment(appointment);
			return "redirect:/owners/{ownerId}";
		}
	}
}
