package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.PetTypeService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = PetTypeController.class, includeFilters = @ComponentScan.Filter(value = PetType.class, type = FilterType.ASSIGNABLE_TYPE), excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class PetTypeControllerTests {

	private static final int TEST_PET_TYPE_ID = 1;

	@Autowired
	private PetTypeController petTypeController;

	@MockBean
	private PetTypeService petTypeService;

	@Autowired
	private MockMvc mockMvc;
	
	private PetType petType;
	
	@BeforeEach
	void setup() {
		petType.setId(TEST_PET_TYPE_ID);
		petType.setName("Shark");
		given(this.petTypeService.findById(TEST_PET_TYPE_ID).get()).willReturn(petType);
	}


	@WithMockUser(value = "spring")
    @Test
void testList() throws Exception {
	mockMvc.perform(get("/pet-type")).andExpect(status().isOk())
			.andExpect(view().name("pet-type/typeList")).andExpect(model().attributeExists("petTypes"));
}



}
