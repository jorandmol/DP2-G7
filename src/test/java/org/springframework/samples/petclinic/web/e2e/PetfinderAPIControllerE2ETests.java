package org.springframework.samples.petclinic.web.e2e;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
public class PetfinderAPIControllerE2ETests {
	
	private static final String OWNER_ROLE = "owner";

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	public void testInitAdoptionsSearchForm() throws Exception {
		mockMvc.perform(get("/adoptions"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("types"))
			.andExpect(view().name("adoptions/adoptionsSearchForm"));
	}
	
	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	public void testProcessAdoptionsSearch() throws Exception {
		mockMvc.perform(get("/adoptions/find")
				.param("type", "cat")
				.param("size", "small")
				.param("gender", "male"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("animals"))
			.andExpect(view().name("adoptions/adoptionsListResult"));
	}
}
