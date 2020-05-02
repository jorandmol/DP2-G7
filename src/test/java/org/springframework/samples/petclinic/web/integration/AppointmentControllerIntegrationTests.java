package org.springframework.samples.petclinic.web.integration;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.service.AppointmentService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.web.AppointmentController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppointmentControllerIntegrationTests {

	private static final String OWNER_ROLE = "owner";

	private static final String VIEWS_PETS_CREATE_OR_UPDATE_APPOINTMENT_FORM = "pets/createOrUpdateAppointmentForm";
	private static final String VIEW_TO_PETS_ACCEPTED_AND_ACTIVE = "pets/myPetsActive";
	private static final String REDIRECT_TO_OUPS = "redirect:/oups";
	private static final String REDIRECT_TO_PETS_DETAILS = "redirect:/owner/pets";

	private static final int TEST_APPOINTMENT_ID_1 = 1;
	private static final int TEST_APPOINTMENT_ID_2 = 2;
	private static final int TEST_APPOINTMENT_ID_3 = 3;
	private static final int TEST_APPOINTMENT_ID_4 = 4;
	private static final int TEST_OWNER_ID = 1;
	private static final int TEST_PET_ID = 1;
	private static final int TEST_WRONG_OWNER_ID = 2;
	
	@Autowired
	private AppointmentController appointmentController;
	
	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testInitNewAppointmentForm() throws Exception {
		Appointment appointment = appointmentController.loadPetWithAppointment(TEST_OWNER_ID, TEST_PET_ID);
		ModelMap model = new ModelMap();
		model.put("appointment", appointment);
		String view = appointmentController.initNewAppointmentForm(TEST_OWNER_ID, TEST_PET_ID);
		assertEquals(appointment.getOwner().getId(), TEST_OWNER_ID);
		assertEquals(view, VIEWS_PETS_CREATE_OR_UPDATE_APPOINTMENT_FORM);
	}
	
}
