package org.springframework.samples.petclinic.web.integration;

import static org.junit.Assert.assertNotNull;

import java.util.Collections;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.MedicalTest;
import org.springframework.samples.petclinic.service.MedicalTestService;
import org.springframework.samples.petclinic.web.MedicalTestController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MedicalTestControllerIntegrationTests {

	private static final String VIEWS_MEDICAL_TEST_CREATE_OR_UPDATE_FORM = "medical-tests/createOrUpdateMedicalTestForm";

	private static final int TEST_MT_ID = 1;
	
	@Autowired
	private MedicalTestController medicalTestController;

	@Autowired
	private MedicalTestService medicalTestService;
	
	private ModelMap modelMap = new ModelMap();

	private BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
	
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testShowMedicalTestsList() throws Exception {
		String view = medicalTestController.showMedicalTestsList(modelMap);
		Assert.assertEquals(view, "medical-tests/medicalTestsList");
		assertNotNull(modelMap.get("medicalTests"));
	}

	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testIinitCreationForm() throws Exception {
		String view = medicalTestController.initCreationForm(modelMap);
		Assert.assertEquals(view, VIEWS_MEDICAL_TEST_CREATE_OR_UPDATE_FORM);
		assertNotNull(modelMap.get("medicalTest"));
	}

	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		MedicalTest medicalTest = new MedicalTest();
		medicalTest.setName("Radiography");
		medicalTest.setDescription("Images of the internal structure of the body to assess the presence of foreign objects, and structural damage or anomaly");
		String view = medicalTestController.processCreationFrom(medicalTest, result, modelMap);
		Assert.assertEquals(view, "redirect:/medical-tests");
	}

	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		MedicalTest medicalTest = new MedicalTest();
		medicalTest.setName("Radiography");
		medicalTest.setDescription("");
		result.reject("description", "no puede estar vacío");
		String view = medicalTestController.processCreationFrom(medicalTest, result, modelMap);
		Assert.assertEquals(view, VIEWS_MEDICAL_TEST_CREATE_OR_UPDATE_FORM);
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testIinitUpdateForm() throws Exception {
		String view = medicalTestController.initUpdateForm(TEST_MT_ID, modelMap);
		Assert.assertEquals(view, VIEWS_MEDICAL_TEST_CREATE_OR_UPDATE_FORM);
		assertNotNull(modelMap.get("medicalTest"));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		MedicalTest medicalTest = this.medicalTestService.findMedicalTestById(TEST_MT_ID);
		medicalTest.setDescription("Images of the internal structure of the body to assess the presence of foreign objects, and structural damage or anomaly");
		String view = medicalTestController.processUpdateFrom(TEST_MT_ID, medicalTest, result, modelMap);
		Assert.assertEquals(view, "redirect:/medical-tests");
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		MedicalTest medicalTest = this.medicalTestService.findMedicalTestById(TEST_MT_ID);
		medicalTest.setName("");
		result.reject("name", "no puede estar vacío");
		String view = medicalTestController.processUpdateFrom(TEST_MT_ID, medicalTest, result, modelMap);
		Assert.assertEquals(view, VIEWS_MEDICAL_TEST_CREATE_OR_UPDATE_FORM);
	}
}
