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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
import org.springframework.samples.petclinic.model.PetRegistrationStatus;
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
	private static final String REDIRECT_TO_OUPS = "redirect:/oups";
	private static final String REDIRECT_TO_PETS_DETAILS = "redirect:/owner/pets";

	private static final int TEST_APPOINTMENT_ID_1 = 1;
	private static final int TEST_APPOINTMENT_ID_2 = 2;
	private static final int TEST_APPOINTMENT_ID_3 = 3;
	private static final int TEST_APPOINTMENT_ID_4 = 4;
	private static final int TEST_APPOINTMENT_ID_5 = 5;
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
	
	@Mock
	private Appointment appointment5;

	@Mock Vet vet;

	private String dateFuture;

	@BeforeEach
	void setup() {
		LocalDate localDateToday = LocalDate.now().getDayOfWeek().equals(DayOfWeek.SUNDAY) ?
				LocalDate.now().plusDays(1) : LocalDate.now();
		LocalDate localDateFuture = LocalDate.now().plusDays(10).getDayOfWeek().equals(DayOfWeek.SUNDAY) ?
				LocalDate.now().plusDays(11) : LocalDate.now().plusDays(10);
		dateFuture = localDateFuture.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

		appointment1 = new Appointment();
		appointment1.setId(TEST_APPOINTMENT_ID_1);
		appointment1.setAppointmentDate(localDateToday.plusDays(10));
		appointment1.setAppointmentRequestDate(LocalDate.of(2020, 2, 26));
		appointment1.setDescription("Revisión de perro");

		appointment2 = new Appointment();
		appointment2.setId(TEST_APPOINTMENT_ID_2);
		appointment2.setAppointmentDate(LocalDate.now().plusDays(1));
		appointment2.setAppointmentRequestDate(localDateToday.minusDays(10));
		appointment2.setDescription("Revisión de perro");

		appointment3 = new Appointment();
		appointment3.setId(TEST_APPOINTMENT_ID_3);
		appointment3.setAppointmentDate(LocalDate.now());
		appointment3.setAppointmentRequestDate(localDateToday.minusDays(10));
		appointment3.setDescription("Revisión de perro");

		appointment4 = new Appointment();
		appointment4.setId(TEST_APPOINTMENT_ID_4);
		appointment4.setAppointmentDate(LocalDate.now().plusDays(2));
		appointment4.setAppointmentRequestDate(localDateToday.minusDays(10));
		appointment4.setDescription("Falta de apetito");
		
		appointment5 = new Appointment();
		appointment5.setId(TEST_APPOINTMENT_ID_5);
		appointment5.setAppointmentDate(localDateToday.plusDays(10));
		appointment5.setAppointmentRequestDate(localDateToday.minusDays(10));
		appointment5.setDescription("Revisión mensual");

		User user1 = new User();
		user1.setEnabled(true);
		user1.setUsername("owner1");
		
		User user2 = new User();
		user2.setEnabled(true);
		user2.setUsername("owner2");

		Pet pet = new Pet();
		pet.setId(TEST_PET_ID);
		pet.setActive(true);
		pet.setStatus(PetRegistrationStatus.ACCEPTED);
		appointment1.setPet(pet);
		appointment2.setPet(pet);
		appointment3.setPet(pet);
		appointment4.setPet(pet);
		
		Pet pet2 = new Pet();
		pet2.setId(2);
		pet2.setActive(true);
		pet2.setStatus(PetRegistrationStatus.ACCEPTED);
		appointment5.setPet(pet2);

		Owner owner1 = new Owner();
		owner1.setId(TEST_OWNER_ID);
		owner1.setUser(user1);
		this.authoritiesService.saveAuthorities("owner1", OWNER_ROLE);
		owner1.addPet(pet);
		appointment1.setOwner(owner1);
		appointment2.setOwner(owner1);
		appointment3.setOwner(owner1);
		appointment4.setOwner(owner1);
		
		Owner owner2 = new Owner();
		owner2.setId(TEST_WRONG_OWNER_ID);
		owner2.setUser(user2);
		owner2.addPet(pet2);
		appointment5.setOwner(owner2);
		
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
		given(this.ownerService.findOwnerById(TEST_OWNER_ID)).willReturn(owner1);
		given(this.ownerService.findOwnerById(TEST_WRONG_OWNER_ID)).willReturn(owner2);
		given(this.appointmentService.getAppointmentById(TEST_APPOINTMENT_ID_2)).willReturn(appointment2);
		given(this.appointmentService.getAppointmentById(TEST_APPOINTMENT_ID_3)).willReturn(appointment3);
		given(this.appointmentService.getAppointmentById(TEST_APPOINTMENT_ID_4)).willReturn(appointment4);
		given(this.appointmentService.getAppointmentById(TEST_APPOINTMENT_ID_5)).willReturn(appointment5);
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

	@ParameterizedTest
	@CsvSource({
		"1,1,4",
		"1,1,3"
	})
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testInitAppointmentEditFormErrors(int ownerId, int petId, int appointmentId) throws Exception{
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/appointments/{appointmentId}/edit", ownerId, petId, appointmentId))
			.andExpect(model().attributeExists("errors"))
			.andExpect(status().isOk())
			.andExpect(view().name("owners/"+ownerId+"/pets/"+petId+"/appointments/"+appointmentId+"/edit"));
	}
	
	@ParameterizedTest
	@CsvSource({
		"1,1,5",
		"2,1,1"
	})
	@WithMockUser(username="owner1", password="0wn3333_1", authorities=OWNER_ROLE)
	void testNotInitAppointmentEditForm(int ownerId, int petId, int appointmentId) throws Exception{
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/appointments/{appointmentId}/edit", ownerId, petId, appointmentId))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name(REDIRECT_TO_OUPS));
	}

	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testProcessNewAppointmentForm() throws Exception{
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/appointments/new", TEST_OWNER_ID, TEST_PET_ID).with(csrf())
			.param("appointmentDate", dateFuture)
			.param("description", "Problema con la pata de mi perro")
			.flashAttr("vet", vet.getId()))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name(REDIRECT_TO_PETS_DETAILS));
	}

	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testProcessNewAppointmentFormHasErrors() throws Exception{
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/appointments/new", TEST_OWNER_ID, TEST_PET_ID).with(csrf())
			.param("appointmentDate", "")
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
			.param("description", "Vacunación de mi perro")
			.flashAttr("vet", vet.getId()))
			.andExpect(view().name(REDIRECT_TO_PETS_DETAILS));
	}

	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testProcessAppointmentEditFormHasErrors() throws Exception{
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/appointments/{appointmentId}/edit", TEST_OWNER_ID, TEST_PET_ID, TEST_APPOINTMENT_ID_1).with(csrf())
			.param("appointmentDate", "")
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
			.andExpect(view().name(REDIRECT_TO_PETS_DETAILS));
	}
	
	@ParameterizedTest
	@CsvSource({
		"1,1,4",
		"1,1,3"
	})
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testProcessDeleteAppointmentErrorsBefore(int ownerId, int petId, int appointmentId) throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/appointments/{appointmentId}/delete", ownerId, petId, appointmentId))
			.andExpect(model().attributeExists("errors"))
			.andExpect(status().isOk())
			.andExpect(view().name("owners/"+ownerId+"/pets/"+petId+"/appointments/"+appointmentId+"/delete"));
	}
}
