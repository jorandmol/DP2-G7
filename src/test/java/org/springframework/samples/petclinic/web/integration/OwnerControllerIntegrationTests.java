package org.springframework.samples.petclinic.web.integration;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.web.OwnerController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.servlet.ModelAndView;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OwnerControllerIntegrationTests {

	private static final int TEST_OWNER_ID1 = 1;
	private static final int TEST_OWNER_ID3 = 3;
	private static final int TEST_OWNER_ID6 = 6;
	private static final int TEST_OWNER_ID7 = 7;
	private static final int TEST_OWNER_ID10 = 10;

	
	private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";
	private static final String REDIRECT_TO_OUPS = "redirect:/oups";
	
	private ModelMap model = new ModelMap();
	private BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
	
	@Autowired
	private OwnerController ownerController;
	
	@Autowired
	private OwnerService ownerService;
	
	// TEST para usuario que SI cumple la seguridad
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testInitCreationForm() throws Exception {
		
		String view = this.ownerController.initCreationForm(model);
		
		assertEquals(view, VIEWS_OWNER_CREATE_OR_UPDATE_FORM);
		assertNotNull(model.get("owner"));	
	}
	
	// TEST para usuario que NO cumple la seguridad
	@WithMockUser(username = "owner1", password = "own3333r_1", authorities = "owner")
	@Test
	void testInitCreationFormWithoutAccess() throws Exception {
			
		String view = this.ownerController.initCreationForm(model);
			
		assertEquals(view, REDIRECT_TO_OUPS);
		assertNull(model.get("owner"));	
	}
	
	
	
	// TESTs para usuarios que SI cumplen la seguridad
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		
		User newUser = new User(); newUser.setUsername("javi"); newUser.setPassword("own3333r_javi");
		
		Owner newOwner = new Owner();
		newOwner.setFirstName("Javier");
		newOwner.setLastName("Hierro");
		newOwner.setAddress("c/Avenida de la paz, 1");
		newOwner.setCity("Badajoz");
		newOwner.setTelephone("636788234");
		newOwner.setUser(newUser);
		
		String view = this.ownerController.processCreationForm(newOwner, result, model);
		
		Owner javier= this.ownerService.findOwnerByUsername("javi");
		
		assertEquals(view, "redirect:/owners/" + javier.getId());
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessCreationFormCatchException() throws Exception {
		
		User newUser = new User(); newUser.setUsername("owner1"); newUser.setPassword("own3333r_rocio");
		
		Owner newOwner = new Owner();
		newOwner.setFirstName("Rocio");
		newOwner.setLastName("Hierro");
		newOwner.setAddress("c/Avenida de la paz, 1");
		newOwner.setCity("Badajoz");
		newOwner.setTelephone("636788234");
		newOwner.setUser(newUser);
		
		String view = this.ownerController.processCreationForm(newOwner, result, model);
				
		assertEquals(view, VIEWS_OWNER_CREATE_OR_UPDATE_FORM);
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		
		User newUser = new User(); newUser.setUsername("hierro01"); newUser.setPassword("own33");
		
		Owner newOwner = new Owner();
		newOwner.setFirstName("");
		newOwner.setLastName("Hierro");
		newOwner.setAddress("c/Avenida de la paz, 1");
		newOwner.setCity("Badajoz");
		newOwner.setTelephone("636788234");
		newOwner.setUser(newUser);
		
		result.reject("firstname", "empty");
		result.reject("user.password", "password.format");
		
		String view = this.ownerController.processCreationForm(newOwner, result, model);
				
		assertEquals(view, VIEWS_OWNER_CREATE_OR_UPDATE_FORM);
	}
	
	// TEST para usuario que NO cumple la seguridad
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
	@Test
	void testProcessCreationFormSuccessWithoutAccess() throws Exception {
		
		User newUser = new User(); newUser.setUsername("hierro01"); newUser.setPassword("own333r_0");
		
		Owner newOwner = new Owner();
		newOwner.setFirstName("Patricio");
		newOwner.setLastName("Hierro");
		newOwner.setAddress("c/Avenida de la paz, 1");
		newOwner.setCity("Badajoz");
		newOwner.setTelephone("636788234");
		newOwner.setUser(newUser);
		
		String view = this.ownerController.processCreationForm(newOwner, result, model);
		
		assertEquals(view, REDIRECT_TO_OUPS);
	}
	
	
	
	// TEST para usuario que SI cumple la seguridad
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testInitFindForm() throws Exception {
			
		String view = this.ownerController.initFindForm(model);
		
		assertEquals(view, "owners/findOwners");
		assertNotNull("owner");
	}
	
	// TEST para usuario que NO cumple la seguridad
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
	@Test
	void testInitFindFormWithoutSecurity() throws Exception {
			
		String view = this.ownerController.initFindForm(model);
		
		assertEquals(view, REDIRECT_TO_OUPS);	
	}
	
	
	
	// TESTs para usuarios que SI cumplen la seguridad
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessFindFormSuccess() throws Exception {
		
		Owner owner= new Owner();
		
		String view = this.ownerController.processFindForm(owner, result, model);
		
		assertEquals(view, "owners/ownersList");
		assertNotNull("selections");
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessFindFormByLastName() throws Exception {
		Owner owner = this.ownerService.findOwnerById(TEST_OWNER_ID10);
				
		String view =this.ownerController.processFindForm(owner, result, model);
		
		assertEquals(view, "redirect:/owners/" + owner.getId());
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessFindFormNoOwnersFound() throws Exception {
		Owner owner= new Owner();
		owner.setLastName("Unknown Surname");
		
		String view = this.ownerController.processFindForm(owner, result, model);
		
		assertEquals(view, "owners/findOwners");		
	}
	
	// TEST para usuario que NO cumple la seguridad
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
	@Test
	void testProcessFindFormSuccessWithoutAccess() throws Exception {
		Owner owner= new Owner();
		owner.setLastName("sabio");
		
		String view = this.ownerController.processFindForm(owner, result, model);
		
		assertEquals(view, REDIRECT_TO_OUPS);
	}
	
	
	
	
	// TESTs para usuarios que SI cumplen la seguridad
	@WithMockUser(username = "owner6", password = "0wn3333r_6", authorities = "owner")
	@Test
	void testInitUpdateProfileForm() throws Exception {
		
		String view = this.ownerController.initUpdateOwnerForm(TEST_OWNER_ID6, model);
		
		assertEquals(view, VIEWS_OWNER_CREATE_OR_UPDATE_FORM);
		assertNotNull(model.get("owner"));
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testInitUpdateOwnerForm() throws Exception {
		
		String view = this.ownerController.initUpdateOwnerForm(TEST_OWNER_ID7, model);
		
		assertEquals(view, VIEWS_OWNER_CREATE_OR_UPDATE_FORM);
		assertNotNull(model.get("owner"));
	}
	
	// TEST para usuarios que NO cumplen la seguridad
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
	@Test
	void testInitUpdateOwnerFormWithoutAccess() throws Exception {
		
		String view = this.ownerController.initUpdateOwnerForm(TEST_OWNER_ID7, model);
		
		assertEquals(view, REDIRECT_TO_OUPS);
		assertNull(model.get("owner"));
	}
	
	
	
	// TESTs para usuarios que SI cumplen la seguridad
	@WithMockUser(username = "owner7", password = "0wn3333r_7", authorities = "owner")
	@Test
	void testProcessUpdateProfileFormSuccess() throws Exception {
		Owner updateJeff = this.ownerService.findOwnerById(TEST_OWNER_ID7);
		updateJeff.setTelephone("636577823");
		
		String view = this.ownerController.processUpdateOwnerForm(updateJeff, result, TEST_OWNER_ID7, model);
	
		assertEquals(view, "redirect:/owners/{ownerId}");
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		Owner updateJeff = this.ownerService.findOwnerById(TEST_OWNER_ID7);
		updateJeff.setFirstName("Jeffy");
		updateJeff.setTelephone("636577823");
		
		String view = this.ownerController.processUpdateOwnerForm(updateJeff, result, TEST_OWNER_ID7, model);
	
		assertEquals(view, "redirect:/owners/{ownerId}");
	}
	
	@WithMockUser(username = "owner6", password = "0wn3333r_6", authorities = "owner")
	@Test
	void testProcessUpdateOwnerFormHasErrors() throws Exception {
		Owner updateJean = this.ownerService.findOwnerById(TEST_OWNER_ID6);
		
		updateJean.setAddress("");
		updateJean.setLastName("");
		
		result.reject("address", "empty");
		result.reject("lastname", "empty");
		
		String view = this.ownerController.processUpdateOwnerForm(updateJean, result, TEST_OWNER_ID6, model);

		assertEquals(view, VIEWS_OWNER_CREATE_OR_UPDATE_FORM);
	}
	
	// TEST para usuarios que NO cumplen la seguridad
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
	@Test
	void testProcessUpdateOwnerFormSuccessWithoutAccess() throws Exception {
		
		Owner updateJeff = this.ownerService.findOwnerById(TEST_OWNER_ID7);
		updateJeff.setFirstName("Jeffy");
		updateJeff.setTelephone("636577823");
		
		String view = this.ownerController.processUpdateOwnerForm(updateJeff, result, TEST_OWNER_ID7, model);
	
		assertEquals(view, REDIRECT_TO_OUPS);
	}
	
	
	
	
	// TESTs para usuarios que SI cumplen la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testShowProfile() throws Exception {

		ModelAndView view = this.ownerController.showOwner(TEST_OWNER_ID1);
		
		assertNotNull(view);
		
	}
	// TEST para usuario que SI cumple la seguridad
	// TEST para owner que tiene pets disabled
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testShowOwnerWithPetsDisabled() throws Exception {
		
		ModelAndView view = this.ownerController.showOwner(TEST_OWNER_ID3);
		
		assertNotNull(view);
	}
	
	// TEST para usuario que NO cumple la seguridad
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
	@Test
	void testShowOwnerWithoutAccess() throws Exception {
		
		ModelAndView view = this.ownerController.showOwner(TEST_OWNER_ID3);
		
		assertNotNull(view);	
	}
}
