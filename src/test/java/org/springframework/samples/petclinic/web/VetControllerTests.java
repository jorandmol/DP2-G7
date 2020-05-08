package org.springframework.samples.petclinic.web;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.xml.HasXPath.hasXPath;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.AppointmentService;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.BannerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for the {@link VetController}
 */
@WebMvcTest(controllers = VetController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class VetControllerTests {

	private static final int TEST_VET_ID_1 = 1;
	private static final int TEST_VET_ID_2 = 2;
	private static final int TEST_VET_ID_3 = 3;
	private static final int TEST_VET_ID_5 = 5;
	
	private static final Integer TEST_PET_ID_1 = 1;
	private static final Integer TEST_PET_ID_2 = 2;
	private static final Integer TEST_PET_ID_3 = 3;
	
	private static final String REDIRECT_TO_OUPS = "redirect:/oups";
	private static final String VIEWS_VET_CREATE_OR_UPDATE_FORM = "vets/createOrUpdateVetForm";

	@MockBean
	private BannerService bannerService;

	@MockBean
	private VetSpecialtiesFormatter vetSpecialtiesFormatter;
	
	@MockBean
	private VetService vetService;

	@MockBean
	private PetService petService;

	@MockBean
	private AppointmentService appointmentService;

	@MockBean
	private UserService userService;

	@MockBean
	private AuthoritiesService authoritiesService;
	
	@Autowired
	private MockMvc mockMvc;

	@Mock
	private Vet vet1;
	
	@Mock
	private Vet vet2;
	
	@Mock
	private Vet vet4;
	
	@Mock
	private Vet vet5;

	@Mock
	private User user1;

	@Mock
	private List<Specialty> specialties1;

	@Mock
	private List<Appointment> appointments;
	
	@Mock
	private List<Appointment> appointmentsToday1;
	
	@Mock
	private List<Appointment> nextAppointments1;

	@BeforeEach
	void setup() {

		LocalDate date = LocalDate.now();
		if(date.getDayOfWeek().equals(DayOfWeek.SUNDAY))
			date.plusDays(1);
		
		Vet james = new Vet();
		james.setFirstName("James");
		james.setLastName("Carter");
		james.setId(1);
		Vet helen = new Vet();
		helen.setFirstName("Helen");
		helen.setLastName("Leary");
		helen.setId(2);

		Specialty radiology = new Specialty();
		radiology.setId(1);
		radiology.setName("radiology");
		Specialty surgery = new Specialty();
		surgery.setId(2);
		surgery.setName("surgery");

		specialties1.add(radiology);
		specialties1.add(surgery);

		Pet pet1 = new Pet();
		pet1.setId(TEST_PET_ID_1);
		Pet pet2 = new Pet();
		pet2.setId(TEST_PET_ID_2);
		Pet pet3 = new Pet();
		pet3.setId(TEST_PET_ID_3);

		// vet1
		vet1 = new Vet();
		vet1.setId(TEST_VET_ID_1);
		vet1.setFirstName("Rafael");
		vet1.setLastName("Ortega");
		vet1.setAddress("110 W. Liberty St.");
		vet1.setCity("Madison");
		vet1.setTelephone("608555102");
		vet1.addSpecialty(surgery);
		user1 = new User();
		user1.setUsername("vet1");
		user1.setPassword("veter1n4ri0_1");
		user1.setEnabled(true);
		vet1.setUser(user1);
		this.authoritiesService.saveAuthorities("vet1", "veterinarian");

		// vet2 - creado para comprobar que el listado de citas es vacio para este vet
		//Se emplea para los accesos no permitidos por seguridad
		User user2 = new User();
		user2.setUsername("vet2");
		user2.setPassword("veter1n4ri0_2");
		user2.setEnabled(true);
		vet2 = new Vet();
		vet2.setFirstName("Rafael");
		vet2.setLastName("Ortega");
		vet2.setAddress("110 W. Liberty St.");
		vet2.setCity("Madison");
		vet2.setTelephone("608555102");
		vet2.addSpecialty(surgery);
		vet2.setUser(user2);
		vet2.setId(TEST_VET_ID_2);
		this.authoritiesService.saveAuthorities("vet2", "veterinarian");
		
		// vet3
		User user3 = new User();
		user3.setUsername("vet3");
		user3.setPassword("veter1n4ri0_3");
		user3.setEnabled(true);
		Vet vet3= new Vet();
		vet3.setUser(user3);
		vet3.setId(TEST_VET_ID_3);
		this.authoritiesService.saveAuthorities("vet3", "veterinarian");
		
		//vet4
		vet4 = new Vet();
		vet4.setUser(user1);
		vet4.setId(TEST_VET_ID_1);
		vet4.setFirstName("Juan");
		vet4.setLastName("Ortega");
		vet4.setAddress("110 W. Liberty St.");
		vet4.setCity("Madison");
		vet4.setTelephone("608555102");
		vet4.addSpecialty(surgery);
		vet4.addSpecialty(radiology);
		vet4.setUser(user1);
		
		//vet5
		vet5 = new Vet();
		vet5.setId(TEST_VET_ID_5);
		vet5.setFirstName("Alberto");
		vet5.setLastName("Ortega");
		vet5.setAddress("110 W. Liberty St.");
		vet5.setCity("Madison");
		vet5.setTelephone("608555102");
		vet5.addSpecialty(radiology);
		User user5 = new User();
		user5.setUsername("vet1");
		user5.setPassword("veter1n4ri0_5");
		user5.setEnabled(true);
		vet5.setUser(user5);
		
		appointmentsToday1 = new ArrayList<>();
		Appointment appointmentToday1 = new Appointment();
		appointmentToday1.setAppointmentDate(date);
		appointmentToday1.setVet(vet1);
		appointmentToday1.setPet(pet1);
		appointmentsToday1.add(appointmentToday1);
		
		nextAppointments1 = new ArrayList<>();
		Appointment nextAppointment = new Appointment();
		nextAppointment.setAppointmentDate(date.plusWeeks(1));
		nextAppointment.setVet(vet1);
		nextAppointment.setPet(pet2);
		nextAppointments1.add(nextAppointment);
		
		appointments= new ArrayList<>();
		
		List<Appointment> appointmentsToday2 = new ArrayList<>();
		Appointment appointmentToday2 = new Appointment();
		appointmentToday2.setAppointmentDate(date);
		appointmentToday2.setVet(vet2);
		appointmentToday2.setPet(pet3);
		appointmentsToday2.add(appointmentToday2);
		
		List<Appointment> nextAppointments2 = new ArrayList<>();
		
		given(this.vetService.findVetById(TEST_VET_ID_1)).willReturn(vet1);
        given(this.vetService.findVets()).willReturn(Lists.newArrayList(james, helen));
        given(this.vetService.findSpecialties()).willReturn(specialties1);
        given(this.appointmentService.getAppointmentsTodayByVetId(TEST_VET_ID_1, LocalDate.now())).willReturn(appointmentsToday1);
        given(this.appointmentService.getNextAppointmentsByVetId(TEST_VET_ID_1, LocalDate.now())).willReturn(nextAppointments1);
        given(this.appointmentService.getAppointmentsTodayByVetId(TEST_VET_ID_2, LocalDate.now())).willReturn(appointmentsToday2);
        given(this.appointmentService.getNextAppointmentsByVetId(TEST_VET_ID_2, LocalDate.now())).willReturn(nextAppointments2);
        given(this.appointmentService.getAppointmentsTodayByVetId(TEST_VET_ID_3, LocalDate.now())).willReturn(appointments);
        given(this.appointmentService.getNextAppointmentsByVetId(TEST_VET_ID_3, LocalDate.now())).willReturn(appointments);
        given(this.vetService.findVetByUsername("vet1")).willReturn(vet1);
        given(this.vetService.findVetByUsername("vet2")).willReturn(vet2);
        given(this.vetService.findVetByUsername("vet3")).willReturn(vet3);
        given(this.petService.countVisitsByDate(TEST_PET_ID_1, LocalDate.now())).willReturn(0);
        given(this.petService.countVisitsByDate(TEST_PET_ID_2, LocalDate.now())).willReturn(1);
        given(this.petService.countVisitsByDate(TEST_PET_ID_3, LocalDate.now())).willReturn(1);
        Mockito.doThrow(DataIntegrityViolationException.class)
        	.when(this.vetService).saveVet(vet5);

	}
	
	
	// TEST para usuario que SI cumple la seguridad
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testShowVetList() throws Exception {
		mockMvc.perform(get("/vets")).andExpect(status().isOk()).andExpect(model().attributeExists("vets"))
				.andExpect(view().name("vets/vetList"));
	}

	// TEST para usuario que NO cumple la seguridad
	@WithMockUser(username = "vet1", password = "veter1n4ri0_1", authorities = "veterinarian")
	@Test
	void testShowVetListWithoutAccess() throws Exception {
		mockMvc.perform(get("/vets")).andExpect(status().is3xxRedirection()).andExpect(view().name(REDIRECT_TO_OUPS));
	}

	
	@WithMockUser(value = "spring")
	@Test
	void testShowVetListXml() throws Exception {
		mockMvc.perform(get("/vets.xml").accept(MediaType.APPLICATION_XML)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_XML_VALUE))
				.andExpect(content().node(hasXPath("/vets/vetList[id=1]/id")));
	}

	
	// TEST para usuario que SI cumple la seguridad
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/vets/new")).andExpect(status().isOk()).andExpect(model().attributeExists("vet"))
				.andExpect(view().name(VIEWS_VET_CREATE_OR_UPDATE_FORM));
	}

	// TEST para usuario que NO cumple la seguridad
	@WithMockUser(username = "vet1", password = "veter1n4ri0_1", authorities = "veterinarian")
	@Test
	void testInitCreationFormWithoutAccess() throws Exception {
		mockMvc.perform(get("/vets/new")).andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}
	

	// TEST para usuario que SI cumple la seguridad
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/vets/new").with(csrf())
				.flashAttr("vet", vet1))
				.andExpect(status().is3xxRedirection());
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessCreationFormCatchException() throws Exception {
		
		mockMvc.perform(post("/vets/new").with(csrf())
				.flashAttr("vet", vet5))
				.andExpect(model().errorCount(1))
				.andExpect(model().hasErrors())
				.andExpect(status().isOk())
				.andExpect(view().name(VIEWS_VET_CREATE_OR_UPDATE_FORM));

	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/vets/new").with(csrf())
				.param("firstName", "")
				.param("lastName", "Bloggs")
				.param("city", "London")
				.param("user.username", "joeBloggs")
				.param("address", "44, Los Rosales")
				.param("telephone", "1234567")
				.param("user.password", "noNumbersPass_"))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("vet"))
				.andExpect(model().attributeHasFieldErrors("vet", "firstName"))
				.andExpect(model().attributeHasFieldErrors("vet", "telephone"))
				.andExpect(model().attributeHasFieldErrors("vet", "user.password"))
				.andExpect(view().name(VIEWS_VET_CREATE_OR_UPDATE_FORM));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessCreationFormHasEmptyFields() throws Exception {
		mockMvc.perform(post("/vets/new").with(csrf())
				.param("firstName", "")
				.param("lastName", "")
				.param("city", "")
				.param("address", "")
				.param("telephone", "")
				.param("user.username", "")
				.param("user.password", ""))
				.andExpect(status().isOk()).andExpect(model().attributeHasErrors("vet"))
				.andExpect(model().attributeHasFieldErrors("vet", "firstName"))
				.andExpect(model().attributeHasFieldErrors("vet", "lastName"))
				.andExpect(model().attributeHasFieldErrors("vet", "city"))
				.andExpect(model().attributeHasFieldErrors("vet", "address"))
				.andExpect(model().attributeHasFieldErrors("vet", "telephone"))
				.andExpect(model().attributeHasFieldErrors("vet", "user.password"))
				.andExpect(view().name(VIEWS_VET_CREATE_OR_UPDATE_FORM));
	}

	// TEST para usuario que NO cumple la seguridad
	@WithMockUser(username = "vet1", password = "veter1n4ri0_1", authorities = "veterinarian")
	@Test
	void testProcessCreationFormSuccessWithoutAccess() throws Exception {
		mockMvc.perform(post("/vets/new")
				.param("firstName", "Elena")
				.param("lastName", "Molino").with(csrf())
				.param("address", "38 Avenida América")
				.param("city", "London")
				.param("telephone", "123456789")
				.param("user.username", "vet55")
				.param("user.password", "v3terinario_55"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}
	
	
	// TEST para usuarios que SI cumple la seguridad
	@WithMockUser(username = "vet1", password = "veter1n4ri0_1", authorities = "veterinarian")
	@Test
	void testInitUpdateProfileForm() throws Exception {
		mockMvc.perform(get("/vets/{vetId}/edit", TEST_VET_ID_1))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("edit"))
				.andExpect(model().attributeExists("vet"))
				.andExpect(model().attribute("vet", hasProperty("firstName", is("Rafael"))))
				.andExpect(model().attribute("vet", hasProperty("lastName", is("Ortega"))))
				.andExpect(model().attribute("vet", hasProperty("address", is("110 W. Liberty St."))))
				.andExpect(model().attribute("vet", hasProperty("city", is("Madison"))))
				.andExpect(model().attribute("vet", hasProperty("telephone", is("608555102"))))
				.andExpect(view().name(VIEWS_VET_CREATE_OR_UPDATE_FORM));
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testInitUpdateVetForm() throws Exception {
		mockMvc.perform(get("/vets/{vetId}/edit", TEST_VET_ID_1))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("edit"))
				.andExpect(model().attributeExists("vet"))
				.andExpect(model().attribute("vet", hasProperty("firstName", is("Rafael"))))
				.andExpect(model().attribute("vet", hasProperty("lastName", is("Ortega"))))
				.andExpect(model().attribute("vet", hasProperty("address", is("110 W. Liberty St."))))
				.andExpect(model().attribute("vet", hasProperty("city", is("Madison"))))
				.andExpect(model().attribute("vet", hasProperty("telephone", is("608555102"))))
				.andExpect(view().name(VIEWS_VET_CREATE_OR_UPDATE_FORM));
	}
	
	// TEST para usuarios que NO cumplen la seguridad
	@WithMockUser(username = "vet2", password = "veter1n4ri0_2", authorities = "veterinarian")
	@Test
	void testInitUpdateVetFormWithoutAccess() throws Exception {
		mockMvc.perform(get("/vets/{vetId}/edit", TEST_VET_ID_1))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}
	
	@WithMockUser(username = "vet2", password = "veter1n4ri0_2", authorities = "owner")
	@Test
	void testInitUpdateVetFormWithoutAuthorities() throws Exception {
		mockMvc.perform(get("/vets/{vetId}/edit", TEST_VET_ID_1))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}
	
	
	
	
	// TEST para usuarios que SI cumple la seguridad
	@WithMockUser(username = "vet1", password = "veter1n4ri0_1", authorities = "veterinarian")
	@Test
	void testProcessUpdateProfileFormSuccess() throws Exception {
		mockMvc.perform(post("/vets/{vetId}/edit", TEST_VET_ID_1).with(csrf())
				.flashAttr("vet", vet1))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/vets/{vetId}"));

	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessUpdateVetFormSuccess() throws Exception {		
		mockMvc.perform(post("/vets/{vetId}/edit", TEST_VET_ID_1).with(csrf())
				.flashAttr("vet", vet4))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/vets/{vetId}"));

	}

	@WithMockUser(username = "vet1", password = "veter1n4ri0_1", authorities = "veterinarian")
	@Test
	void testProcessUpdateVetFormHasErrors() throws Exception {
		mockMvc.perform(post("/vets/{vetId}/edit", TEST_VET_ID_1).with(csrf())
				.param("firstName", "Joe")
				.param("lastName", "Bloggs")
				.param("telephone", "123456789")
				.param("user.password", "v3terin4ri0_1")
				.param("address", "")
				.param("city", ""))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("vet"))
				.andExpect(model().attributeHasFieldErrors("vet", "address"))
				.andExpect(model().attributeHasFieldErrors("vet", "city"))
				.andExpect(view().name(VIEWS_VET_CREATE_OR_UPDATE_FORM));
	}
	
	// TEST para usuario que NO cumple la seguridad
	@WithMockUser(username = "vet2", password = "veter1n4ri0_2", authorities = "veterinarian")
	@Test
	void testProcessUpdateVetFormSuccessWithoutAccess() throws Exception {
		mockMvc.perform(post("/vets/{vetId}/edit", TEST_VET_ID_1).with(csrf())
				.param("firstName", "Rafael")
				.param("lastName", "Bloggs")
				.param("address", "123 Caramel Street")
				.param("city", "London")
				.param("telephone", "123456789")
				.param("user.password", "str0ng_passw0rd"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));

	}
	

	
	
	//Comprobar que, las listas de appointments de un vet TIENEN citas 
	@WithMockUser(username = "vet1", password = "veter1n4ri0_1", authorities = "veterinarian")
	@Test
	void testShowAppoimentsByVetList() throws Exception {
		mockMvc.perform(get("/appointments"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("appointmentsToday"))
				.andExpect(model().attribute("appointmentsToday", appointmentsToday1))
				.andExpect(model().attributeExists("appointmentsWithVisit"))
				.andExpect(model().attributeExists("nextAppointments"))
				.andExpect(model().attribute("nextAppointments", nextAppointments1 ))
				.andExpect(view().name("vets/appointmentsList"));
	}

	//Comprobar que, las listas de appointments de un vet que no tiene, estan VACIAS
	@WithMockUser(username = "vet2", password = "veter1n4ri0_2", authorities = "veterinarian")
	@Test
	void testShowAppoimentsByVetListWithVisits() throws Exception {
		mockMvc.perform(get("/appointments")).andExpect(status().isOk())
				.andExpect(model().attributeExists("appointmentsToday"))
				.andExpect(model().attributeExists("appointmentsWithVisit"))
				.andExpect(model().attributeExists("nextAppointments"))
				.andExpect(view().name("vets/appointmentsList"));

	}
	
	@WithMockUser(username="vet3", password="veter1n4ri0_3", authorities="veterinarian")
	@Test
	void testShowAppoimentsByVetListEmpty() throws Exception {
		mockMvc.perform(get("/appointments")).andExpect(status().isOk())
				.andExpect(model().attribute("appointmentsToday", appointments))
				.andExpect(model().attributeExists("appointmentsWithVisit"))
				.andExpect(model().attribute("nextAppointments", appointments))
				.andExpect(view().name("vets/appointmentsList"));

	}

	// TEST para usuarios que SI cumple la seguridad
	@WithMockUser(username = "vet1", password = "veter1n4ri0_1", authorities = "veterinarian")
	@Test
	void testShowVetProfile() throws Exception {
		mockMvc.perform(get("/vets/{vetId}", TEST_VET_ID_1)).andExpect(status().isOk())
				.andExpect(model().attribute("vet", hasProperty("firstName", is("Rafael"))))
				.andExpect(model().attribute("vet", hasProperty("lastName", is("Ortega"))))
				.andExpect(model().attribute("vet", hasProperty("address", is("110 W. Liberty St."))))
				.andExpect(model().attribute("vet", hasProperty("city", is("Madison"))))
				.andExpect(model().attribute("vet", hasProperty("telephone", is("608555102"))))
				.andExpect(view().name("vets/vetDetails"));
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testShowVet() throws Exception {
		mockMvc.perform(get("/vets/{vetId}", TEST_VET_ID_1)).andExpect(status().isOk())
				.andExpect(model().attribute("vet", hasProperty("firstName", is("Rafael"))))
				.andExpect(model().attribute("vet", hasProperty("lastName", is("Ortega"))))
				.andExpect(model().attribute("vet", hasProperty("address", is("110 W. Liberty St."))))
				.andExpect(model().attribute("vet", hasProperty("city", is("Madison"))))
				.andExpect(model().attribute("vet", hasProperty("telephone", is("608555102"))))
				.andExpect(view().name("vets/vetDetails"));
	}
	
	// TEST para usuarios que NO cumplen la seguridad
	@WithMockUser(username = "vet2", password = "veter1n4ri0_2", authorities = "veterinarian")
	@Test
	void testShowVetWithoutAccess() throws Exception {
		mockMvc.perform(get("/vets/{vetId}", TEST_VET_ID_1))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}
	
	@WithMockUser(username = "vet2", password = "veter1n4ri0_2", authorities = "owner")
	@Test
	void testShowVetWithoutAuthorities() throws Exception {
		mockMvc.perform(get("/vets/{vetId}", TEST_VET_ID_1))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}
	
	@WithMockUser(username="vet1", password="veter1n4ri0_1", authorities="veterinarian")
	@Test
	void testShowPetsList() throws Exception {
		mockMvc.perform(get("/vets/pets")).andExpect(status().isOk())
				.andExpect(model().attributeExists("pets"))
				.andExpect(view().name("pets/petsList"));
	}

}