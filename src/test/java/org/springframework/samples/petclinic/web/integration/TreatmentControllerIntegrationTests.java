package org.springframework.samples.petclinic.web.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.time.LocalDate;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Treatment;
import org.springframework.samples.petclinic.service.TreatmentService;
import org.springframework.samples.petclinic.web.TreatmentController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TreatmentControllerIntegrationTests {

	private static final String OWNER_ROLE = "owner";
	private static final String VET_ROLE = "veterinarian";

	private static final String VIEWS_TREATMENT_LIST = "treatments/listTreatments";
	private static final String VIEWS_TREATMENT_FORM = "treatments/createOrUpdateTreatmentForm";
	private static final String REDIRECT_TO_OUPS = "redirect:/oups";
	private static final String REDIRECT_TO_VET_TREATMENT_LIST = "redirect:/vets/pets/{petId}/treatments";
	private static final String REDIRECT_TO_TREATMENT_SHOW = "redirect:/vets/pets/{petId}/treatments/{treatmentId}";

	private static final int TEST_OWNER_ID = 1;
	private static final int TEST_PET_ID = 1;
	private static final int TEST_WRONG_OWNER_ID = 2;
	private static final int TEST_TREATMENT_ID_1 = 1;
	private static final int TEST_TREATMENT_ID_2 = 2;
	private static final int TEST_TREATMENT_HISTORY_ID = 1;

	@Autowired
	private TreatmentController treatmentController;

	@Autowired
	private TreatmentService treatmentService;
	
	ModelMap modelMap = new ModelMap();
	
	private BindingResult result = new MapBindingResult(Collections.emptyMap(), "");

	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testListTreatmentsToOwner() throws Exception {		
		String view = this.treatmentController.showTreatments(TEST_OWNER_ID, TEST_PET_ID, modelMap);
		assertNotNull(modelMap.get("treatments"));
		assertNotNull(modelMap.get("treatmentsDone"));
		assertEquals(view, VIEWS_TREATMENT_LIST);
	}

	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testNotListTreatmentsToOwner() throws Exception {
		String view = this.treatmentController.showTreatments(TEST_WRONG_OWNER_ID, TEST_PET_ID, modelMap);
		assertEquals(view, REDIRECT_TO_OUPS);
	}

	@Test
    @WithMockUser(username="vet1", password="v3terinarian_1", authorities=VET_ROLE)
    void testListTreatmentsToVet() throws Exception {    
	    String view = this.treatmentController.showTreatments(TEST_PET_ID, modelMap);
	    assertEquals(view , VIEWS_TREATMENT_LIST);
	    assertEquals(modelMap.get("isVet"), true);
	    assertEquals(modelMap.get("petId"), (TEST_PET_ID));
	    assertNotNull(modelMap.get("treatments"));
		assertNotNull(modelMap.get("treatmentsDone"));
    }

	@Test
	@WithMockUser(username="vet1", password="v3terinarian_1", authorities=VET_ROLE)
	void testInitNewTreatmentForm() throws Exception {
		String view = this.treatmentController.initNewTreatmentForm(TEST_PET_ID, modelMap);
		assertNotNull(modelMap.get("treatment"));
		assertEquals(view, VIEWS_TREATMENT_FORM);
	}

    @Test
    @WithMockUser(username="vet1", password="v3terinarian_1", authorities=VET_ROLE)
    void testProcessNewTreatmentForm() throws Exception {	    
	    Treatment treatment = new Treatment();
	    treatment.setDescription("Revisión mensual");
	    treatment.setTimeLimit(LocalDate.now().plusDays(30));
	    String view = this.treatmentController.processNewTreatmentForm(treatment, result, TEST_PET_ID, modelMap);
	    assertEquals(view, REDIRECT_TO_VET_TREATMENT_LIST);
    }
    
    @Test
    @WithMockUser(username="vet1", password="v3terinarian_1", authorities=VET_ROLE)
    void testInitTreatmentEditForm() throws Exception {
	    String view = this.treatmentController.initTreatmentEditForm(TEST_PET_ID, TEST_TREATMENT_ID_1, modelMap);
	    assertNotNull(modelMap.get("treatment"));
	    assertEquals(modelMap.get("edit"), true);
	    assertNotNull(modelMap.get("treatmentMedicines"));
	    assertNotNull(modelMap.get("otherMedicines"));
	    assertEquals(view, VIEWS_TREATMENT_FORM);
    }
    
    @Test
    @WithMockUser(username="vet1", password="v3terinarian_1", authorities=VET_ROLE)
    void testNotInitTreatmentEditForm() throws Exception {
	    String view = this.treatmentController.initTreatmentEditForm(TEST_PET_ID, TEST_TREATMENT_ID_2, modelMap);
	    assertEquals(view, REDIRECT_TO_OUPS);
    }
    
    @Test
    @WithMockUser(username="vet1", password="v3terinarian_1", authorities=VET_ROLE)
    void testProcessTreatmentEditForm() throws Exception {
	    Treatment treatment = this.treatmentService.findById(TEST_TREATMENT_ID_1);
	    treatment.setDescription("Revisión mensual");
	    String view = this.treatmentController.processTreatmentEditForm(treatment, result, TEST_PET_ID, TEST_TREATMENT_ID_1, modelMap);
	    assertEquals(view, REDIRECT_TO_TREATMENT_SHOW);
    }
    
    @Test
    @WithMockUser(username="vet1", password="v3terinarian_1", authorities=VET_ROLE)
    void testNotProcessTreatmentEditForm() throws Exception {
    	Treatment treatment = this.treatmentService.findById(TEST_TREATMENT_ID_1);
	    treatment.setDescription("Revisión mensual");
	    String view = this.treatmentController.processTreatmentEditForm(treatment, result, TEST_PET_ID, TEST_TREATMENT_ID_2, modelMap);
	    assertEquals(view, REDIRECT_TO_OUPS);
    }
    
    @Test
    @WithMockUser(username="vet1", password="v3terinarian_1", authorities=VET_ROLE)
    void testDeleteTreatmentHistoryRegister() throws Exception {
    	String view = this.treatmentController.deleteTreatmentHistoryRegister(TEST_TREATMENT_HISTORY_ID, TEST_TREATMENT_ID_1, TEST_PET_ID);
    	assertEquals(view, REDIRECT_TO_TREATMENT_SHOW);
    }

}
