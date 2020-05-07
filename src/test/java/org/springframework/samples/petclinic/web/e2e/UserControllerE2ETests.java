package org.springframework.samples.petclinic.web.e2e;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class UserControllerE2ETests {
	
	private static final int TEST_OWNER_ID = 1;
	
	private static final int TEST_VET_ID = 1;
	
	@Autowired
	private MockMvc mockMvc;
	
	//Usuario con acceso permitido a su perfil
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testFindUserProfileOwner() throws Exception {	
		mockMvc.perform(get("/users/profile")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/" + TEST_OWNER_ID));
	}
		
	//Usuario con acceso permitido a su perfil
	@WithMockUser(username = "vet1", password = "V3t_erinarian", authorities = "veterinarian")
	@Test
	void testFindUserProfileVet() throws Exception {		
		mockMvc.perform(get("/users/profile")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/vets/" + TEST_VET_ID));
	}
		
	//Usuario con acceso NO permitido al perfil
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testFindUserProfileVetWithoutAccess() throws Exception {	
		mockMvc.perform(get("/users/profile"))
				.andExpect(status().is4xxClientError());
	}
}
