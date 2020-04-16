package org.springframework.samples.petclinic.web;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.service.BannerService;
import org.springframework.samples.petclinic.service.MedicineService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for the {@link PetController}
 *
 * @author Colin But
 */
@WebMvcTest(value = MedicineController.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
class MedicineControllerTests {

	private static final int TEST_MED_ID = 1;
	
	@MockBean
	private BannerService bannerService;

	@MockBean
	private MedicineService medicineService;
        
	@Autowired
	private MockMvc mockMvc;

	private Medicine med;
	
	@BeforeEach
	void setup() {
		med = new Medicine();
		med.setId(TEST_MED_ID);
		med.setName("Vivapum");
		med.setDescription("Antinflamatorio");
		med.setCode("ABC-123");
		med.setExpirationDate(LocalDate.of(2022, 3, 24));
		given(this.medicineService.findMedicineById(TEST_MED_ID)).willReturn(med);
	}
	
	@WithMockUser(value = "spring")
	    @Test
	void testList() throws Exception {
		mockMvc.perform(get("/medicines")).andExpect(status().isOk())
				.andExpect(view().name("medicines/medicinesList")).andExpect(model().attributeExists("medicines"));
	}

	@WithMockUser(value = "spring")
        @Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/medicines/new")).andExpect(status().isOk())
				.andExpect(view().name("medicines/createOrUpdateMedicineForm")).andExpect(model().attributeExists("medicine"));
	}

	@WithMockUser(value = "spring")
        @Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/medicines/new")
							.with(csrf())
							.param("name", "Virbaninte")
							.param("code", "VET-123")
							.param("expirationDate", "2022/03/12")
							.param("description", "Desparasitante"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/medicines"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/medicines/new")
							.with(csrf())
							.param("name", "Virbaninte")
							.param("code", "123-123")
							.param("expirationDate", "2022/03/12")
							.param("description", "desparasitante"))
				.andExpect(model().attributeHasErrors("medicine"))
				.andExpect(status().isOk())
				.andExpect(view().name("medicines/createOrUpdateMedicineForm"));
	}

	@WithMockUser(value = "spring")
	    @Test
	void testShow() throws Exception {
		mockMvc.perform(get("/medicines/{medicineId}", TEST_MED_ID)).andExpect(status().isOk())
		.andExpect(model().attribute("medicine", hasProperty("name", is("Vivapum"))))
		.andExpect(model().attribute("medicine", hasProperty("description", is("Antinflamatorio"))))
		.andExpect(model().attribute("medicine", hasProperty("code", is("ABC-123"))))
		.andExpect(model().attribute("medicine", hasProperty("expirationDate", is(LocalDate.of(2022, 3, 24)))))
		.andExpect(view().name("medicines/medicineDetails"));
	}
	
}
