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
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetRegistrationStatus;
import org.springframework.samples.petclinic.model.Status;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.BannerService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.StayService;
import org.springframework.samples.petclinic.service.exceptions.DateNotAllowed;
import org.springframework.samples.petclinic.service.exceptions.MaximumStaysReached;
import org.springframework.samples.petclinic.service.exceptions.StayAlreadyConfirmed;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = StayController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class StayControllerTests {
	
	private static final int TEST_PET_ID = 1;

	private static final int TEST_OWNER_ID = 1;
	
	private static final int TEST_WRONG_OWNER_ID = 2;
	
	private static final int TEST_STAY_ID = 1;
	
	private static final int TEST_STAY_ID_CONFIRMED = 2;
	
	private static final int TEST_STAY_ID_EXCEPTIONS = 3;


	@MockBean
	private PetService petService;
	
	@MockBean
	private StayService stayService;
	
	@MockBean
	private OwnerService ownerService;
	
	@MockBean
	private BannerService bannerService;

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private AuthoritiesService authoritiesService;
	
	private Stay stay1;
	private Stay stay2;
	private Stay stay3;
	private Stay stay4;
	private Stay stay5;

	
	@BeforeEach
	void setup() throws MaximumStaysReached, StayAlreadyConfirmed, DateNotAllowed {
		MockitoAnnotations.initMocks(this);
		stay1 = new Stay();
		stay2 = new Stay();
		stay3 = new Stay();
		stay4 = new Stay();
		stay5 = new Stay();
		List<Stay> stays = new ArrayList<Stay>();
		Pet pet = new Pet();
		pet.setId(TEST_PET_ID);
		pet.setActive(true);
		pet.setStatus(PetRegistrationStatus.ACCEPTED);
		
		//Owner1 - Para la seguridad
		User user1 = new User();
		user1.setEnabled(true);
		user1.setUsername("owner1");
		Owner owner1 = new Owner();
		owner1.setUser(user1);
		owner1.setId(TEST_OWNER_ID);
		this.authoritiesService.saveAuthorities("owner1", "owner");
		owner1.addPet(pet);
		
		//Owner2 - Para la seguridad
		User user2 = new User();
		user2.setEnabled(true);
		user2.setUsername("owner2");
		Owner owner2 = new Owner();
		owner2.setUser(user2);
		owner2.setId(TEST_WRONG_OWNER_ID);
		this.authoritiesService.saveAuthorities("owner2", "owner");
		
		stay1.setId(TEST_STAY_ID);
		stay1.setRegisterDate(LocalDate.now());
		stay1.setReleaseDate(LocalDate.now().plusDays(3));
		stay1.setStatus(Status.PENDING);
		pet.addStay(stay1);
		
		stay2.setId(TEST_STAY_ID_CONFIRMED);
		stay2.setRegisterDate(LocalDate.now());
		stay2.setReleaseDate(LocalDate.now().plusDays(3));
		stay2.setStatus(Status.ACCEPTED);
		pet.addStay(stay2);
		
		stay3.setId(TEST_STAY_ID_EXCEPTIONS);
		stay3.setRegisterDate(LocalDate.now());
		stay3.setReleaseDate(LocalDate.now().plusDays(3));
		stay3.setStatus(Status.PENDING);
		pet.addStay(stay3);
		
		given(this.ownerService.findOwnerById(TEST_OWNER_ID)).willReturn(owner1);
		given(this.ownerService.findOwnerById(TEST_WRONG_OWNER_ID)).willReturn(owner2);
		given(this.petService.findPetById(TEST_PET_ID)).willReturn(pet);
		given(this.stayService.findStayById(TEST_STAY_ID)).willReturn(stay1);
		given(this.stayService.findStayById(TEST_STAY_ID_CONFIRMED)).willReturn(stay2);
		given(this.stayService.findStayById(TEST_STAY_ID_EXCEPTIONS)).willReturn(stay3);
		given(this.stayService.findStaysByPetId(TEST_PET_ID)).willReturn(stays);
		given(this.stayService.findAllStays()).willReturn(stays);	
		
		Mockito.doThrow(MaximumStaysReached.class).when(this.stayService).saveStay(stay3);
		Mockito.doThrow(StayAlreadyConfirmed.class).when(this.stayService).deleteStay(stay3);
		Mockito.doThrow(StayAlreadyConfirmed.class).when(this.stayService).editStay(stay3);
		Mockito.doThrow(MaximumStaysReached.class).when(this.stayService).editStay(stay4);
		Mockito.doThrow(DateNotAllowed.class).when(this.stayService).editStay(stay5);
		Mockito.doThrow(StayAlreadyConfirmed.class).when(this.stayService).editStatus(stay3);
		
	}
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testListStays() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/stays", TEST_OWNER_ID, TEST_PET_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("stays"))
				.andExpect(view().name("pets/staysList"));
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
    @Test
	void testListStaysAdmin() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/stays", TEST_OWNER_ID, TEST_PET_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("stays"))
				.andExpect(view().name("pets/staysList"));
	}
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testListStaysWrongOwner() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/stays", TEST_WRONG_OWNER_ID, TEST_PET_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testInitNewStayForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/stays/new", TEST_OWNER_ID, TEST_PET_ID))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("stay"))
				.andExpect(view().name("pets/createOrUpdateStayForm"));
	}	
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testInitNewStayFormWrongOwner() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/stays/new", TEST_WRONG_OWNER_ID, TEST_PET_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}	
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testProcessNewStayFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/stays/new",TEST_OWNER_ID,TEST_PET_ID)
							.with(csrf())
							.param("registerDate", "2021/02/12")    
	                        .param("releaseDate", "2021/03/12"))  
	            .andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}/pets/{petId}/stays"));
	}
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testProcessNewStayFormWrongUser() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/stays/new",TEST_WRONG_OWNER_ID,TEST_PET_ID)
							.with(csrf())
							.param("registerDate", "2021/02/12")    
	                        .param("releaseDate", "2021/03/12"))  
	            .andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
		
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testProcessNewStayFormHasErrors() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/stays/new", TEST_OWNER_ID,TEST_PET_ID)
							.with(csrf())
	                        .param("releaseDate", "2021/03/12"))  
				.andExpect(model().attributeHasErrors("stay")).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateStayForm"));
	}
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testProcessNewStayFormHasErrorsMaximumStaysReached() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/stays/new", TEST_OWNER_ID,TEST_PET_ID)
							.with(csrf())
	                        .flashAttr("stay", stay3))  
				.andExpect(model().attributeHasErrors("stay")).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateStayForm"));
	}
		
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testProcessDeleteStaySuccess() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/stays/{stayId}/delete", TEST_OWNER_ID, TEST_PET_ID, TEST_STAY_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("pets/staysList"));
				
				given(this.stayService.findStayById(TEST_STAY_ID)).willReturn(null);
	}
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testProcessDeleteStayWrongUser() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/stays/{stayId}/delete", TEST_WRONG_OWNER_ID, TEST_PET_ID, TEST_STAY_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("exception"));
	}
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testProcessDeleteStayErrorAlreadyConfirmed() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/stays/{stayId}/delete", TEST_OWNER_ID, TEST_PET_ID, TEST_STAY_ID_EXCEPTIONS))
				.andExpect(status().isOk())
				.andExpect(view().name("pets/staysList"));
	}

	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testInitEditStayForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/stays/{stayId}/edit", TEST_OWNER_ID, TEST_PET_ID, TEST_STAY_ID))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("stay"))
				.andExpect(view().name("pets/createOrUpdateStayForm"));
	}	
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testInitEditStayFormWrongOwner() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/stays/{stayId}/edit", TEST_WRONG_OWNER_ID, TEST_PET_ID,TEST_STAY_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}	
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testProcessEditStayFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/stays/{stayId}/edit",TEST_OWNER_ID,TEST_PET_ID,TEST_STAY_ID)
							.with(csrf())
							.param("registerDate", "2021/02/12")    
	                        .param("releaseDate", "2021/03/12"))  
	            .andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}/pets/{petId}/stays"));
	}
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testProcessNewEditFormWrongUser() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/stays/{stayId}/edit",TEST_WRONG_OWNER_ID,TEST_PET_ID,TEST_STAY_ID)
							.with(csrf())
							.param("registerDate", "2021/02/12")    
	                        .param("releaseDate", "2021/03/12"))  
	            .andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
		
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testProcessEditStayFormHasErrors() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/stays/{stayId}/edit",TEST_OWNER_ID,TEST_PET_ID,TEST_STAY_ID)
							.with(csrf())
	                        .param("releaseDate", "2021/03/12"))  
				.andExpect(model().attributeHasErrors("stay")).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateStayForm"));
	}
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testProcessEditStayFormHasAlreadyConfirmed() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/stays/{stayId}/edit",TEST_OWNER_ID,TEST_PET_ID,TEST_STAY_ID)
							.with(csrf())
							.flashAttr("stay", stay3))  
				.andExpect(model().attributeHasFieldErrors("stay", "releaseDate")).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateStayForm"));
	}
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testProcessEditStayFormHasMaximumStaysReached() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/stays/{stayId}/edit",TEST_OWNER_ID,TEST_PET_ID,TEST_STAY_ID)
							.with(csrf())
							.flashAttr("stay", stay4))   
				.andExpect(model().attributeHasFieldErrors("stay", "releaseDate")).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateStayForm"));
	}
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testProcessEditStayFormHasDateNotAllowed() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/stays/{stayId}/edit",TEST_OWNER_ID,TEST_PET_ID,TEST_STAY_ID)
							.with(csrf())
							 .flashAttr("stay", stay5))  
				.andExpect(model().attributeHasFieldErrors("stay", "releaseDate")).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateStayForm"));
	}
	
	// Admin confirms or rejects stays
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
    @Test
	void testListStaysAdminConfirmOrReject() throws Exception {
		mockMvc.perform(get("/admin/stays")).andExpect(status().isOk())
				.andExpect(model().attributeExists("stays"))
				.andExpect(view().name("pets/staysListAdmin"));
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
    @Test
	void testInitEditStatusStayFormAdmin() throws Exception {
		mockMvc.perform(get("/admin/stays/{stayId}", TEST_STAY_ID))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("stay"))
				.andExpect(view().name("pets/createOrUpdateStayFormAdmin"));
	}	
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
    @Test
	void testProcessEditStatusStayFormAdminSuccess() throws Exception {
		mockMvc.perform(post("/admin/stays/{stayId}", TEST_STAY_ID)
							.with(csrf())
							.param("registerDate", "2021/02/12")    
	                        .param("releaseDate", "2021/03/12")
							.param("status", "ACCEPTED"))
	            .andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/admin/stays"));
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
    @Test
	void testProcessEditStatusStayFormAdminErrors() throws Exception {
		mockMvc.perform(post("/admin/stays/{stayId}", TEST_STAY_ID_CONFIRMED)
							.with(csrf())
	                        .param("status", "ACCEPTED"))
	            .andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateStayFormAdmin"));
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
    @Test
	void testProcessEditStatusStayFormAdminAlreadyConfirmed() throws Exception {
		mockMvc.perform(post("/admin/stays/{stayId}", TEST_STAY_ID_EXCEPTIONS)
							.with(csrf())
	                        .flashAttr("stay", stay3))
	            .andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateStayFormAdmin"));
	}
	
}
