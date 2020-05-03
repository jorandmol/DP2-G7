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
import org.springframework.samples.petclinic.model.PetRegistrationStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class PetControllerE2ETests {

	private static final int TEST_OWNER_ID1 = 1;

	private static final int TEST_OWNER_ID2 = 2;

	private static final int TEST_PET_ID_1 = 1;

	private static final int TEST_PET_ID_2 = 2;

	private static final int TEST_PET_ID_3 = 3;

	private static final int TEST_PET_ID_4 = 4;

	private static final int TEST_PET_ID_5 = 5;

	private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";

	private static final String REDIRECT_TO_OUPS = "redirect:/oups";

	private static final PetRegistrationStatus accepted = PetRegistrationStatus.ACCEPTED;

	private static final PetRegistrationStatus pending = PetRegistrationStatus.PENDING;

	private static final PetRegistrationStatus rejected = PetRegistrationStatus.REJECTED;

	@Autowired
	private MockMvc mockMvc;

	// TEST para usuario que SI cumple la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/new", TEST_OWNER_ID1))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("pet"))
				.andExpect(model().attributeExists("owner"))
				.andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	// TEST para usuarios que NO cumplen la seguridad
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
	@Test
	void testInitCreationFormWithoutAccessOwner() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/new", TEST_OWNER_ID1)).andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testInitCreationFormWithoutAccessAdmin() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/new", TEST_OWNER_ID1)).andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}

	
}
