package org.springframework.samples.petclinic.web.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

	private static final int TEST_OWNER_ID = 1;
	private static final int TEST_OWNER_ID3 = 3;
	private static final int TEST_PET_ID = 1;
	private static final int TEST_PET_ID7 = 7;
	private static final int TEST_VET_ID = 1;
	private static final int TEST_WRONG_OWNER_ID = 2;
	
	private LocalDate date = LocalDate.now().plusDays(50);
	private LocalDate appointmentDate = date.getDayOfWeek().equals(DayOfWeek.SUNDAY) ? date.plusDays(1) : date;
	
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

	@ParameterizedTest
	@CsvSource({
		"10,1,1",
		"11,1,1"
	})
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testInitAppointmentEditFormErrors(int appointmentId, int petId, int ownerId) throws Exception{
		ModelMap modelMap = new ModelMap();
		String view = appointmentController.initAppointmentEditForm(appointmentId, petId, ownerId, modelMap);
		assertEquals(view, VIEW_TO_PETS_ACCEPTED_AND_ACTIVE);
	}
	
	@ParameterizedTest
	@CsvSource({
		"4,1,1",
		"1,1,2"
	})
	@WithMockUser(username="owner1", password="0wn3333_1", authorities=OWNER_ROLE)
	void testNotInitAppointmentEditFormAppointment(int appointmentId, int petId, int ownerId) throws Exception{
		ModelMap modelMap = new ModelMap();
		String view = appointmentController.initAppointmentEditForm(appointmentId, petId, ownerId, modelMap);
		assertEquals(view, REDIRECT_TO_OUPS);
	}

	@Test
	@WithMockUser(username="owner3", password="0wn3333r_3", authorities=OWNER_ROLE)
	void testProcessNewAppointmentForm() throws Exception{
		Appointment appointment = new Appointment();
		appointment.setAppointmentDate(appointmentDate);
		appointment.setDescription("Appointment description...");
		appointment.setOwner(this.ownerService.findOwnerById(TEST_OWNER_ID3));
		appointment.setPet(this.petService.findPetById(TEST_PET_ID7));
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		ModelMap modelMap = new ModelMap();
		String view = appointmentController.processNewAppointmentForm(appointment, result, TEST_PET_ID7, TEST_OWNER_ID3, 5, modelMap);
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
		appointment.setAppointmentDate(appointmentDate);
		appointment.setDescription("Appointment description");
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
		appointment.setAppointmentDate(appointmentDate);
		appointment.setDescription("");
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
		String view =appointmentController.deleteAppointment(TEST_OWNER_ID, 8, TEST_PET_ID, modelMap);
		assertEquals(view, REDIRECT_TO_PETS_DETAILS);
	}

	@ParameterizedTest
	@CsvSource({
		"1,10,1",
		"1,11,1"
	})
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testProcessDeleteAppointmentErrors(int ownerId, int appointmentId, int petId) throws Exception {
		ModelMap modelMap = new ModelMap();
		String view =appointmentController.deleteAppointment(ownerId, appointmentId, petId, modelMap);
		assertEquals(view, VIEW_TO_PETS_ACCEPTED_AND_ACTIVE);
	}

}
