package org.springframework.samples.petclinic.web.e2e;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
  webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
/*@TestPropertySource(
  locations = "classpath:application-mysql.properties")*/
class StayControllerE2ETests {
	
	private static final int TEST_PET_ID = 1;
	
	private static final int TEST_PET_ID_2 = 2;

	private static final int TEST_OWNER_ID = 1;
	
	private static final int TEST_WRONG_OWNER_ID = 2;
	
	private static final int TEST_STAY_ID_PENDING = 2;
	
	private static final int TEST_STAY_ID_CONFIRMED = 1;
	
	@Autowired
	private MockMvc mockMvc;
	
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
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/stays", TEST_WRONG_OWNER_ID, TEST_PET_ID_2))
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
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/stays/new", TEST_WRONG_OWNER_ID, TEST_PET_ID_2))
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
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/stays/new",TEST_WRONG_OWNER_ID,TEST_PET_ID_2)
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
	void testProcessNewStayFormHasErrorsSameDate() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/stays/new", TEST_OWNER_ID,TEST_PET_ID)
							.with(csrf())
							.param("registerDate", "2020/10/02")    
	                        .param("releaseDate", "2020/10/04")) 
				.andExpect(model().attributeHasErrors("stay")).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateStayForm"));
	}
		
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
	@Test
	void testProcessDeleteStaySuccess() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/stays/{stayId}/delete", TEST_WRONG_OWNER_ID, TEST_PET_ID_2, TEST_STAY_ID_PENDING))
				.andExpect(status().isOk())
				.andExpect(view().name("pets/staysList"));
				
	}
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testProcessDeleteStayWrongUser() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/stays/{stayId}/delete", TEST_WRONG_OWNER_ID, TEST_PET_ID_2, TEST_STAY_ID_PENDING))
				.andExpect(status().isOk())
				.andExpect(view().name("exception"));
	}
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testProcessDeleteStayErrorsConfirmed() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/stays/{stayId}/delete", TEST_OWNER_ID, TEST_PET_ID, TEST_STAY_ID_CONFIRMED))
				.andExpect(status().isOk())
				.andExpect(view().name("pets/staysList"));
				
	}

	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
    @Test
	void testInitEditStayForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/stays/{stayId}/edit", TEST_WRONG_OWNER_ID, TEST_PET_ID_2, TEST_STAY_ID_PENDING))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("stay"))
				.andExpect(view().name("pets/createOrUpdateStayForm"));
	}	
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testInitEditStayFormWrongOwner() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/stays/{stayId}/edit", TEST_WRONG_OWNER_ID, TEST_PET_ID_2,TEST_STAY_ID_PENDING))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}	
	
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
    @Test
	void testProcessEditStayFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/stays/{stayId}/edit",TEST_WRONG_OWNER_ID,TEST_PET_ID_2,TEST_STAY_ID_PENDING)
							.with(csrf())
							.param("registerDate", "2021/02/12")    
	                        .param("releaseDate", "2021/02/21"))  
	            .andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}/pets/{petId}/stays"));
	}
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testProcessNewEditFormWrongUser() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/stays/{stayId}/edit",TEST_WRONG_OWNER_ID,TEST_PET_ID_2,TEST_STAY_ID_PENDING)
							.with(csrf())
							.param("registerDate", "2021/02/12")    
	                        .param("releaseDate", "2021/03/12"))  
	            .andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
		
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
	@Test
	void testProcessEditStayFormHasErrors() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/stays/{stayId}/edit",TEST_WRONG_OWNER_ID,TEST_PET_ID_2,TEST_STAY_ID_PENDING)
							.with(csrf())
	                        .param("releaseDate", "2021/03/12"))  
				.andExpect(model().attributeHasErrors("stay")).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateStayForm"));
	}
	
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
	@Test
	void testProcessEditStayFormHasErrorsDateNotAllowed() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/stays/{stayId}/edit",TEST_WRONG_OWNER_ID,TEST_PET_ID_2,TEST_STAY_ID_PENDING)
							.with(csrf())
							.param("registerDate", "2020/10/01")    
	                        .param("releaseDate", "2020/10/05")) 
				.andExpect(model().attributeHasErrors("stay")).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateStayForm"));
	}
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testProcessEditStayFormHasErrorsAlreadyConfirmed() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/stays/{stayId}/edit",TEST_OWNER_ID,TEST_PET_ID,TEST_STAY_ID_CONFIRMED)
							.with(csrf())
							.param("registerDate", "2021/02/12")    
	                        .param("releaseDate", "2021/04/12"))
				.andExpect(model().attributeHasErrors("stay")).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateStayForm"));
	}
	
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
	@Test
	void testProcessEditStayFormHasErrorsMaximumStaysReached() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/stays/{stayId}/edit",TEST_WRONG_OWNER_ID,TEST_PET_ID_2,TEST_STAY_ID_PENDING)
							.with(csrf())
							.param("registerDate", "2020/11/02")    
	                        .param("releaseDate", "2020/11/03"))
				.andExpect(model().attributeHasErrors("stay")).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateStayForm"));
	}
	
	// -------------------------------------------------------
	// Admin confirms or rejects stays
	// -------------------------------------------------------
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
    @Test
	void testListStaysAdminConfirmOrReject() throws Exception {
		mockMvc.perform(get("/admin/stays")).andExpect(status().isOk())
				.andExpect(model().attributeExists("stays"))
				.andExpect(view().name("pets/staysListAdmin"));
	}
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testListStaysAdminConfirmOrRejectWrongUser() throws Exception {
		mockMvc.perform(get("/admin/stays"))
				.andExpect(status().is4xxClientError());
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
    @Test
	void testInitEditStatusStayFormAdmin() throws Exception {
		mockMvc.perform(get("/admin/stays/{stayId}", TEST_STAY_ID_PENDING))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("stay"))
				.andExpect(view().name("pets/createOrUpdateStayFormAdmin"));
	}	
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testInitEditStatusStayFormAdminWrongUser() throws Exception {
		mockMvc.perform(get("/admin/stays/{stayId}", TEST_STAY_ID_PENDING))
				.andExpect(status().is4xxClientError());
	}	
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
    @Test
	void testProcessEditStatusStayFormAdminSuccess() throws Exception {
		mockMvc.perform(post("/admin/stays/{stayId}", TEST_STAY_ID_PENDING)
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
		mockMvc.perform(post("/admin/stays/{stayId}", TEST_STAY_ID_PENDING)
							.with(csrf())
							.param("registerDate", "2021/02/12")    
	                        .param("releaseDate", ""))
	            .andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateStayFormAdmin"));
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
    @Test
	void testProcessEditStatusStayFormAdminErrorsAlreadyConfirmed() throws Exception {
		mockMvc.perform(post("/admin/stays/{stayId}", TEST_STAY_ID_CONFIRMED)
							.with(csrf())
							.param("registerDate", "2021/02/12")    
	                        .param("releaseDate", "2021/03/12")
	                        .param("status", "REJECTED"))
	            .andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateStayFormAdmin"));
	}
	
}
