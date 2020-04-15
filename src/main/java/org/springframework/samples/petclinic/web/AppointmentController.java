
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.AppointmentService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.exceptions.VeterinarianNotAvailableException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppointmentController {

	private static final String	VIEWS_PETS_CREATE_OR_UPDATE_APPOINTMENT_FORM = "pets/createOrUpdateAppointmentForm";	
	private static final String REDIRECT_TO_OUPS = "redirect:/oups";

	@Autowired
	private AppointmentService	appointmentService;

	@Autowired
	private OwnerService		ownerService;

	@Autowired
	private PetService			petService;

	@Autowired
	private VetService			vetService;


	@ModelAttribute("appointment")
	public Appointment loadPetWithAppointment(@PathVariable("ownerId") final int ownerId, @PathVariable("petId") final int petId) {
		Owner owner = this.ownerService.findOwnerById(ownerId);
		Pet pet = this.petService.findPetById(petId);
		Appointment appointment = new Appointment();
		appointment.setOwner(owner);
		appointment.setPet(pet);
		return appointment;
	}

	@ModelAttribute("vets")
	public Collection<Vet> loadVets() {
		return this.vetService.findVets();
	}

	@GetMapping(value = "/owners/{ownerId}/pets/{petId}/appointments/new")
	public String initNewAppointmentForm(@PathVariable("ownerId") final int ownerId, @PathVariable("petId") final int petId) {
		if (securityAccessRequestAppointment(ownerId, petId)) {
			return VIEWS_PETS_CREATE_OR_UPDATE_APPOINTMENT_FORM;
		} else {
			return REDIRECT_TO_OUPS;
		}
	}

	@GetMapping(value = "/owners/{ownerId}/pets/{petId}/appointments/{appointmentId}/edit")
	public String initAppointmentEditForm(@PathVariable("appointmentId") final int appointmentId, @PathVariable("petId") final int petId, @PathVariable("ownerId") final int ownerId, final ModelMap modelMap) {
		if (securityAccessRequestAppointment(ownerId, petId)) {
			Appointment appointment = this.appointmentService.getAppointmentById(appointmentId);
			modelMap.put("appointment", appointment);
			modelMap.put("edit", true);
			return VIEWS_PETS_CREATE_OR_UPDATE_APPOINTMENT_FORM;			
		} else {
			return REDIRECT_TO_OUPS;
		}
	}

	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/appointments/new")
	public String processNewAppointmentForm(@Valid final Appointment appointment, final BindingResult result, @PathVariable("petId") final int petId, @PathVariable("ownerId") final int ownerId,
			@ModelAttribute("vet") final Integer vetId, final ModelMap modelMap) {
		if (securityAccessRequestAppointment(ownerId, petId)) {
			if (result.hasErrors()) {
				return VIEWS_PETS_CREATE_OR_UPDATE_APPOINTMENT_FORM;
			} else {
				try {
					this.appointmentService.saveAppointment(appointment, vetId);
				} catch (VeterinarianNotAvailableException e) {
					modelMap.put("vetError", "Imposible realizar una cita con esos datos");
					return VIEWS_PETS_CREATE_OR_UPDATE_APPOINTMENT_FORM;
				}
				return "redirect:/owners/{ownerId}";
			}	
		} else {
			return REDIRECT_TO_OUPS;
		}
	}

	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/appointments/{appointmentId}/edit")
	public String processAppointmentEditForm(@Valid final Appointment appointment, final BindingResult result, @PathVariable("petId") final int petId, @PathVariable("ownerId") final int ownerId,
			@PathVariable("appointmentId") final int appointmentId, final ModelMap modelMap) {
		if (securityAccessRequestAppointment(ownerId, petId)) {
			modelMap.put("edit", true);
			if (result.hasErrors()) {
				return VIEWS_PETS_CREATE_OR_UPDATE_APPOINTMENT_FORM;
			} else {
				try {
					this.appointmentService.editAppointment(appointment);
				} catch (VeterinarianNotAvailableException e) {
					modelMap.put("vetError", "Imposible realizar una cita con esos datos");
					return VIEWS_PETS_CREATE_OR_UPDATE_APPOINTMENT_FORM;
				}
			}
			return "redirect:/owners/{ownerId}";			
		} else {
			return REDIRECT_TO_OUPS;
		}
	}

	// TODO Implementar el filtro de securityAccessRequestAppointment
	@GetMapping(value = "/owners/{ownerId}/pets/{petId}/appointments/{appointmentId}/delete")
	public ModelAndView deleteAppointment(@PathVariable("ownerId") final int ownerId, @PathVariable("appointmentId") final int appointmentId, @PathVariable("petId") final int petId, final ModelMap model) {
		Appointment appointment = this.appointmentService.getAppointmentById(appointmentId);
		Pet pet = this.petService.findPetById(petId);
		ModelAndView mav = new ModelAndView("owners/ownerDetails");
		mav.addObject(this.ownerService.findOwnerById(ownerId));

		if (appointment.getAppointmentDate().minusDays(2).isEqual(LocalDate.now()) || appointment.getAppointmentDate().minusDays(2).isBefore(LocalDate.now())) {
			model.addAttribute("errors", "No se puede cancelar una cita con 2 dias o menos de antelación");

		} else {
			pet.deleteAppointment(appointment);
			this.appointmentService.deleteAppointment(appointment);
		}
		return mav;
	}
	
	private boolean securityAccessRequestAppointment(int ownerId, int petId) {
		String authority = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.collect(Collectors.toList()).get(0).toString();
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
	
}
