package org.springframework.samples.petclinic.web.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.web.AppointmentController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppointmentControllerIntegrationTests {

	private static final String OWNER_ROLE = "owner";

	private static final String VIEWS_PETS_CREATE_OR_UPDATE_APPOINTMENT_FORM = "pets/createOrUpdateAppointmentForm";
	private static final String VIEW_TO_PETS_ACCEPTED_AND_ACTIVE = "pets/myPetsActive";
	private static final String REDIRECT_TO_OUPS = "redirect:/oups";
	private static final String REDIRECT_TO_PETS_DETAILS = "redirect:/owner/pets";

	private static final int TEST_APPOINTMENT_ID_1 = 1;
	private static final int TEST_APPOINTMENT_ID_4 = 4;
	private static final int TEST_OWNER_ID = 1;
	private static final int TEST_PET_ID = 1;
	private static final int TEST_VET_ID = 1;
	private static final int TEST_WRONG_OWNER_ID = 2;
	
	private LocalDate date = LocalDate.now().plusDays(100);
	private LocalDate appointmentDate1 = date.getDayOfWeek().equals(DayOfWeek.SUNDAY) ? date.plusDays(1) : date;
	private LocalDate appointmentDate2 = date.plusDays(1).getDayOfWeek().equals(DayOfWeek.SUNDAY) ? date.plusDays(2) : date.plusDays(1);
	
	@Autowired
	private AppointmentController appointmentController;
	
	@Autowired
	private OwnerService ownerService;
	
	@Autowired
	private PetService petService;
	
	@Autowired
	private VetService vetService;
	
	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testInitNewAppointmentForm() throws Exception {
		Appointment appointment = appointmentController.loadPetWithAppointment(TEST_OWNER_ID, TEST_PET_ID);
		String view = appointmentController.initNewAppointmentForm(TEST_OWNER_ID, TEST_PET_ID);
		assertEquals(appointment.getOwner().getId(), TEST_OWNER_ID);
		assertEquals(view, VIEWS_PETS_CREATE_OR_UPDATE_APPOINTMENT_FORM);
	}
	
	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testNotInitNewAppointmentForm() throws Exception {
		String view = appointmentController.initNewAppointmentForm(TEST_WRONG_OWNER_ID, TEST_PET_ID);
		assertEquals(view, REDIRECT_TO_OUPS);
	}
	
	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testInitAppointmentEditForm() throws Exception {
		ModelMap modelMap = new ModelMap();
		String view = appointmentController.initAppointmentEditForm(7, TEST_PET_ID, TEST_OWNER_ID, modelMap);
		assertEquals(view, VIEWS_PETS_CREATE_OR_UPDATE_APPOINTMENT_FORM);
	}

	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testInitAppointmentEditFormErrorsBefore() throws Exception{
		ModelMap modelMap = new ModelMap();
		String view = appointmentController.initAppointmentEditForm(10, TEST_PET_ID, TEST_OWNER_ID, modelMap);
		assertEquals(view, VIEW_TO_PETS_ACCEPTED_AND_ACTIVE);
	}
	
	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testInitAppointmentEditFormErrorsNow() throws Exception{
		ModelMap modelMap = new ModelMap();
		String view = appointmentController.initAppointmentEditForm(11, TEST_PET_ID, TEST_OWNER_ID, modelMap);
		assertEquals(view, VIEW_TO_PETS_ACCEPTED_AND_ACTIVE);
	}
	
	@Test
	@WithMockUser(username="owner1", password="0wn3333_1", authorities=OWNER_ROLE)
	void testNotInitAppointmentEditFormAppointmentNotYours() throws Exception{
		ModelMap modelMap = new ModelMap();
		String view = appointmentController.initAppointmentEditForm(TEST_APPOINTMENT_ID_4, TEST_PET_ID, TEST_OWNER_ID, modelMap);
		assertEquals(view, REDIRECT_TO_OUPS);
	}

	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testNotInitAppointmentEditForm() throws Exception{
		ModelMap modelMap = new ModelMap();
		String view = appointmentController.initAppointmentEditForm(TEST_APPOINTMENT_ID_1, TEST_PET_ID, TEST_WRONG_OWNER_ID, modelMap);
		assertEquals(view, REDIRECT_TO_OUPS);
	}

	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testProcessNewAppointmentForm() throws Exception{
		Appointment appointment = new Appointment();
		appointment.setAppointmentDate(appointmentDate1.plusDays(3));
		appointment.setDescription("Appointment's description...");
		appointment.setOwner(this.ownerService.findOwnerById(TEST_OWNER_ID));
		appointment.setPet(this.petService.findPetById(TEST_PET_ID));
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		ModelMap modelMap = new ModelMap();
		String view = appointmentController.processNewAppointmentForm(appointment, result, TEST_PET_ID, TEST_OWNER_ID, TEST_VET_ID, modelMap);
		assertEquals(view, REDIRECT_TO_PETS_DETAILS);
	}

	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testProcessNewAppointmentFormHasErrors() throws Exception{
		Appointment appointment = new Appointment();
		appointment.setAppointmentDate(null);
		appointment.setDescription(null);
		appointment.setOwner(this.ownerService.findOwnerById(TEST_OWNER_ID));
		appointment.setPet(this.petService.findPetById(TEST_PET_ID));
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		ModelMap modelMap = new ModelMap();
		String view = appointmentController.processNewAppointmentForm(appointment, result, TEST_PET_ID, TEST_OWNER_ID, TEST_VET_ID, modelMap);
		assertEquals(view, VIEWS_PETS_CREATE_OR_UPDATE_APPOINTMENT_FORM);
	}

	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testProcessAppointmentEditForm() throws Exception{
		Appointment appointment = new Appointment();
		appointment.setId(7);
		appointment.setAppointmentDate(appointmentDate2);
		appointment.setDescription("Appointment's description...");
		appointment.setOwner(this.ownerService.findOwnerById(TEST_OWNER_ID));
		appointment.setPet(this.petService.findPetById(TEST_PET_ID));
		appointment.setVet(this.vetService.findVetById(TEST_VET_ID));
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		ModelMap modelMap = new ModelMap();
		String view = appointmentController.processAppointmentEditForm(appointment, result, TEST_PET_ID, TEST_OWNER_ID, 7, modelMap);
		assertEquals(view, REDIRECT_TO_PETS_DETAILS);
	}

	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testProcessAppointmentEditFormHasErrors() throws Exception{
		Appointment appointment = new Appointment();
		appointment.setId(7);
		appointment.setAppointmentDate(appointmentDate2);
		appointment.setDescription(null);
		appointment.setOwner(this.ownerService.findOwnerById(TEST_OWNER_ID));
		appointment.setPet(this.petService.findPetById(TEST_PET_ID));
		appointment.setVet(this.vetService.findVetById(TEST_VET_ID));
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		ModelMap modelMap = new ModelMap();
		
		assertThrows(TransactionSystemException.class, () -> {
			appointmentController.processAppointmentEditForm(appointment, result, TEST_PET_ID, TEST_OWNER_ID, 7, modelMap);
		});
	}

	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testProcessDeleteAppointment() throws Exception {
		ModelMap modelMap = new ModelMap();
		String view =appointmentController.deleteAppointment(TEST_OWNER_ID, 7, TEST_PET_ID, modelMap);
		assertEquals(view, REDIRECT_TO_PETS_DETAILS);
	}

	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testProcessDeleteAppointmentErrorsBefore() throws Exception {
		ModelMap modelMap = new ModelMap();
		String view =appointmentController.deleteAppointment(TEST_OWNER_ID, 10, TEST_PET_ID, modelMap);
		assertEquals(view, VIEW_TO_PETS_ACCEPTED_AND_ACTIVE);
	}

	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testProcessDeleteAppointmentErrorsNow() throws Exception {
		ModelMap modelMap = new ModelMap();
		String view =appointmentController.deleteAppointment(TEST_OWNER_ID, 11, TEST_PET_ID, modelMap);
		assertEquals(view, VIEW_TO_PETS_ACCEPTED_AND_ACTIVE);
	}

}
