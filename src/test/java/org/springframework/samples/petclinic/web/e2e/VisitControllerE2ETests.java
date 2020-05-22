package org.springframework.samples.petclinic.web.e2e;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
class VisitControllerE2ETests {

	private static final int TEST_PET_ID_1 = 1;
	
	private static final int TEST_PET_ID_2 = 2;

	private static final int TEST_PET_ID_3 = 15;
	
	private static final int TEST_PET_ID_4 = 18;

	private static final int TEST_OWNER_ID_1 = 1;
	
	private static final int TEST_VISIT_ID_1 = 1;

	private static final int TEST_VISIT_ID_2 = 5;

	@Autowired
	private MockMvc mockMvc;

    @WithMockUser(username="vet1", password="v3terinarian_1", authorities="veterinarian")
    @Test
	void testInitNewVisitFormSuccess() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/visits/new", 4, TEST_PET_ID_3))
				.andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateVisitForm"));
	}

	@WithMockUser(username="vet2", password="v3terinarian_2", authorities="veterinarian")
    @Test
	void testProcessNewVisitFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID_1, TEST_PET_ID_1)
				.param("name", "George").with(csrf())
				.param("description", "Visit Description"))                                
                .andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/appointments"));
	}

	@WithMockUser(username="vet2", password="v3terinarian_2", authorities="veterinarian")
    @Test
	void testProcessNewVisitFormHasErrors() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID_1, TEST_PET_ID_1)
							.with(csrf())
							.param("name", "George")
							.param("description", ""))
				.andExpect(model().attributeHasErrors("visit")).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateVisitForm"));
	}

	@WithMockUser(username="owner1", password="0wn3333r_1", authorities="owner")
    @Test
	void testShowVisitSuccessWithOwnerUser() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/visits/{visitId}", TEST_OWNER_ID_1, TEST_PET_ID_2, TEST_VISIT_ID_1))
				.andExpect(status().isOk())
				.andExpect(view().name("visits/visitDetails"));
	}
	
	@WithMockUser(username="admin1", password="4dm1n", authorities="admin")
    @Test
	void testShowVisitSuccessWithAdminUser() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/visits/{visitId}", TEST_OWNER_ID_1, TEST_PET_ID_2, TEST_VISIT_ID_1))
				.andExpect(status().isOk())
				.andExpect(view().name("visits/visitDetails"));
	}
	
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities="veterinarian")
    @Test
	void testShowVisitWithoutAuthorities() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/visits/{visitId}", TEST_OWNER_ID_1, TEST_PET_ID_1, TEST_VISIT_ID_2))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
	@WithMockUser(username="vet1", password="v3terinarian_1", authorities="veterinarian")
    @Test
	void testShowVisitWithoutAuthoritiesOrUsername() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/visits/{visitId}", TEST_OWNER_ID_1, TEST_PET_ID_2, TEST_VISIT_ID_2))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
	@WithMockUser(username="owner2", password="0wn3333r_2", authorities="owner")
	@Test
	void testShowVisitWithWrongOwner() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/visits/{visitId}", TEST_OWNER_ID_1, TEST_PET_ID_2, TEST_VISIT_ID_1))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}

}
