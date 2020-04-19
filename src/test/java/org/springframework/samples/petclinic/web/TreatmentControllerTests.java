package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.samples.petclinic.model.*;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.BannerService;
import org.springframework.samples.petclinic.service.MedicineService;
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
	private static final String VET_ROLE = "veterinarian";

	private static final String VIEWS_TREATMENT_LIST = "treatments/listTreatments";
	private static final String VIEWS_TREATMENT_FORM = "treatments/createOrUpdateTreatmentForm";
	private static final String REDIRECT_TO_OUPS = "redirect:/oups";
	private static final String REDIRECT_TO_VET_TREATMENT_LIST = "redirect:/vets/pets/{petId}/treatments";

	private static final int TEST_OWNER_ID = 1;
    private static final int TEST_VET_ID = 1;
	private static final int TEST_PET_ID = 1;
	private static final int TEST_WRONG_OWNER_ID = 2;
	private static final int TEST_TREATMENT_ID_1 = 1;
	private static final int TEST_TREATMENT_ID_2 = 2;

	@MockBean
	private AuthoritiesService authoritiesService;

	@MockBean
	private TreatmentService treatmentService;

	@MockBean
	private MedicineService medicineService;

	@MockBean
	private BannerService bannerService;

	@MockBean
	private OwnerService ownerService;

	@MockBean
	private PetService petService;

	@Autowired
	private MockMvc mockMvc;

	private Treatment treatment1, treatment2;
	private List<Treatment> treatments;

	@BeforeEach
	void setup() {
		User user1 = new User();
		user1.setEnabled(true);
		user1.setUsername("owner1");

		User user2 = new User();
		user2.setEnabled(true);
		user2.setUsername("vet1");

		Pet pet = new Pet();
		pet.setId(TEST_PET_ID);

		Medicine medicine = new Medicine();
		medicine.setId(1);
		medicine.setName("Ibuprofeno");
        Set<Medicine> medicines = new HashSet<>();
		medicines.add(medicine);

		treatment1 = new Treatment();
		treatment1.setId(TEST_TREATMENT_ID_1);
		treatment1.setName("Tratamiento post-operatorio");
		treatment1.setDescription("Ingerir una pastilla de ibuprefono cada 6 horas");
		treatment1.setTimeLimit(LocalDate.now().plusWeeks(2));
		treatment1.setMedicines(medicines);
		treatment1.setPet(pet);

		treatment2 = new Treatment();
		treatment2.setId(TEST_TREATMENT_ID_2);
		treatment2.setName("Tratamiento para curación de fractura");
		treatment2.setDescription("Ingerir una pastilla de ibuprefono cada 6 horas");
		treatment2.setTimeLimit(LocalDate.now().minusMonths(2));
		treatment2.setMedicines(medicines);
		treatment2.setPet(pet);

		treatments = new ArrayList<>();
		treatments.add(treatment1);
		treatments.add(treatment2);

		Owner owner = new Owner();
		owner.setId(TEST_OWNER_ID);
		owner.setUser(user1);
		this.authoritiesService.saveAuthorities("owner1", OWNER_ROLE);
		owner.addPet(pet);

		Vet vet = new Vet();
		vet.setId(TEST_VET_ID);
		vet.setUser(user2);
		this.authoritiesService.saveAuthorities("vet1", VET_ROLE);

		given(this.petService.findPetById(TEST_PET_ID)).willReturn(pet);
		given(this.ownerService.findOwnerById(TEST_OWNER_ID)).willReturn(owner);
	}

	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testListTreatmentsToOwner() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/treatments", TEST_OWNER_ID, TEST_PET_ID))
			.andExpect(model().attributeExists("treatments"))
			.andExpect(model().attributeExists("treatmentsDone"))
			.andExpect(status().isOk())
			.andExpect(view().name(VIEWS_TREATMENT_LIST));
	}

	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testNotListTreatmentsToOwner() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/treatments", TEST_WRONG_OWNER_ID, TEST_PET_ID))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name(REDIRECT_TO_OUPS));
	}

	@Test
    @WithMockUser(username="vet1", password="v3terinarian_1", authorities=VET_ROLE)
    void testListTreatmentsToVet() throws Exception {
	    mockMvc.perform(get("/vets/pets/{petId}/treatments", TEST_PET_ID))
            .andExpect(model().attributeExists("isVet"))
            .andExpect(model().attributeExists("treatments"))
            .andExpect(model().attributeExists("treatmentsDone"))
            .andExpect(status().isOk())
            .andExpect(view().name(VIEWS_TREATMENT_LIST));
    }

	@Test
	@WithMockUser(username="vet1", password="v3terinarian_1", authorities=VET_ROLE)
	void testInitNewTreatmentForm() throws Exception {
		mockMvc.perform(get("/vets/pets/{petId}/treatments/new", TEST_PET_ID))
			.andExpect(status().isOk())
			.andExpect(view().name(VIEWS_TREATMENT_FORM));
	}

    @Test
    @WithMockUser(username="vet1", password="v3terinarian_1", authorities=VET_ROLE)
    void testProcessNewTreatmentForm() throws Exception {
	    mockMvc.perform(post("/vets/pets/{petId}/treatments/new", TEST_PET_ID).with(csrf())
            .flashAttr("treatment", treatment1))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(REDIRECT_TO_VET_TREATMENT_LIST));
    }
}
