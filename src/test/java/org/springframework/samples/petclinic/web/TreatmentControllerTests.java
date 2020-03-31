package org.springframework.samples.petclinic.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Treatment;
import org.springframework.samples.petclinic.service.BannerService;
import org.springframework.samples.petclinic.service.TreatmentService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers=TreatmentController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
public class TreatmentControllerTests {
	
	private static final int TEST_TREATMENT_ID = 1;
	
	private static final int TEST_OWNER_ID = 1;
	
	private static final int TEST_PET_ID = 1;

	@Autowired
	private TreatmentController treatmentController;
	
	@MockBean
	private TreatmentService treatmentService;
	
	@MockBean
	private BannerService bannerService;
	
	@Autowired
	private MockMvc mockMvc;
	
	private Treatment treatment;
	
	@BeforeEach
	void setup() {
		treatment = new Treatment();
		treatment.setId(TEST_TREATMENT_ID);
		treatment.setName("Dolor muscular");
		treatment.setDescription("Ingerir una pastilla de ibuprefono cada 6 horas");
		treatment.setTimeLimit(LocalDate.of(2020, 8, 20));
		treatment.setMedicines(new HashSet<Medicine>());
		treatment.setPet(new Pet());
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowTreatments() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petID}/treatments", TEST_OWNER_ID, TEST_PET_ID))
		.andExpect(status().isOk())
		.andExpect(view().name("treatments/listTreatments"));
	}
}
