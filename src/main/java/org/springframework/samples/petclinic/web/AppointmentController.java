
package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.AppointmentService;
import org.springframework.samples.petclinic.service.PetService;
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


	@ModelAttribute("appointment")
	public Appointment loadPetWithAppointment(@PathVariable("petId") final int petId) {
		Pet pet = this.petService.findPetById(petId);
		Appointment appointment = new Appointment();
		appointment.addPet(pet);
		return appointment;
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
	
	@GetMapping(value = "/owners/{ownerId}/pets/{petId}/appointments/{appointmentId}/delete")
	public String deleteAppointment(@PathVariable("appointmentId") int appointmentId, @PathVariable("petId") int petId) {
		Appointment appointment = this.appointmentService.findAppointmentById(appointmentId);
        Pet pet =  this.petService.findPetById(petId);
        pet.deleteAppointments(appointment);
        this.appointmentService.deleteAppointment(appointment);
        
        return "redirect:/owners/{ownerId}";
	}
}
