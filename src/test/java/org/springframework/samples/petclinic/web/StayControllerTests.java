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
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.service.BannerService;
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
	
	private static final int TEST_STAY_ID = 1;

	@MockBean
	private PetService clinicService;
	
	@MockBean
	private BannerService bannerService;

	@Autowired
	private MockMvc mockMvc;
	
	@BeforeEach
	void setup() {
		Stay stay = new Stay();
		Pet pet = new Pet();
		stay.setId(TEST_STAY_ID);
		stay.setRegisterDate(LocalDate.now());
		stay.setReleaseDate(LocalDate.now().plusDays(3));
		pet.addStay(stay);
		given(this.clinicService.findPetById(TEST_PET_ID)).willReturn(pet);
		given(this.clinicService.findStayById(TEST_STAY_ID)).willReturn(stay);
	}
	
	@WithMockUser(value = "spring")
    @Test
	void testListStays() throws Exception {
		mockMvc.perform(get("/owners/*/pets/{petId}/stays", TEST_PET_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("stays"))
				.andExpect(view().name("pets/staysList"));
	}
	
	@WithMockUser(value = "spring")
    @Test
	void testInitNewStayForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/stays/new", TEST_OWNER_ID, TEST_PET_ID))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("stay"))
				.andExpect(view().name("pets/createOrUpdateStayForm"));
	}	
	
	@WithMockUser(value = "spring")
    @Test
	void testProcessNewStayFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/stays/new",TEST_OWNER_ID,TEST_PET_ID)
							.with(csrf())
							.param("registerDate", "2021/02/12")    
	                        .param("releaseDate", "2021/03/12"))  
	            .andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}/pets/{petId}/stays"));
	}
		
	@WithMockUser(value = "spring")
	@Test
	void testProcessNewStayFormHasErrors() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/stays/new", TEST_OWNER_ID,TEST_PET_ID)
							.with(csrf())
	                        .param("releaseDate", "2021/03/12"))  
				.andExpect(model().attributeHasErrors("stay")).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateStayForm"));
	}
		
	@WithMockUser(value = "spring")
	@Test
	void testProcessDeleteStay() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/stays/{stayId}/delete", TEST_OWNER_ID, TEST_PET_ID, TEST_STAY_ID))
		.andExpect(status().isFound())
		.andExpect(view().name("redirect:/owners/{ownerId}/pets/{petId}/stays"));
		
		given(this.clinicService.findStayById(TEST_STAY_ID)).willReturn(null);
	}


}
