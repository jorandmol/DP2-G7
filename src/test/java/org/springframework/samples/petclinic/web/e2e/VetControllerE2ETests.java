package org.springframework.samples.petclinic.web.e2e;

import static org.hamcrest.xml.HasXPath.hasXPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class VetControllerE2ETests {
	
	private static final int TEST_VET_ID_1 = 1;
	private static final int TEST_VET_ID_2 = 2;
	private static final int TEST_VET_ID_3 = 3;
	private static final int TEST_VET_ID_5 = 5;
	
	private static final Integer TEST_PET_ID_1 = 1;
	private static final Integer TEST_PET_ID_2 = 2;
	private static final Integer TEST_PET_ID_3 = 3;
	
	private static final String REDIRECT_TO_OUPS = "redirect:/oups";
	private static final String VIEWS_VET_CREATE_OR_UPDATE_FORM = "vets/createOrUpdateVetForm";

	
	@Autowired
	private MockMvc mockMvc;

	
	// TEST para usuario que SI cumple la seguridad
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testShowVetList() throws Exception {
		mockMvc.perform(get("/vets"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("vets"))
				.andExpect(view().name("vets/vetList"));
	}
	
	// TEST para usuario que NO cumple la seguridad
	@WithMockUser(username = "vet1", password = "veter1n4ri0_1", authorities = "veterinarian")
	@Test
	void testShowVetListWithoutAccess() throws Exception {
		mockMvc.perform(get("/vets"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}

	
	// TEST para usuario que SI cumple la seguridad
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/vets/new"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("vet"))
				.andExpect(view().name(VIEWS_VET_CREATE_OR_UPDATE_FORM));
	}

	// TEST para usuario que NO cumple la seguridad
	@WithMockUser(username = "vet1", password = "veter1n4ri0_1", authorities = "veterinarian")
	@Test
	void testInitCreationFormWithoutAccess() throws Exception {
		mockMvc.perform(get("/vets/new"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}

	

	// TEST para usuario que SI cumple la seguridad
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/vets/new").with(csrf())
				.param("firstName", "Jorge")
				.param("lastName", "Martín")
				.param("city", "Cáceres")
				.param("address", "C/Almendralejo, 44")
				.param("telephone", "636211578")
				.param("user.username", "yorch06")
				.param("user.password", "jcveterin_06"))
				.andExpect(status().is3xxRedirection());
	}
	
}
