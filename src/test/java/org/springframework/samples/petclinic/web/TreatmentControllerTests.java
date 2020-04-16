package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Treatment;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.BannerService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.TreatmentService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers=TreatmentController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
public class TreatmentControllerTests {
	
	private static final String OWNER_ROLE = "owner";
	
	private static final String VIEWS_TREATMENT_LIST = "treatments/listTreatments";
	private static final String REDIRECT_TO_OUPS = "redirect:/oups";

	private static final int TEST_OWNER_ID = 1;
	private static final int TEST_PET_ID = 1;
	private static final int TEST_WRONG_OWNER_ID = 2;
	private static final int TEST_TREATMENT_ID_1 = 1;
	private static final int TEST_TREATMENT_ID_2 = 2;
	
	@MockBean
	private AuthoritiesService authoritiesService;
	
	@MockBean
	private TreatmentService treatmentService;
	
	@MockBean
	private BannerService bannerService;
	
	@MockBean
	private OwnerService ownerService;
	
	@MockBean
	private PetService petService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@BeforeEach
	void setup() {		
		User user = new User();
		user.setEnabled(true);
		user.setUsername("owner1");
		
		Pet pet = new Pet();	
		pet.setId(TEST_PET_ID);
		
		Medicine medicine = new Medicine();
		medicine.setName("Ibuprofeno");
		Set<Medicine> medicines = new HashSet<>();
		medicines.add(medicine);
		
		Treatment treatment1 = new Treatment();
		treatment1.setId(TEST_TREATMENT_ID_1);
		treatment1.setName("Tratamiento post-operatorio");
		treatment1.setDescription("Ingerir una pastilla de ibuprefono cada 6 horas");
		treatment1.setTimeLimit(LocalDate.now().plusWeeks(2));
		treatment1.setMedicines(medicines);
		treatment1.setPet(pet);
		
		Treatment treatment2 = new Treatment();
		treatment2.setId(TEST_TREATMENT_ID_2);
		treatment2.setName("Tratamiento para curación de fractura");
		treatment2.setDescription("Ingerir una pastilla de ibuprefono cada 6 horas");
		treatment2.setTimeLimit(LocalDate.now().minusMonths(2));
		treatment2.setMedicines(medicines);
		treatment2.setPet(pet);
		
		List<Treatment> treatments = new ArrayList<>();
		treatments.add(treatment1);
		treatments.add(treatment2);
		
		Owner owner = new Owner();
		owner.setId(TEST_OWNER_ID);
		owner.setUser(user);
		this.authoritiesService.saveAuthorities("owner1", OWNER_ROLE);
		owner.addPet(pet);
		
		given(this.petService.findPetById(TEST_PET_ID)).willReturn(pet);
		given(this.ownerService.findOwnerById(TEST_OWNER_ID)).willReturn(owner);
		given(this.treatmentService.findTreatmentsByPet(TEST_PET_ID)).willReturn(treatments);
	}
	
	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testListTreatments() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petID}/treatments", TEST_OWNER_ID, TEST_PET_ID))
			.andExpect(model().attributeExists("treatments"))
			.andExpect(model().attributeExists("treatmentsDone"))
			.andExpect(status().isOk())
			.andExpect(view().name(VIEWS_TREATMENT_LIST));
	}
	
	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testNotListTreatments() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petID}/treatments", TEST_WRONG_OWNER_ID, TEST_PET_ID))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name(REDIRECT_TO_OUPS));
	}
}
