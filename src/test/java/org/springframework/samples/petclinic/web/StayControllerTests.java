package org.springframework.samples.petclinic.web;

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
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = StayController.class,
            excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), 
            excludeAutoConfiguration = SecurityConfiguration.class)
class StayControllerTests {
	
	private static final int TEST_PET_ID = 1;

	private static final int TEST_OWNER_ID = 1;

	@Autowired
	private StayController stayController;

	@MockBean
	private PetService clinicService;

	@Autowired
	private MockMvc mockMvc;
	
	@BeforeEach
	void setup() {
		given(this.clinicService.findPetById(TEST_PET_ID)).willReturn(new Pet());
	}
	
	 @WithMockUser(value = "spring")
     @Test
	void testInitNewStayForm() throws Exception {
		mockMvc.perform(get("/owners/*/pets/{petId}/stances/new", TEST_PET_ID)).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateStayForm"));
	}
	 
	 

	@WithMockUser(value = "spring")
     @Test
	void testShowStances() throws Exception {
		mockMvc.perform(get("/owners/*/pets/{petId}/stances", TEST_PET_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("stances")).andExpect(view().name("stayList"));
	}
	
	
	@WithMockUser(value = "spring")
    @Test
void testProcessNewStayFormSuccess() throws Exception {
	mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/stances/new", TEST_OWNER_ID,TEST_PET_ID)
						.with(csrf())
						.param("registerDate", "2021/02/12")    
                        .param("releaseDate", "2021/03/12"))  
            .andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners/{ownerId}"));
}
	
	
	@WithMockUser(value = "spring")
    @Test
void testProcessNewStayFormHasErrors() throws Exception {
	mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/stances/new", TEST_OWNER_ID,TEST_PET_ID)
						.with(csrf())
                        .param("releaseDate", "2021/03/12"))  
			.andExpect(model().attributeHasErrors("stay")).andExpect(status().isOk())
			.andExpect(view().name("pets/createOrUpdateStayForm"));
}


}
