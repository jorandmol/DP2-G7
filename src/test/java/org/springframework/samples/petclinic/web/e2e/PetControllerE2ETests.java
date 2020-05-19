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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class PetControllerE2ETests {

	private static final int TEST_OWNER_ID1 = 1;
	private static final int TEST_OWNER_ID2 = 2;
	private static final int TEST_OWNER_ID3 = 3;

	private static final int TEST_PET_ID_1 = 1;
	private static final int TEST_PET_ID_2 = 2;
	private static final int TEST_PET_ID_3 = 3;
	private static final int TEST_PET_ID_4 = 4;
	private static final int TEST_PET_ID_5 = 5;
	private static final int TEST_PET_ID_7 = 7;
	private static final int TEST_PET_ID_17 = 17;

	private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";
	private static final String REDIRECT_TO_OUPS = "redirect:/oups";

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
		mockMvc.perform(get("/owners/{ownerId}/pets/new", TEST_OWNER_ID1))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testInitCreationFormWithoutAccessAdmin() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/new", TEST_OWNER_ID1))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}



	// TEST para usuario que SI cumple la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/new", TEST_OWNER_ID1)
				.with(csrf())
				.param("name", "Betty")
				.param("type", "hamster")
				.param("birthDate", "2015/02/12"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owner/requests"));
	}

	@WithMockUser(username = "owner10", password = "0wn3333r_10", authorities = "owner")
	@Test
	void testProcessCreationFormCatchException() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/new", 10)
				.with(csrf())
				.param("name", "Sly")
				.param("type", "cat")
				.param("birthDate", "2012/06/08"))
				.andExpect(status().isOk())
				.andExpect(view().name(VIEWS_PETS_CREATE_OR_UPDATE_FORM));
	}


	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/new", TEST_OWNER_ID1, TEST_PET_ID_1).with(csrf())
				.param("name", "Betty")
				.param("birthDate", "2015/02/12"))
				.andExpect(model().attributeHasNoErrors("owner"))
				.andExpect(model().attributeHasErrors("pet"))
				.andExpect(model().attributeHasFieldErrors("pet", "type"))
				.andExpect(status().isOk())
				.andExpect(view().name(VIEWS_PETS_CREATE_OR_UPDATE_FORM));
	}

	// TEST para usuarios que NO cumplen la seguridad
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
	@Test
	void testProcessCreationFormSuccessWithoutAccessOwner() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/new", TEST_OWNER_ID1)
				.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
		}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessCreationFormSuccessWithoutAccessAdmin() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/new", TEST_OWNER_ID1)
				.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}




	// TEST para usuarios que SI cumplen la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testInitUpdateMyPetAcceptedAndActiveForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID1, TEST_PET_ID_1))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("pet"))
				.andExpect(view().name(VIEWS_PETS_CREATE_OR_UPDATE_FORM));
	}

	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testInitUpdateMyPetActiveAndRejectedForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID1, TEST_PET_ID_2))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}

	@WithMockUser(username = "owner3", password = "0wn3333r_3", authorities = "owner")
	@Test
	void testInitUpdateMyPetDisabledForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID3, TEST_PET_ID_5))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testInitUpdatePetForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID1, TEST_PET_ID_1))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("pet"))
				.andExpect(view().name(VIEWS_PETS_CREATE_OR_UPDATE_FORM));
	}

	// TEST para usuario que NO cumple la seguridad
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
	@Test
	void testInitUpdatePetOtherOwnerFormWithoutAccess() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID1, TEST_PET_ID_1))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}




	// TEST para usuarios que SI cumplen la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testProcessUpdateMyPetFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID1, TEST_PET_ID_1).with(csrf())
				.param("name", "León")
				.param("type", "cat")
				.param("birthDate", "2010/09/07"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owner/pets"));
	}

	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
	@Test
	void testProcessUpdateMyPetFormCatchException() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID2, TEST_PET_ID_17).with(csrf())
				.param("name", "Nina")
				.param("type", "snake")
				.param("birthDate", "2015/03/24"))
				.andExpect(status().isOk())
				.andExpect(view().name(VIEWS_PETS_CREATE_OR_UPDATE_FORM));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessUpdatePetFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID3, TEST_PET_ID_7).with(csrf())
				.param("name", "Samanthy")
				.param("type", "cat")
				.param("birthDate", "2013/09/04"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owner/pets"));
	}

	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID1, TEST_PET_ID_1).with(csrf())
				.param("type", "cat")
				.param("birthDate", "2015/02/12"))
				.andExpect(model().attributeHasNoErrors("owner"))
				.andExpect(model().attributeHasErrors("pet"))
				.andExpect(status().isOk())
				.andExpect(view().name(VIEWS_PETS_CREATE_OR_UPDATE_FORM));
	}

	// TEST para usuario que NO cumple la seguridad
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
	@Test
	void testProcessUpdateOtherPetFormSuccessWithoutAccess() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID1, TEST_PET_ID_1).with(csrf())
				.param("name", "Leo")
				.param("type", "hamster")
				.param("birthDate", "2015/02/12"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}



	// TEST para usuarios que SI cumplen la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testShowPetRequestPending() throws Exception{
		mockMvc.perform(get("/owners/{ownerId}/pet/{petId}", TEST_OWNER_ID1, TEST_PET_ID_3))
				.andExpect(model().attributeExists("status"))
				.andExpect(model().attributeExists("pet"))
				.andExpect(model().attributeExists("petRequest"))
				.andExpect(status().isOk())
				.andExpect(view().name("pets/updatePetRequest"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testUpdatePetRequestPending() throws Exception{
		mockMvc.perform(get("/owners/{ownerId}/pet/{petId}", 10, 12))
                .andExpect(model().attributeExists("status"))
				.andExpect(model().attributeExists("pet"))
				.andExpect(model().attributeExists("petRequest"))
				.andExpect(status().isOk())
				.andExpect(view().name("pets/updatePetRequest"));
	}

	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testShowPetRequestRejected() throws Exception{
		mockMvc.perform(get("/owners/{ownerId}/pet/{petId}", TEST_OWNER_ID1, TEST_PET_ID_2))
				.andExpect(model().attributeExists("rejected"))
				.andExpect(model().attributeExists("status"))
				.andExpect(model().attributeExists("pet"))
				.andExpect(model().attributeExists("petRequest"))
				.andExpect(status().isOk())
				.andExpect(view().name("pets/updatePetRequest"));
	}

	// TEST para usuario que NO cumple la seguridad
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
	@Test
	void testShowPetRequestWithoutAccess() throws Exception{
		mockMvc.perform(get("/owners/{ownerId}/pet/{petId}", TEST_OWNER_ID1, TEST_PET_ID_2))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}




	// TEST para usuarios que SI cumplen la seguridad
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testAnswerPetRequest() throws Exception{
		mockMvc.perform(post("/owners/{ownerId}/pet/{petId}", TEST_OWNER_ID1, TEST_PET_ID_3).with(csrf())
				.param("status",PetRegistrationStatus.ACCEPTED.toString())
				.param("justification", ""))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/requests"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testAnswerPetRequestHasErrors() throws Exception{
		mockMvc.perform(post("/owners/{ownerId}/pet/{petId}", TEST_OWNER_ID3, TEST_PET_ID_4).with(csrf())
				.param("status",PetRegistrationStatus.REJECTED.toString())
				.param("justification", ""))
				.andExpect(model().attributeExists("status"))
				.andExpect(model().attributeExists("petRequest"))
				.andExpect(status().isOk())
				.andExpect(view().name("pets/updatePetRequest"));
	}

	// TEST para usuario que NO cumple la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testAnswerPetRequestWithoutAccessSecurity() throws Exception{
		mockMvc.perform(post("/owners/{ownerId}/pet/{petId}", TEST_OWNER_ID1, TEST_PET_ID_1).with(csrf())
				.param("status",PetRegistrationStatus.ACCEPTED.toString())
				.param("justification", ""))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testAnswerPetRequestWithoutAccessNotPending() throws Exception{
		mockMvc.perform(post("/owners/{ownerId}/pet/{petId}", TEST_OWNER_ID1, TEST_PET_ID_2).with(csrf())
				.param("status",PetRegistrationStatus.ACCEPTED.toString())
				.param("justification", ""))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}



	// TEST para usuario que SI cumple la seguridad
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testShowPetRequests() throws Exception {
		mockMvc.perform(get("/requests"))
				.andExpect(status().isOk())
				.andExpect(view().name("pets/requests"));
	}

	// TEST para usuario que NO cumple la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testShowPetRequestsWithoutAccess() throws Exception {
		mockMvc.perform(get("/requests"))
				.andExpect(status().is4xxClientError());
	}



	// TEST para usuario que SI cumple la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testShowMyPetRequests() throws Exception{
		mockMvc.perform(get("/owner/requests"))
				.andExpect(status().isOk())
				.andExpect(view().name("pets/myRequests"));
	}

	// TEST para usuario que NO cumple la seguridad
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testShowMyPetRequestsWithoutAccess() throws Exception{
		mockMvc.perform(get("/owner/requests"))
				.andExpect(status().is4xxClientError());
	}




	// TEST para usuario sin pets disabled
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testshowMyPetsActiveWithoutDisabledPets() throws Exception{
		mockMvc.perform(get("/owner/pets"))
				.andExpect(model().attributeExists("owner"))
				.andExpect(model().attributeExists("pets"))
				.andExpect(status().isOk())
				.andExpect(view().name("pets/myPetsActive"));
	}
	//TEST para usuario con pets disabled
	@WithMockUser(username = "owner3", password = "0wn3333r_3", authorities = "owner")
	@Test
	void testshowMyPetsActiveWithDisabledPets() throws Exception{
		mockMvc.perform(get("/owner/pets"))
				.andExpect(model().attributeExists("disabled"))
				.andExpect(model().attributeExists("owner"))
				.andExpect(model().attributeExists("pets"))
				.andExpect(status().isOk())
				.andExpect(view().name("pets/myPetsActive"));
	}
	//TEST para usuario sin acceso
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testshowPetsActiveWithoutAccess() throws Exception{
		mockMvc.perform(get("/owner/pets"))
				.andExpect(status().is4xxClientError());
	}



	// TEST para usuarios que SI cumplen la seguridad
		@WithMockUser(username = "owner3", password = "0wn3333r_3", authorities = "owner")
		@Test
		void showMyPetsDisabled() throws Exception{
			mockMvc.perform(get("/owners/{ownerId}/pets/disabled", TEST_OWNER_ID3))
					.andExpect(model().attributeExists("owner"))
					.andExpect(model().attributeExists("pets"))
					.andExpect(status().isOk())
					.andExpect(view().name("pets/myPetsDisabled"));
		}

		@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
		@Test
		void showOwnerPetsDisabled() throws Exception{
			mockMvc.perform(get("/owners/{ownerId}/pets/disabled", TEST_OWNER_ID3))
					.andExpect(model().attributeExists("owner"))
					.andExpect(model().attributeExists("pets"))
					.andExpect(status().isOk())
					.andExpect(view().name("pets/myPetsDisabled"));
		}

		// TEST para usuario que NO cumple la seguridad
		@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
		@Test
		void showOtherPetsDisabledWithoutAccessAndPetDisabled() throws Exception{
			mockMvc.perform(get("/owners/{ownerId}/pets/disabled", TEST_OWNER_ID2))
					.andExpect(status().is3xxRedirection())
					.andExpect(view().name(REDIRECT_TO_OUPS));
		}

		@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
		@Test
		void showOtherPetsDisabledWithoutAccess() throws Exception{
			mockMvc.perform(get("/owners/{ownerId}/pets/disabled", TEST_OWNER_ID1))
					.andExpect(status().is3xxRedirection())
					.andExpect(view().name(REDIRECT_TO_OUPS));
		}

}
