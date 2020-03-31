package org.springframework.samples.petclinic.web;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.AppointmentService;
import org.springframework.samples.petclinic.service.BannerService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VetService;
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
	private BannerService bannerService;

	@MockBean
	private PetController petCotroller;

	@MockBean
	private PetService petService;
	
	@MockBean
	private VetController vetController;
	
	@MockBean
	private VetService vetService;

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private Appointment appointment1;

	@Mock
	private Appointment appointment2;

	@Mock
	private Appointment appointment3;
	
	@Mock Vet rafael;

	@BeforeEach
	void setup() {
		appointment1 = new Appointment();
		appointment1.setId(TEST_APPOINTMENT_ID1);
		appointment1.setAppointmentDate(LocalDate.now().plusDays(10));
		appointment1.setAppointmentRequestDate(LocalDate.of(2020, 02, 26));
		appointment1.setDescription("Revisión de perro");
		given(this.appointmentService.getAppointmentById(TEST_APPOINTMENT_ID1)).willReturn(appointment1);
		given(this.petService.findPetById(TEST_PET_ID)).willReturn(new Pet());
		given(this.ownerService.findOwnerById(TEST_OWNER_ID)).willReturn(new Owner());

		rafael = new Vet();
		rafael.setId(1);
		rafael.setFirstName("Rafael");
		rafael.setLastName("Ortega");
		rafael.setAddress("110 W. Liberty St.");
		rafael.setCity("Madison");
		rafael.setTelephone("608555102");
		User user = new User();
		user.setUsername("vet1");
		user.setPassword("veter1n4ri0_");
		user.setEnabled(true);
		rafael.setUser(user);
		given(this.vetService.findVetById(1)).willReturn(rafael);
		
		appointment2 = new Appointment();
		appointment2.setId(TEST_APPOINTMENT_ID2);
		appointment2.setAppointmentDate(LocalDate.now().plusDays(1));
		appointment2.setAppointmentRequestDate(LocalDate.of(2020, 02, 26));
		appointment2.setDescription("Revisión de perro");
		given(this.appointmentService.getAppointmentById(TEST_APPOINTMENT_ID2)).willReturn(appointment2);

		appointment3 = new Appointment();
		appointment3.setId(TEST_APPOINTMENT_ID3);
		appointment3.setAppointmentDate(LocalDate.now().plusDays(2));
		appointment3.setAppointmentRequestDate(LocalDate.of(2020, 02, 26));
		appointment3.setDescription("Revisión de perro");
		given(this.appointmentService.getAppointmentById(TEST_APPOINTMENT_ID3)).willReturn(appointment3);
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitNewAppointmentForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/appointments/new", TEST_OWNER_ID, TEST_PET_ID))
		.andExpect(status().isOk())
		.andExpect(view().name("pets/createOrUpdateAppointmentForm"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitAppointmentEditForm() throws Exception{
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/appointments/{appointmentId}/edit", TEST_OWNER_ID, TEST_PET_ID, TEST_APPOINTMENT_ID1))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("appointment"))
			.andExpect(model().attributeExists("edit"))
			.andExpect(model().attribute("appointment", hasProperty("description",is("Revisión de perro"))))
			.andExpect(view().name("pets/createOrUpdateAppointmentForm"));
	}
	
	@WithMockUser(value="spring")
	@Test
	void testprocessNewAppointmentForm() throws Exception{
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/appointments/new", TEST_OWNER_ID, TEST_PET_ID).with(csrf())
				.param("appointmentDate", "2020/12/01")
				.param("appointmentRequestDate", "2020/03/01")
				.param("description", "Problema con la pata de mi perro")
				.flashAttr("vet", rafael.getId()))
				.andExpect(status().is3xxRedirection());
	}
	
	@WithMockUser(value="spring")
	@Test
	void testprocessNewAppointmentFormHasErrors() throws Exception{
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/appointments/new", TEST_OWNER_ID, TEST_PET_ID).with(csrf())
				.param("appointmentDate", "")
				.param("appointmentRequestDate", "2020/03/01")
				.param("description", "")
				.flashAttr("vet", rafael.getId()))
				.andExpect(model().attributeHasErrors("appointment"))
				.andExpect(model().attributeHasFieldErrors("appointment", "appointmentDate"))
				.andExpect(model().attributeHasFieldErrors("appointment","description"))
				.andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateAppointmentForm"));
	}
	
	@WithMockUser(value="spring")
	@Test
	void testprocessUpdateAppointmentForm() throws Exception{
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/appointments/{appointmentId}/edit", TEST_OWNER_ID, TEST_PET_ID, TEST_APPOINTMENT_ID1).with(csrf())
				.param("appointmentDate", "2020/12/01")
				.param("appointmentRequestDate", "2020/03/01")
				.param("description", "Vacunación de mi perro")
				.flashAttr("vet", rafael.getId()))
				.andExpect(view().name("redirect:/owners/{ownerId}"));
	}
	
	@WithMockUser(value="spring")
	@Test
	void testprocessUpdateAppointmentFormHasErrors() throws Exception{
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/appointments/{appointmentId}/edit", TEST_OWNER_ID, TEST_PET_ID, TEST_APPOINTMENT_ID1).with(csrf())
				.param("appointmentDate", "")
				.param("appointmentRequestDate", "2020/03/01")
				.param("description", "")
				.flashAttr("vet", rafael.getId()))
				.andExpect(model().attributeHasErrors("appointment"))
				.andExpect(model().attributeHasFieldErrors("appointment", "appointmentDate"))
				.andExpect(model().attributeHasFieldErrors("appointment","description"))
				.andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateAppointmentForm"));
	}
	


	@WithMockUser(value = "spring")
	@Test
	void testProcessDeleteAppointment() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/appointments/{appointmentId}/delete", TEST_OWNER_ID, TEST_PET_ID, TEST_APPOINTMENT_ID1))
		.andExpect(status().isOk())
		.andExpect(view().name("owners/ownerDetails"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessDeleteAppointmentErrorsBefore() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/appointments/{appointmentId}/delete", TEST_OWNER_ID, TEST_PET_ID, TEST_APPOINTMENT_ID2))
		.andExpect(status().isOk())
		.andExpect(view().name("owners/ownerDetails"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessDeleteAppointmentErrorsNow() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/appointments/{appointmentId}/delete", TEST_OWNER_ID, TEST_PET_ID, TEST_APPOINTMENT_ID3))
		.andExpect(status().isOk())
		.andExpect(view().name("owners/ownerDetails"));
	}
}
