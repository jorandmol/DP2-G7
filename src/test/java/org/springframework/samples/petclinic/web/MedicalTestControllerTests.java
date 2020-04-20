package org.springframework.samples.petclinic.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.service.BannerService;
import org.springframework.samples.petclinic.service.MedicalTestService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = MedicalTestController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)

public class MedicalTestControllerTests {

	@MockBean
	private BannerService bannerService;

	@MockBean
	private MedicalTestService medicalTestService;

	@Autowired
	private MockMvc mockMvc;

	@WithMockUser(value = "spring")
	@Test
	void testShowMedicalTestsList() throws Exception {
		mockMvc.perform(get("/medical-tests")).andExpect(status().isOk())
				.andExpect(model().attributeExists("medicalTests"))
				.andExpect(view().name("medical-tests/medicalTestsList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testIinitCreationForm() throws Exception {
		mockMvc.perform(get("/medical-tests/new")).andExpect(status().isOk())
				.andExpect(model().attributeExists("medicalTest"))
				.andExpect(view().name("medical-tests/createOrUpdateMedicalTestForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFromSuccess() throws Exception {
		mockMvc.perform(post("/medical-tests/new").with(csrf())
				.param("name", "Radiography")
				.param("description","Images of the internal structure of the body to assess the presence of foreign objects, and structural damage or anomaly"))
				.andExpect(status().is3xxRedirection());
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFromHasErrors() throws Exception {
		mockMvc.perform(post("/medical-tests/new").with(csrf())
				.param("name", "Radiography")
				.param("description", ""))
				.andExpect(model().attributeHasErrors("medicalTest"))
				.andExpect(model().attributeHasFieldErrors("medicalTest", "description"))
				.andExpect(view().name("medical-tests/createOrUpdateMedicalTestForm"));
	}
}
