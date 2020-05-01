package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

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
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.AppointmentService;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.BannerService;
import org.springframework.samples.petclinic.service.MedicalTestService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for {@link VisitController}
 *
 * @author Colin But
 */
@WebMvcTest(controllers = VisitController.class,
			excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
			excludeAutoConfiguration= SecurityConfiguration.class)
class VisitControllerTests {

	private static final int TEST_PET_ID_1 = 1;
	
	private static final int TEST_PET_ID_2 = 2;
	
	private static final int TEST_VET_ID_1 = 1;

	private static final int TEST_VET_ID_2 = 2;

	private static final int TEST_APPOINTMENT_ID_1 = 1;
	
	private static final int TEST_APPOINTMENT_ID_2 = 2;
	
	private static final int TEST_VISIT_ID = 1;

	private static final int TEST_OWNER_ID_1 = 1;

	private static final int TEST_OWNER_ID_2 = 2;

	
	@MockBean
	private AppointmentService appointmentService;
	
	@MockBean
	private BannerService bannerService;

	@MockBean
	private PetService petService;
	
	@MockBean
	private MedicalTestService medicalTestService;
	
	@MockBean
	private AuthoritiesService authoritiesService;
	
	@MockBean
	private VetService vetService;
	
	@MockBean
	private OwnerService ownerService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	private Appointment appointment1;

	@Mock
	private Appointment appointment2;
	
	@BeforeEach
	void setup() {
		
		LocalDate date = LocalDate.now();
		if(date.getDayOfWeek().equals(DayOfWeek.SUNDAY))
			date.plusDays(1);
		
		appointment1 = new Appointment();
		appointment1.setId(TEST_APPOINTMENT_ID_1);
		appointment1.setAppointmentDate(date);
		appointment1.setAppointmentRequestDate(date.minusMonths(1));
		appointment1.setDescription("Revision");
		
		appointment2 = new Appointment();
		appointment2.setId(TEST_APPOINTMENT_ID_2);
		appointment2.setAppointmentDate(date);
		appointment2.setAppointmentRequestDate(date.minusMonths(1));
		appointment2.setDescription("Revision");
		
		PetType dog = new PetType();
		dog.setId(1);
		dog.setName("dog");
		
		Pet pet1 = new Pet();
		pet1.setBirthDate(date.minusYears(5));
		pet1.setName("Rosy");
		pet1.setType(dog);
		pet1.setId(TEST_PET_ID_2);
		appointment1.setPet(pet1);
		
		Pet pet2 = new Pet();
		pet2.setBirthDate(date.minusYears(5));
		pet2.setName("Rosy");
		pet2.setId(TEST_PET_ID_2);
		appointment2.setPet(pet2);
		
		User user1 = new User();
		user1.setUsername("vet1");
		user1.setPassword("veter1n4ri0_1");
		user1.setEnabled(true);
		Vet vet1 = new Vet();
		vet1.setUser(user1);
		vet1.setId(TEST_VET_ID_1);
		this.authoritiesService.saveAuthorities("vet1", "veterinarian");
		appointment1.setVet(vet1);
		
		User user2 = new User();
		user2.setUsername("vet2");
		user2.setPassword("veter1n4ri0_2");
		user2.setEnabled(true);
		Vet vet2 = new Vet();
		vet2.setUser(user2);
		vet2.setId(TEST_VET_ID_2);
		this.authoritiesService.saveAuthorities("vet2", "veterinarian");
		appointment2.setVet(vet2);
		
		User user3 = new User();
		user3.setUsername("owner1");
		user3.setPassword("0wn3333r_1");
		user3.setEnabled(true);
		this.authoritiesService.saveAuthorities("owner1", "owner");
		Owner owner1 = new Owner();
		owner1.setId(TEST_OWNER_ID_1);
		owner1.setUser(user3);
		owner1.addPet(pet2);
		
		User user4 = new User();
		user4.setUsername("owner2");
		user4.setPassword("0wn3333r_2");
		user4.setEnabled(true);
		this.authoritiesService.saveAuthorities("owner2", "owner");
		Owner owner2 = new Owner();
		owner2.setId(TEST_OWNER_ID_2);
		owner2.setUser(user4);
		
		Visit visit1 = new Visit();
		visit1.setDate(date);
		visit1.setDescription("Revision");
		visit1.setId(TEST_VISIT_ID);
		visit1.setMedicalTests(new ArrayList<>());
		visit1.setPet(pet2);
		
		
		
		given(this.vetService.findVetByUsername("vet1")).willReturn(vet1);
		given(this.petService.findPetById(TEST_PET_ID_1)).willReturn(pet1);
		given(this.appointmentService.findAppointmentByDate(TEST_PET_ID_1, date)).willReturn(appointment1);
		given(this.petService.countVisitsByDate(TEST_PET_ID_1, date)).willReturn(0);
		
		given(this.vetService.findVetByUsername("vet2")).willReturn(vet2);
		given(this.petService.findPetById(TEST_PET_ID_2)).willReturn(pet2);
		given(this.appointmentService.findAppointmentByDate(TEST_PET_ID_2, date)).willReturn(appointment2);
		given(this.petService.countVisitsByDate(TEST_PET_ID_2, date)).willReturn(1);
		
		given(this.petService.findVisitById(TEST_VISIT_ID)).willReturn(visit1);		
		given(this.ownerService.findOwnerById(TEST_OWNER_ID_1)).willReturn(owner1);
		given(this.ownerService.findOwnerById(TEST_OWNER_ID_2)).willReturn(owner2);
	}

    @WithMockUser(username="vet1", password="veter1n4ri0_1", authorities="veterinarian")
    @Test
	void testInitNewVisitFormSuccess() throws Exception {
		mockMvc.perform(get("/owners/*/pets/{petId}/visits/new", TEST_PET_ID_1))
				.andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateVisitForm"));
	}
    
    @WithMockUser(username="vet2", password="veter1n4ri0_2", authorities="veterinarian")
    @Test
	void testInitNewVisitFormDuplicated() throws Exception {
		mockMvc.perform(get("/owners/*/pets/{petId}/visits/new", TEST_PET_ID_2))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}

	@WithMockUser(username="vet1", password="veter1n4ri0_1", authorities="veterinarian")
    @Test
	void testProcessNewVisitFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/*/pets/{petId}/visits/new", TEST_PET_ID_1)
				.param("name", "George").with(csrf())
				.param("description", "Visit Description"))                                
                .andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/appointments"));
	}
	
	@WithMockUser(username="vet2", password="veter1n4ri0_2", authorities="veterinarian")
    @Test
	void testProcessNewVisitFormWithWrongVet() throws Exception {
		mockMvc.perform(post("/owners/*/pets/{petId}/visits/new", TEST_PET_ID_1)
				.param("name", "George").with(csrf())
				.param("description", "Visit Description"))                                
                .andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}

	@WithMockUser(username="vet1", password="veter1n4ri0_1", authorities="veterinarian")
    @Test
	void testProcessNewVisitFormHasErrors() throws Exception {
		mockMvc.perform(post("/owners/*/pets/{petId}/visits/new", TEST_PET_ID_1)
							.with(csrf())
							.param("name", "George"))
				.andExpect(model().attributeHasErrors("visit")).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateVisitForm"));
	}

	@WithMockUser(username="owner1", password="0wn3333r_1", authorities="owner")
    @Test
	void testShowVisitSuccessWithOwnerUser() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/visits/{visitId}", TEST_OWNER_ID_1, TEST_PET_ID_2, TEST_VISIT_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("visits/visitDetails"));
	}
	
	@WithMockUser(username="admin1", password="4dm1n", authorities="admin")
    @Test
	void testShowVisitSuccessWithAdminUser() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/visits/{visitId}", TEST_OWNER_ID_1, TEST_PET_ID_2, TEST_VISIT_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("visits/visitDetails"));
	}
	
	@WithMockUser(username="owner1", password="0wn3333r_1")
    @Test
	void testShowVisitWithoutAuthorities() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/visits/{visitId}", TEST_OWNER_ID_1, TEST_PET_ID_2, TEST_VISIT_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
	@WithMockUser(username="vet1", password="veter1n4ri0_1", authorities="veterinarian")
    @Test
	void testShowVisitWithoutAuthoritiesOrUsername() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/visits/{visitId}", TEST_OWNER_ID_1, TEST_PET_ID_2, TEST_VISIT_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
	@WithMockUser(username="owner2", password="0wn3333r_2", authorities="owner")
	@Test
	void testShowVisitWithWrongOwner() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/visits/{visitId}", TEST_OWNER_ID_1, TEST_PET_ID_2, TEST_VISIT_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}

}
