package org.springframework.samples.petclinic.web.integration;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetRegistrationStatus;
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
	
	private ModelMap model = new ModelMap();
	private BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
	
	
	@Autowired
	private PetController petController;
	
	@Autowired
	private PetService petService;
	
	//Usuario que cumple la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testInitCreationForm() throws Exception {

		String view = petController.initCreationForm(TEST_OWNER_ID1, model);
		
		assertEquals(view, VIEWS_PETS_CREATE_OR_UPDATE_FORM);
		assertNotNull(model.get("pet"));		
	}
	//Usuario que no cumple la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testInitCreationFormWithoutAccess() throws Exception {

		String view = petController.initCreationForm(TEST_OWNER_ID2, model);
		
		assertEquals(view, REDIRECT_TO_OUPS);
		assertNull(model.get("pet"));		
	}

	
	//Usuario que cumple la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testProcessCreationFormSuccess() throws Exception {
    	Pet newPet=new Pet();
    	newPet.setName("Rositis");
		newPet.setType(petService.findPetTypes().iterator().next());
		newPet.setBirthDate(LocalDate.now());    	
		
		String view=petController.processCreationForm(TEST_OWNER_ID1, newPet, result, model);
    	
		assertEquals(view,"redirect:/owner/requests");
	}
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testProcessCreationFormCatchException() throws Exception {
    	Pet newPet=new Pet();
    	newPet.setName("Leo");
		newPet.setType(petService.findPetTypes().iterator().next());
		newPet.setBirthDate(LocalDate.now());    	
		
		String view=petController.processCreationForm(TEST_OWNER_ID1, newPet, result, model);
    	
		assertEquals(view, VIEWS_PETS_CREATE_OR_UPDATE_FORM);				
	}

	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testProcessCreationFormHasErrors() throws Exception {
    	Pet newPet=new Pet();
    	newPet.setName("Ulito");		
		newPet.setBirthDate(LocalDate.now().plusDays(1));    	
		result.reject("petType", "Requied!");
		result.reject("birthDate", "BirthDate must be before or equal to today");
		
		String view=petController.processCreationForm(TEST_OWNER_ID1, newPet, result, model);
		
		assertEquals(view,"pets/createOrUpdatePetForm");		
	}
	//Usuario que no cumple la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testProcessCreationFormSuccessWithoutAccess() throws Exception {
    	Pet newPet=new Pet();
    	newPet.setName("Rositis");
		newPet.setType(petService.findPetTypes().iterator().next());
		newPet.setBirthDate(LocalDate.now());    	
		
		String view=petController.processCreationForm(TEST_OWNER_ID2, newPet, result, model);
    	
		assertEquals(view, REDIRECT_TO_OUPS);				
	}

	
	//Usuario que cumple la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testInitUpdateForm() throws Exception {		
		
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
    	Pet updateSara= new Pet();
    	updateSara.setName("Mini");
    	updateSara.setType(petService.findPetTypes().iterator().next());
    	updateSara.setBirthDate(LocalDate.now());    	
		
		String view=petController.processUpdateForm(TEST_OWNER_ID2, updateSara, result, TEST_PET_ID_17, model);
		
		assertEquals(view, "redirect:/owner/pets");
		assertNotNull(model.get("owner"));
		assertNotNull(model.get("edit"));
	}
	
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
	@Test
	void testProcessUpdateFormCatchException() throws Exception {
    	Pet updateSara= new Pet();
    	updateSara.setName("Nina");
    	updateSara.setType(petService.findPetTypes().iterator().next());
    	updateSara.setBirthDate(LocalDate.now());    	
		
		String view=petController.processUpdateForm(TEST_OWNER_ID2, updateSara, result, TEST_PET_ID_17, model);
		
		assertEquals(view, VIEWS_PETS_CREATE_OR_UPDATE_FORM);
		assertNotNull(model.get("owner"));
		assertNotNull(model.get("edit"));
	}
	
	@WithMockUser(username = "admin", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessUpdateFormSuccessAsAdmin() throws Exception {
    	Pet updateSamantha=new Pet();
    	updateSamantha.setName("Uli");
    	updateSamantha.setType(petService.findPetTypes().iterator().next());
    	updateSamantha.setBirthDate(LocalDate.now());    	
		
		String view=petController.processUpdateForm(TEST_OWNER_ID3, updateSamantha, result, TEST_PET_ID_7, model);
		
		assertEquals(view, "redirect:/owner/pets");
		assertNotNull(model.get("owner"));
		assertNotNull(model.get("edit"));
	}
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
    	Pet updateLeo=new Pet();
    	updateLeo.setName("Mini");		
    	updateLeo.setBirthDate(LocalDate.now());    	
		result.reject("petType", "Requied!");
		
		String view=petController.processUpdateForm(TEST_OWNER_ID1, updateLeo, result, TEST_PET_ID_1, model);
		
		assertEquals(view,"pets/createOrUpdatePetForm");
	}
	//Usuario que no cumple la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testProcessUpdateFormWithoutAccess() throws Exception {
    	Pet updateSamantha=this.petService.findPetById(TEST_PET_ID_17);
    	updateSamantha.setName("Uli");
    	updateSamantha.setType(updateSamantha.getType());
    	updateSamantha.setBirthDate(updateSamantha.getBirthDate());    	
		
		String view=petController.processUpdateForm(TEST_OWNER_ID3, updateSamantha, result, TEST_PET_ID_7, model);
		
		assertEquals(view, REDIRECT_TO_OUPS);
	}
	
	
	
	//Usuario que cumple la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testShowPetRequestRejected() {
		
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
		
		String view = this.petController.showAndUpdatePetRequest(TEST_OWNER_ID1, TEST_PET_ID_3, model);
		
		assertEquals(view, "pets/updatePetRequest");
		assertNotNull(model.get("status"));
		assertNotNull(model.get("pet"));
		assertNotNull(model.get("petRequest"));
	}
	
	@WithMockUser(username = "admin", password = "4dm1n", authorities = "admin")
	@Test
	void testUpdatePetRequestPending() {
		
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
		
		String view = this.petController.showAndUpdatePetRequest(TEST_OWNER_ID3, TEST_PET_ID_5, model);
		
		assertEquals(view, REDIRECT_TO_OUPS);
	}
	

	
	//Usuario que SI cumple la seguridad
	@WithMockUser(username = "admin", password = "4dm1n", authorities = "admin")
	@Test
	void testAnswerPetRequestPendingToAccepted() {

		Pet updateGeorge= new Pet();
		updateGeorge.setStatus(PetRegistrationStatus.ACCEPTED);
		updateGeorge.setJustification("");
		
		String view = this.petController.AnswerPetRequest(updateGeorge, result, TEST_OWNER_ID1, TEST_PET_ID_3, model);
		
		assertEquals(view, "redirect:/requests");
		assertNotNull("status");
		assertNotNull("petRequest");
	}

	@WithMockUser(username = "admin", password = "4dm1n", authorities = "admin")
	@Test
	void testAnswerPetRequestPendingToRejectedWithErrors() {

		Pet updateGeorge= new Pet();
		updateGeorge.setStatus(PetRegistrationStatus.REJECTED);
		updateGeorge.setJustification("");
		result.reject("justification", "justification is mandatory if the application is rejected");
		
		String view = this.petController.AnswerPetRequest(updateGeorge, result, TEST_OWNER_ID3, TEST_PET_ID_4, model);
		
		assertEquals(view, "pets/updatePetRequest");
	}
	
	//Usuario que NO cumple la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testAnswerPetRequestPendingWithoutAccess() {

		Pet updateGeorge= new Pet();
		updateGeorge.setStatus(PetRegistrationStatus.ACCEPTED);
		updateGeorge.setJustification("");
		
		String view = this.petController.AnswerPetRequest(updateGeorge, result, TEST_OWNER_ID3, TEST_PET_ID_4, model);
		
		assertEquals(view, REDIRECT_TO_OUPS);
	}
	
	
	
	//Usuario que SI cumple la seguridad
	@WithMockUser(username = "admin", password = "4dm1n", authorities = "admin")
	@Test
	void testShowPetRequests() {
		
		String view = this.petController.showPetRequests(model);
		
		assertEquals(view, "pets/requests");		
	}
	
	

	//Usuario que SI cumple la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testShowMyPetRequests() {
		
		String view = this.petController.showMyPetRequests(model);
		
		assertEquals(view, "pets/myRequests");		
		assertNotNull("pets");
	}
	
	
	
	//Usuario que SI cumple la seguridad sin pets disabled
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testShowMyPetsActive() {
		
		String view = this.petController.showMyPetsActive(model);
		
		assertEquals(view, "pets/myPetsActive");		
		assertNotNull("pets");
		assertNotNull("owner");
	}
	
	//Usuario que SI cumple la seguridad con pets disabled
	@WithMockUser(username = "owner3", password = "0wn3333r_3", authorities = "owner")
	@Test
	void testShowMyPetsActiveAndDisabled() {
		
		String view = this.petController.showMyPetsActive(model);
		
		assertEquals(view, "pets/myPetsActive");	
		assertNotNull("disabled");
		assertNotNull("pets");
		assertNotNull("owner");
	}
	
	
	//Usuario que SI cumple la seguridad con pets disabled
	@WithMockUser(username = "owner3", password = "0wn3333r_3", authorities = "owner")
	@Test
	void testShowMyPetsDisabled() {
		
		String view = this.petController.showMyPetsDisabled(TEST_OWNER_ID3, model);
		
		assertEquals(view, "pets/myPetsDisabled");	
		assertNotNull("pets");
		assertNotNull("owner");
	}
	
	//Usuario que NO cumple la seguridad 
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testShowMyPetsDisabledWithoutAccess() {
		
		String view = this.petController.showMyPetsDisabled(TEST_OWNER_ID1, model);
		
		assertEquals(view, REDIRECT_TO_OUPS);	
	}
}

