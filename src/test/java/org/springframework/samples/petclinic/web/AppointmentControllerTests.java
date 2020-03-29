package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.mockito.BDDMockito.given;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.AppointmentService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AppointmentController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class AppointmentControllerTests {

	private static final int TEST_APPOINTMENT_ID1 = 1;

	private static final int TEST_APPOINTMENT_ID2 = 2;

	private static final int TEST_APPOINTMENT_ID3 = 3;

	private static final int TEST_OWNER_ID = 1;

	private static final int TEST_PET_ID = 1;

	@Autowired
	private AppointmentController appointmentController;

	@MockBean
	private AppointmentService appointmentService;

	@MockBean
	private OwnerController ownerController;

	@MockBean
	private OwnerService ownerService;

	@MockBean
	private PetController petCotroller;

	@MockBean
	private PetService petService;

	@Autowired
	private MockMvc mockMvc;

	private Appointment appointment1;

	private Appointment appointment2;

	private Appointment appointment3;

	@BeforeEach
	void setup() {
		appointment1 = new Appointment();
		appointment1.setId(TEST_APPOINTMENT_ID1);
		appointment1.setAppointmentDate(LocalDate.now().plusDays(10));
		appointment1.setAppointmentRequestDate(LocalDate.of(2020, 02, 26));
		appointment1.setDescription("Revisión de perro");
		given(this.appointmentService.getAppointmentById(TEST_APPOINTMENT_ID1)).willReturn(appointment1);
		given(this.petService.findPetById(TEST_PET_ID)).willReturn(new Pet());

		appointment2 = new Appointment();
		appointment2.setId(TEST_APPOINTMENT_ID2);
		appointment2.setAppointmentDate(LocalDate.now().plusDays(1));
		appointment2.setAppointmentRequestDate(LocalDate.of(2020, 02, 26));
		appointment2.setDescription("Revisión de perro");
		given(this.appointmentService.getAppointmentById(TEST_APPOINTMENT_ID2)).willReturn(appointment2);
		given(this.petService.findPetById(TEST_PET_ID)).willReturn(new Pet());

		appointment3 = new Appointment();
		appointment3.setId(TEST_APPOINTMENT_ID3);
		appointment3.setAppointmentDate(LocalDate.now().plusDays(2));
		appointment3.setAppointmentRequestDate(LocalDate.of(2020, 02, 26));
		appointment3.setDescription("Revisión de perro");
		given(this.appointmentService.getAppointmentById(TEST_APPOINTMENT_ID3)).willReturn(appointment3);
		given(this.petService.findPetById(TEST_PET_ID)).willReturn(new Pet());
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessDeleteAppointment() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/appointments/{appointmentId}/delete", TEST_OWNER_ID, TEST_PET_ID, TEST_APPOINTMENT_ID1))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessDeleteAppointmentErrorsBefore() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/appointments/{appointmentId}/delete", TEST_OWNER_ID, TEST_PET_ID, TEST_APPOINTMENT_ID2))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessDeleteAppointmentErrorsNow() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/appointments/{appointmentId}/delete", TEST_OWNER_ID, TEST_PET_ID, TEST_APPOINTMENT_ID3))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/owners/{ownerId}"));
	}
}
