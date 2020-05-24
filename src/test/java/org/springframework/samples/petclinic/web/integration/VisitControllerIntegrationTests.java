package org.springframework.samples.petclinic.web.integration;

import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.MedicalTest;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.MedicalTestService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.samples.petclinic.web.VisitController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VisitControllerIntegrationTests {

	private static final String VIEWS_VISIT_DETAILS = "visits/visitDetails";
	
	private static final String VIEWS_CREATE_OR_UPDATE_VISIT_FORM = "pets/createOrUpdateVisitForm";
	
	private static final String REDIRECT_TO_OUPS = "redirect:/oups";

	private static final int TEST_PET_ID_1 = 1;
	
	private static final int TEST_PET_ID_12 = 12;
	
	private static final int TEST_PET_ID_15 = 15;
	
	private static final int TEST_PET_ID_18 = 18;

	private static final int TEST_OWNER_ID_1 = 1;
	
	private static final int TEST_VISIT_ID_1 = 1;

	private static final int TEST_VISIT_ID_5 = 5;
	
	private static final int TEST_VISIT_ID_7 = 7;


	@Autowired
	private VisitController visitController;
		
	@Autowired
	private PetService petService;

	@Autowired
	private VisitService visitService;
	
	@Autowired
	private MedicalTestService medicalTestService;

	private ModelMap modelMap = new ModelMap();
	
	private BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
	
    @WithMockUser(username="vet1", password="v3terinarian_1", authorities="veterinarian")
    @Test
	void testInitNewVisitFormSuccess() throws Exception {
    	ModelMap modelMap = new ModelMap();
    	String view = visitController.initNewVisitForm(TEST_PET_ID_15, 4, modelMap);
		Assert.assertEquals(view, VIEWS_CREATE_OR_UPDATE_VISIT_FORM);
    	assertNotNull(modelMap.get("visit"));
	}

	@WithMockUser(username="vet2", password="v3terinarian_2", authorities="veterinarian")
    @Test
	void testProcessNewVisitFormSuccess() throws Exception {
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		Pet pet = this.petService.findPetById(TEST_PET_ID_1);
		Visit visit = new Visit();
		visit.setDate(LocalDate.now());
		visit.setDescription("Emergency surgery");
		List<MedicalTest> tests = new ArrayList<>();
		tests.add(medicalTestService.findMedicalTestById(1));
		visit.setMedicalTests(tests);
		visit.setPet(pet);
		String view = visitController.processNewVisitForm(TEST_PET_ID_1, 1, visit, result);
		Assert.assertEquals(view, "redirect:/appointments");
	}

	@WithMockUser(username="vet2", password="v3terinarian_2", authorities="veterinarian")
    @Test
	void testProcessNewVisitFormHasErrors() throws Exception {
		Pet pet = this.petService.findPetById(TEST_PET_ID_1);
		Visit visit = new Visit();
		visit.setDate(LocalDate.now());
		visit.setDescription("");
		visit.setMedicalTests(new ArrayList<>());
		visit.setPet(pet);
		result.reject("description", "no puede estar vacío");
		String view = visitController.processNewVisitForm(TEST_PET_ID_1, 1, visit, result);
		Assert.assertEquals(view, VIEWS_CREATE_OR_UPDATE_VISIT_FORM);
	}

	@WithMockUser(username="owner1", password="0wn3333r_1", authorities="owner")
    @Test
	void testShowVisitSuccessWithOwnerUser() throws Exception {
		String view = visitController.showVisit(TEST_OWNER_ID_1, TEST_VISIT_ID_1, modelMap);
		Assert.assertEquals(view, VIEWS_VISIT_DETAILS);
		assertNotNull(modelMap.get("visit"));
	}
	
	@WithMockUser(username="admin1", password="4dm1n", authorities="admin")
    @Test
	void testShowVisitSuccessWithAdminUser() throws Exception {
		String view = visitController.showVisit(TEST_OWNER_ID_1, TEST_VISIT_ID_1, modelMap);
		Assert.assertEquals(view, VIEWS_VISIT_DETAILS);
		assertNotNull(modelMap.get("visit"));
	}
	
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities="veterinarian")
    @Test
	void testShowVisitWithoutAuthorities() throws Exception {
		String view = visitController.showVisit(TEST_OWNER_ID_1, TEST_VISIT_ID_5, modelMap);
		Assert.assertEquals(view, REDIRECT_TO_OUPS);
	}
	
	@WithMockUser(username="vet1", password="v3terinarian_1", authorities="veterinarian")
    @Test
	void testShowVisitWithoutAuthoritiesOrUsername() throws Exception {
		String view = visitController.showVisit(TEST_OWNER_ID_1, TEST_VISIT_ID_5, modelMap);
		Assert.assertEquals(view, REDIRECT_TO_OUPS);
	}
	
	@WithMockUser(username="owner2", password="0wn3333r_2", authorities="owner")
	@Test
	void testShowVisitWithWrongOwner() throws Exception {
		String view = visitController.showVisit(TEST_OWNER_ID_1, TEST_VISIT_ID_1, modelMap);
		Assert.assertEquals(view, REDIRECT_TO_OUPS);
	}
	
	@WithMockUser(username="vet1", password="v3terinarian_1", authorities="veterinarian")
    @Test
	void testInitUpdateVisitFormSuccess() throws Exception {
		String view = visitController.initUpdateVisitForm(TEST_PET_ID_12, TEST_VISIT_ID_7, modelMap);
		Assert.assertEquals(view, VIEWS_CREATE_OR_UPDATE_VISIT_FORM);
    	assertNotNull(modelMap.get("visit"));
	}
	
	@WithMockUser(username="vet1", password="v3terinarian_1", authorities="veterinarian")
    @Test
	void testProcessUpdateVisitFormSuccess() throws Exception {
		Visit visit = this.visitService.findVisitById(TEST_VISIT_ID_7);
		visit.setDescription("Emergency surgery");
		List<MedicalTest> tests = new ArrayList<>();
		tests.add(medicalTestService.findMedicalTestById(1));
		visit.setMedicalTests(tests);
		String view = visitController.processUpdateVisitForm(TEST_PET_ID_12, TEST_VISIT_ID_7, visit,  result, modelMap);
		Assert.assertEquals(view, "redirect:/vets/pets/"+TEST_PET_ID_12+"/visits");
	}
	
	@WithMockUser(username="vet1", password="v3terinarian_1", authorities="veterinarian")
    @Test
	void testProcessUpdateVisitFormHasErrors() throws Exception {
		Visit visit = this.visitService.findVisitById(TEST_VISIT_ID_7);
		visit.setDescription("");
		List<MedicalTest> tests = new ArrayList<>();
		tests.add(medicalTestService.findMedicalTestById(2));
		visit.setMedicalTests(tests);
		result.reject("description", "no puede estar vacío");
		String view = visitController.processUpdateVisitForm(TEST_PET_ID_12, TEST_VISIT_ID_7, visit,  result, modelMap);
		Assert.assertEquals(view, VIEWS_CREATE_OR_UPDATE_VISIT_FORM);
	}
	
	
}
