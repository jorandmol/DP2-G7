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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.BannerService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AppointmentController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class AppointmentControllerTests {
	
	private static final String OWNER_ROLE = "owner";
	
	private static final String VIEWS_PETS_CREATE_OR_UPDATE_APPOINTMENT_FORM = "pets/createOrUpdateAppointmentForm";
	private static final String VIEWS_OWNER_DETAILS = "owners/ownerDetails";
	private static final String REDIRECT_TO_OUPS = "redirect:/oups";
	private static final String REDIRECT_TO_OWNER_DETAILS = "redirect:/owners/{ownerId}";

	private static final int TEST_APPOINTMENT_ID_1 = 1;
	private static final int TEST_APPOINTMENT_ID_2 = 2;
	private static final int TEST_APPOINTMENT_ID_3 = 3;
	private static final int TEST_APPOINTMENT_ID_4 = 4;
	private static final int TEST_OWNER_ID = 1;
	private static final int TEST_PET_ID = 1;
	private static final int TEST_WRONG_OWNER_ID = 2;
	
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
	
	@MockBean
	private AuthoritiesService authoritiesService;

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private Appointment appointment1;

	@Mock
	private Appointment appointment2;

	@Mock
	private Appointment appointment3;
	
	@Mock
	private Appointment appointment4;
	
	@Mock Vet vet;
	
	private String dateToday;
	private String dateFuture;

	@BeforeEach
	void setup() {
		LocalDate localDateToday = LocalDate.now().getDayOfWeek().equals(DayOfWeek.SUNDAY) ? 
				LocalDate.now().plusDays(1) : LocalDate.now();
		LocalDate localDateFuture = LocalDate.now().plusDays(10).getDayOfWeek().equals(DayOfWeek.SUNDAY) ?
				LocalDate.now().plusDays(11) : LocalDate.now().plusDays(10);
		dateFuture = localDateFuture.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		dateToday = localDateToday.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		
		appointment1 = new Appointment();
		appointment1.setId(TEST_APPOINTMENT_ID_1);
		appointment1.setAppointmentDate(LocalDate.now().plusDays(10));
		appointment1.setAppointmentRequestDate(LocalDate.of(2020, 02, 26));
		appointment1.setDescription("Revisión de perro");
		
		appointment2 = new Appointment();
		appointment2.setId(TEST_APPOINTMENT_ID_2);
		appointment2.setAppointmentDate(localDateToday.plusDays(1));
		appointment2.setAppointmentRequestDate(localDateToday.minusDays(10));
		appointment2.setDescription("Revisión de perro");

		appointment3 = new Appointment();
		appointment3.setId(TEST_APPOINTMENT_ID_3);
		appointment3.setAppointmentDate(localDateToday.plusDays(2));
		appointment3.setAppointmentRequestDate(localDateToday.minusDays(10));
		appointment3.setDescription("Revisión de perro");

		appointment4 = new Appointment();
		appointment4.setId(TEST_APPOINTMENT_ID_4);
		appointment4.setAppointmentDate(localDateFuture.plusDays(2));
		appointment4.setAppointmentRequestDate(localDateToday.minusDays(10));
		appointment4.setDescription("Falta de apetito");
		
		User user = new User();
		user.setEnabled(true);
		user.setUsername("owner1");
		
		Pet pet = new Pet();
		pet.setId(TEST_PET_ID);
		appointment1.setPet(pet);
		appointment2.setPet(pet);
		appointment3.setPet(pet);
		appointment4.setPet(pet);
		
		Owner owner = new Owner();
		owner.setId(TEST_OWNER_ID);
		owner.setUser(user);
		this.authoritiesService.saveAuthorities("owner1", OWNER_ROLE);
		owner.addPet(pet);
		appointment1.setOwner(owner);
		appointment2.setOwner(owner);
		appointment3.setOwner(owner);
		appointment4.setOwner(owner);
		
		vet = new Vet();
		vet.setId(1);
		vet.setFirstName("Rafael");
		vet.setLastName("Ortega");
		vet.setAddress("110 W. Liberty St.");
		vet.setCity("Madison");
		vet.setTelephone("608555102");
		User userVet = new User();
		userVet.setUsername("vet1");
		userVet.setPassword("veter1n4ri0_");
		userVet.setEnabled(true);
		vet.setUser(userVet);
		
		given(this.appointmentService.getAppointmentById(TEST_APPOINTMENT_ID_1)).willReturn(appointment1);
		given(this.petService.findPetById(TEST_PET_ID)).willReturn(pet);
		given(this.ownerService.findOwnerById(TEST_OWNER_ID)).willReturn(owner);
		given(this.appointmentService.getAppointmentById(TEST_APPOINTMENT_ID_2)).willReturn(appointment2);
		given(this.appointmentService.getAppointmentById(TEST_APPOINTMENT_ID_3)).willReturn(appointment3);
		given(this.appointmentService.getAppointmentById(TEST_APPOINTMENT_ID_4)).willReturn(appointment4);
		given(this.vetService.findVetById(1)).willReturn(vet);
	}
	
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
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/appointments/{appointmentId}/edit", TEST_OWNER_ID, TEST_PET_ID, TEST_APPOINTMENT_ID_1))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("appointment"))
			.andExpect(model().attributeExists("edit"))
			.andExpect(model().attribute("appointment", hasProperty("description",is("Revisión de perro"))))
			.andExpect(view().name(VIEWS_PETS_CREATE_OR_UPDATE_APPOINTMENT_FORM));
	}
	
	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testNotInitAppointmentEditForm() throws Exception{
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/appointments/{appointmentId}/edit", TEST_WRONG_OWNER_ID, TEST_PET_ID, TEST_APPOINTMENT_ID_1))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name(REDIRECT_TO_OUPS));
	}
	
	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testProcessNewAppointmentForm() throws Exception{
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/appointments/new", TEST_OWNER_ID, TEST_PET_ID).with(csrf())
			.param("appointmentDate", dateFuture)
			.param("appointmentRequestDate", dateToday)
			.param("description", "Problema con la pata de mi perro")
			.flashAttr("vet", vet.getId()))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name(REDIRECT_TO_OWNER_DETAILS));
	}
	
	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testProcessNewAppointmentFormHasErrors() throws Exception{
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/appointments/new", TEST_OWNER_ID, TEST_PET_ID).with(csrf())
			.param("appointmentDate", "")
			.param("appointmentRequestDate", dateToday)
			.param("description", "")
			.flashAttr("vet", vet.getId()))
			.andExpect(model().attributeHasErrors("appointment"))
			.andExpect(model().attributeHasFieldErrors("appointment", "appointmentDate"))
			.andExpect(model().attributeHasFieldErrors("appointment","description"))
			.andExpect(status().isOk())
			.andExpect(view().name(VIEWS_PETS_CREATE_OR_UPDATE_APPOINTMENT_FORM));
	}
	
	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testProcessAppointmentEditForm() throws Exception{
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/appointments/{appointmentId}/edit", TEST_OWNER_ID, TEST_PET_ID, TEST_APPOINTMENT_ID_1).with(csrf())
			.param("appointmentDate", dateFuture)
			.param("appointmentRequestDate", dateToday)
			.param("description", "Vacunación de mi perro")
			.flashAttr("vet", vet.getId()))
			.andExpect(view().name(REDIRECT_TO_OWNER_DETAILS));
	}
	
	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testProcessAppointmentEditFormHasErrors() throws Exception{
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/appointments/{appointmentId}/edit", TEST_OWNER_ID, TEST_PET_ID, TEST_APPOINTMENT_ID_1).with(csrf())
			.param("appointmentDate", "")
			.param("appointmentRequestDate", dateToday)
			.param("description", "")
			.flashAttr("vet", vet.getId()))
			.andExpect(model().attributeHasErrors("appointment"))
			.andExpect(model().attributeHasFieldErrors("appointment", "appointmentDate"))
			.andExpect(model().attributeHasFieldErrors("appointment","description"))
			.andExpect(status().isOk())
			.andExpect(view().name(VIEWS_PETS_CREATE_OR_UPDATE_APPOINTMENT_FORM));
	}
	
	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testProcessDeleteAppointment() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/appointments/{appointmentId}/delete", TEST_OWNER_ID, TEST_PET_ID, TEST_APPOINTMENT_ID_1))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name(REDIRECT_TO_OWNER_DETAILS));
	}

	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testProcessDeleteAppointmentErrorsBefore() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/appointments/{appointmentId}/delete", TEST_OWNER_ID, TEST_PET_ID, TEST_APPOINTMENT_ID_2))
			.andExpect(model().attributeExists("errors"))	
			.andExpect(status().isOk())
			.andExpect(view().name(VIEWS_OWNER_DETAILS));
	}

	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testProcessDeleteAppointmentErrorsNow() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/appointments/{appointmentId}/delete", TEST_OWNER_ID, TEST_PET_ID, TEST_APPOINTMENT_ID_3))
			.andExpect(model().attributeExists("errors"))
			.andExpect(status().isOk())
			.andExpect(view().name(VIEWS_OWNER_DETAILS));
	}
}
