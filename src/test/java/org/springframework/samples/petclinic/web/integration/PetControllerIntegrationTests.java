package org.springframework.samples.petclinic.web.integration;

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
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testInitCreationForm() throws Exception {
		ModelMap model=new ModelMap();

		String view=petController.initCreationForm(TEST_OWNER_ID1, model);
		
		assertEquals(view,"pets/createOrUpdatePetForm");
		assertNotNull(model.get("pet"));		
	}

	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testProcessCreationFormSuccess() throws Exception {
    	ModelMap model=new ModelMap();
    	Owner owner=ownerService.findOwnerById(TEST_OWNER_ID1);
    	Pet newPet=new Pet();
    	PetType petType=petService.findPetTypes().iterator().next();
    	newPet.setName("Betty");
		newPet.setType(petType);
		newPet.setBirthDate(LocalDate.now());    	
		BindingResult bindingResult=new MapBindingResult(Collections.emptyMap(),"");
		
		String view=petController.processCreationForm(TEST_OWNER_ID1, newPet, bindingResult, model);
    	
		assertEquals(view,"redirect:/owner/requests");				
	}

	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testProcessCreationFormHasErrors() throws Exception {
		ModelMap model=new ModelMap();
    	Owner owner=ownerService.findOwnerById(TEST_OWNER_ID1);
    	Pet newPet=new Pet();
    	PetType petType=petService.findPetTypes().iterator().next();
    	newPet.setName("Betty");		
		newPet.setBirthDate(LocalDate.now());    	
		BindingResult bindingResult=new MapBindingResult(new HashMap(),"");
		bindingResult.reject("petType", "Requied!");
		
		String view=petController.processCreationForm(TEST_OWNER_ID1, newPet, bindingResult, model);
		
		assertEquals(view,"pets/createOrUpdatePetForm");		
	}

	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testInitUpdateForm() throws Exception {		
		ModelMap model=new ModelMap();
		
		String view=petController.initUpdateForm(TEST_OWNER_ID1, TEST_PET_ID_1, model);
		
		assertEquals(view,"pets/createOrUpdatePetForm");
		assertNotNull(model.get("pet"));						
	}
    
    	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		ModelMap model=new ModelMap();
    	Owner owner=ownerService.findOwnerById(TEST_OWNER_ID1);
    	Pet newPet=new Pet();
    	PetType petType=petService.findPetTypes().iterator().next();
    	newPet.setName("Betty");		
		newPet.setBirthDate(LocalDate.now());    	
		BindingResult bindingResult=new MapBindingResult(new HashMap(),"");
		bindingResult.reject("petType", "Requied!");
		
		String view=petController.processUpdateForm(TEST_OWNER_ID1, newPet, bindingResult, TEST_PET_ID_1, model);
		
		assertEquals(view,"pets/createOrUpdatePetForm");
	}

}

