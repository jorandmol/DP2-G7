package org.springframework.samples.petclinic.web.integration;

import static org.junit.Assert.assertNotNull;

import java.util.Collections;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.web.PetTypeController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PetTypeControllerIntegrationTests {

	@Autowired
	private PetTypeController petTypeController;

	private ModelMap modelMap = new ModelMap();

	private BindingResult result = new MapBindingResult(Collections.emptyMap(), "");

	private static final String VIEWS_PET_TYPE_CREATE_OR_UPDATE_FORM = "pet-type/typeForm";

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testList() throws Exception {
		String view = petTypeController.listTypes(modelMap);
		Assert.assertEquals(view, "pet-type/typeList");
		assertNotNull(modelMap.get("petTypes"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testInitCreationForm() throws Exception {
		String view = petTypeController.addType(modelMap);
		;
		Assert.assertEquals(view, VIEWS_PET_TYPE_CREATE_OR_UPDATE_FORM);
		assertNotNull(modelMap.get("petType"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		PetType petType = new PetType();
		petType.setName("Hipopotamus");

		String view = petTypeController.savePetType(petType, result, modelMap);

		Assert.assertEquals(view, "redirect:/pet-type");
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		PetType petType = new PetType();
		petType.setName("");

		result.reject("name");

		String view = petTypeController.savePetType(petType, result, modelMap);

		Assert.assertEquals(view, VIEWS_PET_TYPE_CREATE_OR_UPDATE_FORM);

	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessCreationFormHasRepeatedName() throws Exception {
		PetType petType = new PetType();
		petType.setName("cat");

		String view = petTypeController.savePetType(petType, result, modelMap);

		Assert.assertEquals(view, VIEWS_PET_TYPE_CREATE_OR_UPDATE_FORM);

	}
}
