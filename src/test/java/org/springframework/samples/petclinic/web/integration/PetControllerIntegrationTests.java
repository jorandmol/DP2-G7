package org.springframework.samples.petclinic.web.integration;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.web.PetController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PetControllerIntegrationTests {
	
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
	private PetController petController;
	
	@Autowired
	private PetService petService;
        
	@Autowired
	private OwnerService ownerService;	
	
	//Usuario que cumple la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testInitCreationForm() throws Exception {
		ModelMap model=new ModelMap();

		String view=petController.initCreationForm(TEST_OWNER_ID1, model);
		
		assertEquals(view, VIEWS_PETS_CREATE_OR_UPDATE_FORM);
		assertNotNull(model.get("pet"));		
	}
	//Usuario que no cumple la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testInitCreationFormWithoutAccess() throws Exception {
		ModelMap model=new ModelMap();

		String view=petController.initCreationForm(TEST_OWNER_ID2, model);
		
		assertEquals(view, REDIRECT_TO_OUPS);
		assertNull(model.get("pet"));		
	}

	
	//Usuario que cumple la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testProcessCreationFormSuccess() throws Exception {
    	ModelMap model=new ModelMap();
    	Pet newPet=new Pet();
    	PetType petType=petService.findPetTypes().iterator().next();
    	newPet.setName("Rositis");
		newPet.setType(petType);
		newPet.setBirthDate(LocalDate.now());    	
		BindingResult bindingResult=new MapBindingResult(Collections.emptyMap(),"");
		
		String view=petController.processCreationForm(TEST_OWNER_ID1, newPet, bindingResult, model);
    	
		assertEquals(view,"redirect:/owner/requests");
	}
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testProcessCreationFormCatchException() throws Exception {
    	ModelMap model=new ModelMap();
    	Pet newPet=new Pet();
    	PetType petType=petService.findPetTypes().iterator().next();
    	newPet.setName("Leo");
		newPet.setType(petType);
		newPet.setBirthDate(LocalDate.now());    	
		BindingResult bindingResult=new MapBindingResult(Collections.emptyMap(),"");
		
		String view=petController.processCreationForm(TEST_OWNER_ID1, newPet, bindingResult, model);
    	
		assertEquals(view, VIEWS_PETS_CREATE_OR_UPDATE_FORM);				
	}

	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testProcessCreationFormHasErrors() throws Exception {
		ModelMap model=new ModelMap();
    	Pet newPet=new Pet();
    	newPet.setName("Ulito");		
		newPet.setBirthDate(LocalDate.now().plusDays(1));    	
		BindingResult bindingResult=new MapBindingResult(new HashMap(),"");
		bindingResult.reject("petType", "Requied!");
		bindingResult.reject("birthDate", "BirthDate must be before or equal to today");
		
		String view=petController.processCreationForm(TEST_OWNER_ID1, newPet, bindingResult, model);
		
		assertEquals(view,"pets/createOrUpdatePetForm");		
	}
	//Usuario que no cumple la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testProcessCreationFormSuccessWithoutAccess() throws Exception {
    	ModelMap model=new ModelMap();
    	Pet newPet=new Pet();
    	PetType petType=petService.findPetTypes().iterator().next();
    	newPet.setName("Rositis");
		newPet.setType(petType);
		newPet.setBirthDate(LocalDate.now());    	
		BindingResult bindingResult=new MapBindingResult(Collections.emptyMap(),"");
		
		String view=petController.processCreationForm(TEST_OWNER_ID2, newPet, bindingResult, model);
    	
		assertEquals(view, REDIRECT_TO_OUPS);				
	}

	
	//Usuario que cumple la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testInitUpdateForm() throws Exception {		
		ModelMap model=new ModelMap();
		
		String view=petController.initUpdateForm(TEST_OWNER_ID1, TEST_PET_ID_1, model);
		
		assertEquals(view,"pets/createOrUpdatePetForm");
		assertNotNull(model.get("pet"));	
		assertNotNull(model.get("owner"));
		assertNotNull(model.get("edit"));
	}
	//Usuario que no cumple la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testInitUpdateFormWithoutAccess() throws Exception {		
		ModelMap model=new ModelMap();
		
		String view=petController.initUpdateForm(TEST_OWNER_ID2, TEST_PET_ID_1, model);
		
		assertEquals(view, REDIRECT_TO_OUPS);
		assertNull(model.get("pet"));	
		assertNull(model.get("owner"));
		assertNull(model.get("edit"));
	}
    	
	
	
	
	//Usuario que cumple la seguridad
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		ModelMap model=new ModelMap();
    	Pet updateSara= new Pet();
    	updateSara.setName("Mini");
    	updateSara.setType(petService.findPetTypes().iterator().next());
    	updateSara.setBirthDate(LocalDate.now());    	
		BindingResult bindingResult=new MapBindingResult(new HashMap(),"");
		
		String view=petController.processUpdateForm(TEST_OWNER_ID2, updateSara, bindingResult, TEST_PET_ID_17, model);
		
		assertEquals(view, "redirect:/owner/pets");
		assertNotNull(model.get("owner"));
		assertNotNull(model.get("edit"));
	}
	
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
	@Test
	void testProcessUpdateFormCatchException() throws Exception {
		ModelMap model=new ModelMap();
    	Pet updateSara= new Pet();
    	updateSara.setName("Nina");
    	updateSara.setType(petService.findPetTypes().iterator().next());
    	updateSara.setBirthDate(LocalDate.now());    	
		BindingResult bindingResult=new MapBindingResult(new HashMap(),"");
		
		String view=petController.processUpdateForm(TEST_OWNER_ID2, updateSara, bindingResult, TEST_PET_ID_17, model);
		
		assertEquals(view, VIEWS_PETS_CREATE_OR_UPDATE_FORM);
		assertNotNull(model.get("owner"));
		assertNotNull(model.get("edit"));
	}
	
	@WithMockUser(username = "admin", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessUpdateFormSuccessAsAdmin() throws Exception {
		ModelMap model=new ModelMap();
    	Pet updateSamantha=this.petService.findPetById(TEST_PET_ID_17);
    	updateSamantha.setName("Uli");
    	updateSamantha.setType(updateSamantha.getType());
    	updateSamantha.setBirthDate(updateSamantha.getBirthDate());    	
		BindingResult bindingResult=new MapBindingResult(new HashMap(),"");
		
		String view=petController.processUpdateForm(TEST_OWNER_ID3, updateSamantha, bindingResult, TEST_PET_ID_7, model);
		
		assertEquals(view, "redirect:/owners/"+ TEST_OWNER_ID3);
		assertNotNull(model.get("owner"));
		assertNotNull(model.get("edit"));
	}
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		ModelMap model=new ModelMap();
    	Pet updateLeo=new Pet();
    	updateLeo.setName("Mini");		
    	updateLeo.setBirthDate(LocalDate.now());    	
		BindingResult bindingResult=new MapBindingResult(new HashMap(),"");
		bindingResult.reject("petType", "Requied!");
		
		String view=petController.processUpdateForm(TEST_OWNER_ID1, updateLeo, bindingResult, TEST_PET_ID_1, model);
		
		assertEquals(view,"pets/createOrUpdatePetForm");
	}
	//Usuario que no cumple la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testProcessUpdateFormWithoutAccess() throws Exception {
		ModelMap model=new ModelMap();
    	Pet updateSamantha=this.petService.findPetById(TEST_PET_ID_17);
    	updateSamantha.setName("Uli");
    	updateSamantha.setType(updateSamantha.getType());
    	updateSamantha.setBirthDate(updateSamantha.getBirthDate());    	
		BindingResult bindingResult=new MapBindingResult(new HashMap(),"");
		
		String view=petController.processUpdateForm(TEST_OWNER_ID3, updateSamantha, bindingResult, TEST_PET_ID_7, model);
		
		assertEquals(view, REDIRECT_TO_OUPS);
	}
	
	
	
	//Usuario que cumple la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testShowPetRequestRejected() {
		ModelMap model=new ModelMap();
		
		String view = this.petController.showAndUpdatePetRequest(TEST_OWNER_ID1, TEST_PET_ID_2, model);
		
		assertEquals(view, "pets/updatePetRequest");
		assertNotNull(model.get("rejected"));
		assertNotNull(model.get("status"));
		assertNotNull(model.get("pet"));
		assertNotNull(model.get("petRequest"));
	}
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testShowPetRequestPending() {
		ModelMap model=new ModelMap();
		
		String view = this.petController.showAndUpdatePetRequest(TEST_OWNER_ID1, TEST_PET_ID_3, model);
		
		assertEquals(view, "pets/updatePetRequest");
		assertNotNull(model.get("status"));
		assertNotNull(model.get("pet"));
		assertNotNull(model.get("petRequest"));
	}
	
	@WithMockUser(username = "admin", password = "4dm1n", authorities = "admin")
	@Test
	void testUpdatePetRequestPending() {
		ModelMap model=new ModelMap();
		
		String view = this.petController.showAndUpdatePetRequest(TEST_OWNER_ID3, TEST_PET_ID_4, model);
		
		assertEquals(view, "pets/updatePetRequest");
		assertNotNull(model.get("status"));
		assertNotNull(model.get("pet"));
		assertNotNull(model.get("petRequest"));
	}
	//Usuario que NO cumple la seguridad
	@WithMockUser(username = "admin", password = "4dm1n", authorities = "admin")
	@Test
	void testUpdatePetRequestNotPending() {
		ModelMap model=new ModelMap();
		
		String view = this.petController.showAndUpdatePetRequest(TEST_OWNER_ID3, TEST_PET_ID_5, model);
		
		assertEquals(view, REDIRECT_TO_OUPS);
	}
	


}

