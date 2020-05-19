package org.springframework.samples.petclinic.web;

/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetRegistrationStatus;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.BannerService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for the {@link PetController}
 *
 * @author Colin But
 */
@WebMvcTest(value = PetController.class, includeFilters = @ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE), excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class PetControllerTests {

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

	@MockBean
	private PetService petService;

	@MockBean
	private OwnerService ownerService;
	
	@MockBean
	private AuthoritiesService authoritiesService;

	@MockBean
	private BannerService bannerService;

	@Autowired
	private MockMvc mockMvc;
	
	private Owner owner1;
	
	private Pet rosy;
	
	private Pet gufy;
	
	private Pet nina;
	
	private Pet sara;
	
	private Pet petWithSameName; 

	@BeforeEach
	void setup() throws DataAccessException, DuplicatedPetNameException {
		
		//OWNER
		owner1 = new Owner();
		owner1.setId(TEST_OWNER_ID1);
		owner1.setFirstName("George");
		owner1.setLastName("Franklin");
		owner1.setAddress("110 W. Liberty St.");
		owner1.setCity("Madison");
		owner1.setTelephone("608555102");
		User user = new User();
		user.setUsername("owner1");
		user.setPassword("0wn3333r_1");
		user.setEnabled(true);
		owner1.setUser(user);
		this.authoritiesService.saveAuthorities("owner1", "owner");
		
		//OWNER Se emplea para los accesos no permitidos por seguridad
		User user2 = new User();
		user2.setUsername("owner2");
		user2.setPassword("0wn3333r_2");
		user2.setEnabled(true);
		Owner owner2 = new Owner();
		owner2.setUser(user2);
		owner2.setId(TEST_OWNER_ID2);
		this.authoritiesService.saveAuthorities("owner2", "owner");
		
		//PET Type
		PetType hamster = new PetType();
		hamster.setId(3);
		hamster.setName("hamster");
		
		PetType dog = new PetType();
		dog.setId(2);
		dog.setName("dog");
		
		//PET
		rosy= new Pet();
		rosy.setId(TEST_PET_ID_1);
		rosy.setBirthDate(LocalDate.now().minusDays(20));
		rosy.setName("Rosy");
		rosy.setType(dog);
		rosy.setActive(false);
		rosy.setStatus(pending);
		
		//PET
		nina= new Pet();
		nina.setId(TEST_PET_ID_2);
		nina.setBirthDate(LocalDate.of(2002, 06, 05));
		nina.setName("Nina");
		nina.setType(dog);
		nina.setActive(true);
		nina.setStatus(rejected);
		nina.setJustification("It is impossible to accept it because the hamster quota has been exceeded");
		
		//PET
		sara= new Pet();
		sara.setId(TEST_PET_ID_3);
		sara.setBirthDate(LocalDate.now().minusDays(20));
		sara.setName("Sara");
		sara.setType(dog);
		sara.setActive(false);
		sara.setStatus(accepted);
		
		//PET
		gufy= new Pet();
		gufy.setId(TEST_PET_ID_4);
		gufy.setBirthDate(LocalDate.of(2004, 11, 12));
		gufy.setName("Gufy");
		gufy.setType(dog);
		gufy.setActive(true);
		gufy.setStatus(accepted);
		
		//PET With same name 
		petWithSameName = new Pet();
		petWithSameName.setId(TEST_PET_ID_5);
		petWithSameName.setBirthDate(LocalDate.now().minusDays(20));
		petWithSameName.setName("Rosy");
		petWithSameName.setType(dog);
		petWithSameName.setActive(true);
		petWithSameName.setStatus(accepted);
		
		//LIST of PET
		List<Pet> petStatusPending= new ArrayList<>();
		petStatusPending.add(rosy);petStatusPending.add(sara);
		
		List<Pet> petStatusPendingAndRejected= new ArrayList<>();
		petStatusPendingAndRejected.add(nina);petStatusPendingAndRejected.add(rosy);petStatusPendingAndRejected.add(sara);
		
		List<Pet> petStatusAcceptedAndActive= new ArrayList<>();
		petStatusAcceptedAndActive.add(gufy);
		
		List<Pet> petStatusAcceptedAndDisable = new ArrayList<>();
		petStatusAcceptedAndDisable.add(sara);
		
		owner1.addPet(gufy);
		owner1.addPet(rosy);
		owner1.addPet(nina);
		owner1.addPet(sara);
		owner1.addPet(petWithSameName);
		
		given(this.petService.findPetTypes()).willReturn(Lists.newArrayList(hamster));
		given(this.ownerService.findOwnerById(TEST_OWNER_ID1)).willReturn(owner1);
		given(this.petService.findPetById(TEST_PET_ID_1)).willReturn(rosy);
		given(this.petService.findPetById(TEST_PET_ID_2)).willReturn(nina);
		given(this.petService.findPetById(TEST_PET_ID_3)).willReturn(sara);
		given(this.petService.findPetById(TEST_PET_ID_4)).willReturn(gufy);
		given(this.petService.findPetById(TEST_PET_ID_5)).willReturn(petWithSameName);
		given(this.petService.findPetsRequests(pending)).willReturn(petStatusPending);
		given(this.petService.findMyPetsRequests(pending, rejected, TEST_OWNER_ID1)).willReturn(petStatusPendingAndRejected);
		given(this.petService.findMyPetsAcceptedByActive(accepted, true, TEST_OWNER_ID1)).willReturn(petStatusAcceptedAndActive);
		given(this.petService.findMyPetsAcceptedByActive(accepted, false, TEST_OWNER_ID1)).willReturn(petStatusAcceptedAndDisable);
		given(this.ownerService.findOwnerByUsername("owner1")).willReturn(owner1);
		given(this.ownerService.findOwnerByUsername("owner2")).willReturn(owner2);
		given(this.petService.countMyPetsAcceptedByActive(accepted, false, TEST_OWNER_ID1)).willReturn(1);
		given(this.petService.countMyPetsAcceptedByActive(accepted, false, TEST_OWNER_ID2)).willReturn(0);
		given(this.petService.petHasStaysOrAppointmentsActive(TEST_PET_ID_4)).willReturn(false);
		given(this.petService.petHasStaysOrAppointmentsActive(TEST_PET_ID_5)).willReturn(true);
		
		Mockito.doThrow(new DuplicatedPetNameException())
		.when(this.petService).savePet(petWithSameName);

	}
	
	
	// TEST para usuario que SI cumple la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/new", TEST_OWNER_ID1))
				.andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdatePetForm"))
				.andExpect(model().attributeExists("pet"))
				.andExpect(model().attributeExists("owner"));
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
	void testProcessCreationFormCatchException() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/new", TEST_OWNER_ID1)
				.with(csrf())
				.flashAttr("pet", petWithSameName))
				.andExpect(status().isOk())
				.andExpect(view().name(VIEWS_PETS_CREATE_OR_UPDATE_FORM));
	}
	
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
				.with(csrf())
				.param("name", "Betty")
				.param("type", "hamster")
				.param("birthDate", "2015/02/12"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessCreationFormSuccessWithoutAccessAdmin() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/new", TEST_OWNER_ID1)
				.with(csrf())
				.param("name", "Betty")
				.param("type", "hamster")
				.param("birthDate", "2015/02/12"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}
	
	
	
	
	// TEST para usuarios que SI cumplen la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testInitUpdateMyPetAcceptedAndActiveForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID1, TEST_PET_ID_4))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("pet"))
				.andExpect(view().name(VIEWS_PETS_CREATE_OR_UPDATE_FORM));
	}
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testInitUpdateMyPetNotActiveAndPendingForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID1, TEST_PET_ID_1))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testInitUpdateMyPetActiveAndRejectedForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID1, TEST_PET_ID_2))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	} 
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testInitUpdateMyPetDisabledForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID1, TEST_PET_ID_3))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testInitUpdatePetForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID1, TEST_PET_ID_4))
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
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID1, TEST_PET_ID_4).with(csrf())
				.param("name", "Betty")
				.param("type", "hamster")
				.param("birthDate", "2015/02/12"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owner/pets"));
	}
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testProcessUpdateMyPetFormCatchException() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID1, TEST_PET_ID_5).with(csrf())
				.flashAttr("pet", petWithSameName))
				.andExpect(status().isOk())
				.andExpect(view().name(VIEWS_PETS_CREATE_OR_UPDATE_FORM));
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessUpdatePetFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID1, TEST_PET_ID_4).with(csrf())
				.param("name", "Betty")
				.param("type", "hamster")
				.param("birthDate", "2015/02/12"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/"+TEST_OWNER_ID1));
	}

	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID1, TEST_PET_ID_4).with(csrf())
				.param("name", "Betty")
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
				.param("name", "Betty")
				.param("type", "hamster")
				.param("birthDate", "2015/02/12"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}
	
	
	
	
	// TEST para usuarios que SI cumplen la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testShowPetRequestPending() throws Exception{
		mockMvc.perform(get("/owners/{ownerId}/pet/{petId}", TEST_OWNER_ID1, TEST_PET_ID_1))
				.andExpect(model().attributeExists("status"))
				.andExpect(model().attributeExists("pet"))
				.andExpect(model().attributeExists("petRequest"))
				.andExpect(status().isOk())
				.andExpect(view().name("pets/updatePetRequest"));
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testUpdatePetRequestPending() throws Exception{
		mockMvc.perform(get("/owners/{ownerId}/pet/{petId}", TEST_OWNER_ID1, TEST_PET_ID_1))
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
		mockMvc.perform(get("/owners/{ownerId}/pet/{petId}", TEST_OWNER_ID1, TEST_PET_ID_1))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}
	
	
	
	
	// TEST para usuarios que SI cumplen la seguridad
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testAnswerPetRequest() throws Exception{
		mockMvc.perform(post("/owners/{ownerId}/pet/{petId}", TEST_OWNER_ID1, TEST_PET_ID_1).with(csrf())
				.param("status",PetRegistrationStatus.ACCEPTED.toString())
				.param("justification", ""))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/requests"));
	}
	
//	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
//	@Test
//	void testAnswerPetRequestThrowsCatchException() throws Exception{
//		mockMvc.perform(post("/owners/{ownerId}/pet/{petId}", TEST_OWNER_ID1, TEST_PET_ID_5).with(csrf())
//				.flashAttr("pet", petWithSameName))
//				.andExpect(status().isOk())
//				.andExpect(view().name("pets/updatePetRequest"));
//	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testAnswerPetRequestHasErrors() throws Exception{
		mockMvc.perform(post("/owners/{ownerId}/pet/{petId}", TEST_OWNER_ID1, TEST_PET_ID_1).with(csrf())
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
	void testAnswerPetRequestWithoutAccess() throws Exception{
		mockMvc.perform(post("/owners/{ownerId}/pet/{petId}", TEST_OWNER_ID1, TEST_PET_ID_1).with(csrf())
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
				.andExpect(model().attributeExists("pets"))
				.andExpect(status().isOk())
				.andExpect(view().name("pets/requests"));
	}
	
	
	
	
	// TEST para usuario que SI cumple la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testShowMyPetRequests() throws Exception{
		mockMvc.perform(get("/owner/requests"))
				.andExpect(model().attributeExists("pets"))
				.andExpect(status().isOk())
				.andExpect(view().name("pets/myRequests"));
	}
	
	

	
	//TEST para usuario con pets disabled
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
	@Test
	void testshowMyPetsActiveWithDisabledPets() throws Exception{
		mockMvc.perform(get("/owner/pets"))
				.andExpect(model().attributeExists("disabled"))
				.andExpect(model().attributeExists("owner"))
				.andExpect(model().attributeExists("pets"))
				.andExpect(status().isOk())
				.andExpect(view().name("pets/myPetsActive"));
	}
	
	
	
	// TEST para usuarios que SI cumplen la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void showMyPetsDisabled() throws Exception{
		mockMvc.perform(get("/owners/{ownerId}/pets/disabled", TEST_OWNER_ID1))
				.andExpect(model().attributeExists("owner"))
				.andExpect(model().attributeExists("pets"))		
				.andExpect(status().isOk())
				.andExpect(view().name("pets/myPetsDisabled"));
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void showOwnerPetsDisabled() throws Exception{
		mockMvc.perform(get("/owners/{ownerId}/pets/disabled", TEST_OWNER_ID1))
				.andExpect(model().attributeExists("owner"))
				.andExpect(model().attributeExists("pets"))
				.andExpect(status().isOk())
				.andExpect(view().name("pets/myPetsDisabled"));
	}
	
	// TEST para usuario que NO cumple la seguridad
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
	@Test
	void showOtherPetsDisabledWithoutAccess() throws Exception{
		mockMvc.perform(get("/owners/{ownerId}/pets/disabled", TEST_OWNER_ID1))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}
	
	// TEST para dar de baja a una mascota
	
	// TEST para usuarios que SI cumplen la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testDisablePetStaysOrAppointmentsInactive() throws Exception{
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/disable", TEST_OWNER_ID1, TEST_PET_ID_4))
				.andExpect(model().attributeDoesNotExist("errorDisabled"))	
				.andExpect(status().isOk())
				.andExpect(view().name("pets/myPetsActive"));
	}
	
	// TEST para usuarios que SI cumplen la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testDisablePetStaysOrAppointmentsActive() throws Exception{
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/disable", TEST_OWNER_ID1, TEST_PET_ID_5))
				.andExpect(model().attributeExists("errorDisabled"))	
				.andExpect(status().isOk())
				.andExpect(view().name("pets/myPetsActive"));
	}
	
	// TEST para usuarios que NO cumplen la seguridad
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
	@Test
	void testDisablePetStaysOrAppointmentsWithoutAccess() throws Exception{
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/disable", TEST_OWNER_ID1, TEST_PET_ID_1))	
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}
}
