package org.springframework.samples.petclinic.web.integration;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.web.VetController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.servlet.ModelAndView;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VetControllerIntegrationTests {

	private static final int TEST_VET_ID1 = 1;
	private static final int TEST_VET_ID2 = 2;
	private static final int TEST_VET_ID3 = 3;
	private static final int TEST_VET_ID4 = 4;
	private static final int TEST_VET_ID5 = 5;

	private static final String REDIRECT_TO_OUPS = "redirect:/oups";
	private static final String VIEWS_VET_CREATE_OR_UPDATE_FORM = "vets/createOrUpdateVetForm";

	private ModelMap model = new ModelMap();
	private BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
	
	@Autowired
	private VetController vetController;
	
	@Autowired
	private VetService vetService;
     
	
	// TEST para usuario que SI cumple la seguridad
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testShowVetList() throws Exception {
			
		String view = this.vetController.showVetList(model);
		
		assertEquals(view, "vets/vetList");
		assertNotNull(model.get("vets"));		
	}
	
	// TEST para usuario que NO cumple la seguridad
	@WithMockUser(username = "vet1", password = "veter1n4ri0_1", authorities = "veterinarian")
	@Test
	void testShowVetListWithoutAccess() throws Exception {
		
		String view = this.vetController.showVetList(model);
		
		assertEquals(view, REDIRECT_TO_OUPS);
		assertNull(model.get("vets"));
	}
	
	
	
	// TEST para usuario que SI cumple la seguridad
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testInitCreationForm() throws Exception {
		
		String view = this.vetController.initCreationForm(model);
		
		assertEquals(view, VIEWS_VET_CREATE_OR_UPDATE_FORM);
		assertNotNull(model.get("vet"));
	}
	
	// TEST para usuario que NO cumple la seguridad
	@WithMockUser(username = "vet1", password = "veter1n4ri0_1", authorities = "veterinarian")
	@Test
	void testInitCreationFormWithoutAccess() throws Exception {
		
		String view = this.vetController.initCreationForm(model);
		
		assertEquals(view, REDIRECT_TO_OUPS);
		assertNull(model.get("vet"));
	}
	
	
	
	// TEST para usuario que SI cumple la seguridad
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		Specialty s = vetService.findSpecialties().iterator().next();
		
		User newUser = new User(); newUser.setUsername("martitaH"); newUser.setPassword("v3t_rinarian");
		
		Vet newVet = new Vet();
		newVet.addSpecialty(s);
		newVet.setFirstName("Marta");
		newVet.setLastName("Hierro");
		newVet.setAddress("c/Avenida de la paz, 1");
		newVet.setCity("Badajoz");
		newVet.setTelephone("636788234");
		newVet.setUser(newUser);
		
		String view = this.vetController.processCreationForm(newVet, result, model);
		
		Vet marta = this.vetService.findVetByUsername("martitaH"); 
		
		assertEquals(view, "redirect:/vets/" + marta.getId());
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessCreationFormCatchException() throws Exception {
		Specialty s = vetService.findSpecialties().iterator().next();
		
		User newUser = new User(); newUser.setUsername("vet2"); newUser.setPassword("v3t_rinarian");
		
		Vet newVet = new Vet();
		newVet.addSpecialty(s);
		newVet.setFirstName("Helen");
		newVet.setLastName("Hierro");
		newVet.setAddress("c/Avenida de la paz, 1");
		newVet.setCity("Badajoz");
		newVet.setTelephone("636788234");
		newVet.setUser(newUser);
				
		String view = this.vetController.processCreationForm(newVet, result, model);
		
		assertEquals(view, VIEWS_VET_CREATE_OR_UPDATE_FORM);
		
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		Specialty s = vetService.findSpecialties().iterator().next(); 
		
		User newUser = new User(); newUser.setUsername("martitaH"); newUser.setPassword("v3t_rinarian");
		
		Vet newVet = new Vet();
		newVet.addSpecialty(s);
		newVet.setFirstName("");
		newVet.setLastName("Hierro");
		newVet.setAddress("c/Avenida de la paz, 1");
		newVet.setCity("Badajoz");
		newVet.setTelephone("63678");
		newVet.setUser(newUser);
		
		result.reject("telephone", "phone.format");
		result.reject("firstName", "empty");
		
		String view = this.vetController.processCreationForm(newVet, result, model);
		
		assertEquals(view, VIEWS_VET_CREATE_OR_UPDATE_FORM);
		assertNotNull("vet");
	}
	
	// TEST para usuario que NO cumple la seguridad
	@WithMockUser(username = "vet1", password = "v3terinarian_1", authorities = "veterinarian")
	@Test
	void testProcessCreationFormWithoutSecurity() throws Exception {
		Specialty s = vetService.findSpecialties().iterator().next(); 
		
		User newUser = new User(); newUser.setUsername("alee"); newUser.setPassword("v3t_rinarian");

		Vet newVet = new Vet();
		newVet.addSpecialty(s);
		newVet.setFirstName("Alejandra");
		newVet.setLastName("Hierro");
		newVet.setAddress("c/Avenida de la paz, 1");
		newVet.setCity("Badajoz");
		newVet.setTelephone("63678");
		newVet.setUser(newUser);
		
		String view = this.vetController.processCreationForm(newVet, result, model);
		
		assertEquals(view, REDIRECT_TO_OUPS);
	}
	
	
	
	// TEST para usuarios que SI cumple la seguridad
	@WithMockUser(username = "vet1", password = "veter1n4ri0_1", authorities = "veterinarian")
	@Test
	void testInitUpdateProfileForm() throws Exception {
		
		String view = this.vetController.initUpdateVetForm(TEST_VET_ID1, model);
		
		assertEquals(view, VIEWS_VET_CREATE_OR_UPDATE_FORM);
		assertNotNull(model.get("vet"));
	}
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testInitUpdateForm() throws Exception {
		
		String view = this.vetController.initUpdateVetForm(TEST_VET_ID3, model);
		
		assertEquals(view, VIEWS_VET_CREATE_OR_UPDATE_FORM);
		assertNotNull(model.get("vet"));
	}
	// TEST para usuario que NO cumple la seguridad
	@WithMockUser(username = "vet1", password = "veter1n4ri0_1", authorities = "veterinarian")
	@Test
	void testInitUpdateFormWithoutAccess() throws Exception {
		
		String view = this.vetController.initUpdateVetForm(TEST_VET_ID2, model);
		
		assertEquals(view, REDIRECT_TO_OUPS);
		assertNull(model.get("vet"));
	}
	
	
	
	// TEST para usuario que SI cumple la seguridad
	@WithMockUser(username = "vet2", password = "veter1n4ri0_2", authorities = "veterinarian")
	@Test
	void testProcessUpdateProfileFormSuccess() throws Exception {
		Vet updateHelen= this.vetService.findVetById(TEST_VET_ID2);
		updateHelen.setTelephone("676522389");
		updateHelen.addSpecialty(this.vetService.findSpecialties().iterator().next());
		String view = this.vetController.processUpdateVetForm(updateHelen, result, TEST_VET_ID2, model);
	
		assertEquals(view, "redirect:/vets/{vetId}");
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessUpdateVetFormSuccess() throws Exception {		
		Vet updateHenry = this.vetService.findVetById(TEST_VET_ID5);
		updateHenry.addSpecialty(this.vetService.findSpecialties().iterator().next());
		updateHenry.addSpecialty(this.vetService.findSpecialties().iterator().next());
		updateHenry.addSpecialty(this.vetService.findSpecialties().iterator().next());
		updateHenry.addSpecialty(this.vetService.findSpecialties().iterator().next());
		updateHenry.setAddress("c/Santa Marta, 3");
		
		String view = this.vetController.processUpdateVetForm(updateHenry, result, TEST_VET_ID5, model);
		
		assertEquals(view, "redirect:/vets/{vetId}");
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessUpdateVetFormHasErrors() throws Exception {		
		Vet updateRafael = this.vetService.findVetById(TEST_VET_ID4);
		updateRafael.setFirstName("");
		updateRafael.setAddress("");
		
		result.reject("firstName", "empty");
		result.reject("address", "empty");
		
		String view = this.vetController.processUpdateVetForm(updateRafael, result, TEST_VET_ID4, model);
		
		assertEquals(view, VIEWS_VET_CREATE_OR_UPDATE_FORM);
	}
	
	// TEST para usuario que NO cumple la seguridad
	@WithMockUser(username = "vet2", password = "veter1n4ri0_2", authorities = "veterinarian")
	@Test
	void testProcessUpdateVetFormSuccessWithoutAccess() throws Exception {
		Vet updateLinda = this.vetService.findVetById(TEST_VET_ID3);
		updateLinda.setFirstName("Elena");
		
		String view = this.vetController.processUpdateVetForm(updateLinda, result, TEST_VET_ID3, model);
	
		assertEquals(view, REDIRECT_TO_OUPS);
	}
	
	
	
	//Comprobar que, las listas de appointments de un vet
	@WithMockUser(username = "vet1", password = "veter1n4ri0_1", authorities = "veterinarian")
	@Test
	void testShowAppoimentsByVetList() throws Exception {
		
		String view = this.vetController.showAppoimentsByVetList(model);
		
		assertEquals(view, "vets/appointmentsList");
		assertNotNull("appointmentsToday");
		assertNotNull("appointmentsWithVisit");
		assertNotNull("nextAppointments");
	}
	

	
	// TEST para usuarios que SI cumple la seguridad
	@WithMockUser(username = "vet4", password = "veter1n4ri0_4", authorities = "veterinarian")
	@Test
	void testShowVetProfile() throws Exception {

		ModelAndView view = this.vetController.showVet(TEST_VET_ID4);
		
		assertNotNull(view);
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testShowVet() throws Exception {

		ModelAndView view = this.vetController.showVet(TEST_VET_ID4);
		
		assertNotNull(view);
	}
	
	// TEST para usuarios que NO cumplen la seguridad
	@WithMockUser(username = "vet2", password = "veter1n4ri0_2", authorities = "veterinarian")
	@Test
	void testShowVetWithoutAccess() throws Exception {

		ModelAndView view = this.vetController.showVet(TEST_VET_ID4);
		
		assertNotNull(view);
	}
	
		
	
	@WithMockUser(username="vet1", password="veter1n4ri0_1", authorities="veterinarian")
	@Test
	void testShowPetsList() throws Exception {
		
		String view = this.vetController.showPetsLit(model);
		
		assertEquals(view, "pets/petsList");
	}
}
