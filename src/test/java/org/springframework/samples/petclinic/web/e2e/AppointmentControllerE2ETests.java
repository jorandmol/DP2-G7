package org.springframework.samples.petclinic.web.e2e;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.util.PetclinicDates;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class AppointmentControllerE2ETests {

	private static final String OWNER_ROLE = "owner";

	private static final String VIEWS_PETS_CREATE_OR_UPDATE_APPOINTMENT_FORM = "pets/createOrUpdateAppointmentForm";
	private static final String VIEWS_PETS_DETAILS = "pets/myPetsActive";
	private static final String REDIRECT_TO_OUPS = "redirect:/oups";
	private static final String REDIRECT_TO_PETS_DETAILS = "redirect:/owner/pets";

	private static final int TEST_APPOINTMENT_ID_5 = 5;
	private static final int TEST_APPOINTMENT_ID_6 = 6;
	private static final int TEST_APPOINTMENT_ID_7 = 7;
	private static final int TEST_OWNER_ID = 1;
	private static final int TEST_PET_ID = 1;
	private static final int TEST_WRONG_OWNER_ID = 2;

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testInitNewAppointmentForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/appointments/new", TEST_OWNER_ID, TEST_PET_ID))
			.andExpect(status().isOk())
			.andExpect(view().name(VIEWS_PETS_CREATE_OR_UPDATE_APPOINTMENT_FORM));
	}

	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testNotInitNewAppointmentForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/appointments/new", TEST_WRONG_OWNER_ID, TEST_PET_ID))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name(REDIRECT_TO_OUPS));
	}

	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testInitAppointmentEditForm() throws Exception{
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/appointments/{appointmentId}/edit", TEST_OWNER_ID, TEST_PET_ID, TEST_APPOINTMENT_ID_7))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("appointment"))
			.andExpect(model().attributeExists("edit"))
			.andExpect(model().attribute("appointment", hasProperty("description",is("Fracture treatment"))))
			.andExpect(view().name(VIEWS_PETS_CREATE_OR_UPDATE_APPOINTMENT_FORM));
	}

	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testNotInitAppointmentEditForm() throws Exception{
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/appointments/{appointmentId}/edit", TEST_WRONG_OWNER_ID, TEST_PET_ID, TEST_APPOINTMENT_ID_5))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name(REDIRECT_TO_OUPS));
	}

	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testProcessNewAppointmentForm() throws Exception{
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/appointments/new", TEST_OWNER_ID, TEST_PET_ID).with(csrf())
			.param("appointmentDate", PetclinicDates.getFormattedFutureDate(LocalDate.now(), 5, "yyyy/MM/dd"))
			.param("description", "Problema con la pata de mi perro")
			.flashAttr("vet", 6))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name(REDIRECT_TO_PETS_DETAILS));
	}

	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testProcessNewAppointmentFormHasErrors() throws Exception{
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/appointments/new", TEST_OWNER_ID, TEST_PET_ID).with(csrf())
			.param("appointmentDate", "")
			.param("description", "")
			.flashAttr("vet", 6))
			.andExpect(model().attributeHasErrors("appointment"))
			.andExpect(model().attributeHasFieldErrors("appointment", "appointmentDate"))
			.andExpect(model().attributeHasFieldErrors("appointment","description"))
			.andExpect(status().isOk())
			.andExpect(view().name(VIEWS_PETS_CREATE_OR_UPDATE_APPOINTMENT_FORM));
	}

	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testProcessAppointmentEditForm() throws Exception{
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/appointments/{appointmentId}/edit", TEST_OWNER_ID, TEST_PET_ID, TEST_APPOINTMENT_ID_6).with(csrf())
			.param("appointmentDate", "2020/06/30")
			.param("description", "Vacunación de mi perro")
			.param("id", "6")
			.param("vet", "1"))
			.andExpect(view().name(REDIRECT_TO_PETS_DETAILS));
	}

	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testProcessAppointmentEditFormHasErrors() throws Exception{
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/appointments/{appointmentId}/edit", TEST_OWNER_ID, TEST_PET_ID, TEST_APPOINTMENT_ID_5).with(csrf())
			.param("appointmentDate", "")
			.param("description", "")
			.flashAttr("vet", 5))
			.andExpect(model().attributeHasErrors("appointment"))
			.andExpect(model().attributeHasFieldErrors("appointment", "appointmentDate"))
			.andExpect(model().attributeHasFieldErrors("appointment","description"))
			.andExpect(status().isOk())
			.andExpect(view().name(VIEWS_PETS_CREATE_OR_UPDATE_APPOINTMENT_FORM));
	}

	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testProcessDeleteAppointment() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/appointments/{appointmentId}/delete", TEST_OWNER_ID, TEST_PET_ID, TEST_APPOINTMENT_ID_7))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name(REDIRECT_TO_PETS_DETAILS));
	}

	@ParameterizedTest
	@CsvSource({
		"1,1,10",
		"1,1,11"
	})
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testProcessDeleteAppointmentErrors(int ownerId, int petId, int appointmentId) throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/appointments/{appointmentId}/delete", ownerId, petId, appointmentId))
			.andExpect(model().attributeExists("errors"))
			.andExpect(status().isOk())
			.andExpect(view().name(VIEWS_PETS_DETAILS));
	}
}
