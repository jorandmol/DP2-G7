package org.springframework.samples.petclinic.web.e2e;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class TreatmentControllerE2ETests {
	
	private static final String OWNER_ROLE = "owner";
	private static final String VET_ROLE = "veterinarian";

	private static final String VIEWS_TREATMENT_LIST = "treatments/listTreatments";
	private static final String VIEWS_TREATMENT_FORM = "treatments/createOrUpdateTreatmentForm";
	private static final String REDIRECT_TO_OUPS = "redirect:/oups";
	private static final String REDIRECT_TO_VET_TREATMENT_LIST = "redirect:/vets/pets/{petId}/treatments";
	private static final String REDIRECT_TO_TREATMENT_SHOW = "redirect:/vets/pets/{petId}/treatments/{treatmentId}";

	private static final int TEST_OWNER_ID = 1;
    private static final int TEST_VET_ID = 1;
	private static final int TEST_PET_ID = 1;
	private static final int TEST_WRONG_OWNER_ID = 2;
	private static final int TEST_TREATMENT_ID_1 = 1;
	private static final int TEST_TREATMENT_ID_2 = 2;
	private static final int TEST_TREATMENT_HISTORY_ID = 1;

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testListTreatmentsToOwner() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/treatments", TEST_OWNER_ID, TEST_PET_ID))
			.andExpect(model().attributeExists("treatments"))
			.andExpect(model().attributeExists("treatmentsDone"))
			.andExpect(status().isOk())
			.andExpect(view().name(VIEWS_TREATMENT_LIST));
	}

	@Test
	@WithMockUser(username="owner1", password="0wn3333r_1", authorities=OWNER_ROLE)
	void testNotListTreatmentsToOwner() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/treatments", TEST_WRONG_OWNER_ID, TEST_PET_ID))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name(REDIRECT_TO_OUPS));
	}

	@Test
    @WithMockUser(username="vet1", password="v3terinarian_1", authorities=VET_ROLE)
    void testListTreatmentsToVet() throws Exception {
	    mockMvc.perform(get("/vets/pets/{petId}/treatments", TEST_PET_ID))
            .andExpect(model().attributeExists("isVet"))
            .andExpect(model().attributeExists("treatments"))
            .andExpect(model().attributeExists("treatmentsDone"))
            .andExpect(status().isOk())
            .andExpect(view().name(VIEWS_TREATMENT_LIST));
    }

	@Test
	@WithMockUser(username="vet1", password="v3terinarian_1", authorities=VET_ROLE)
	void testInitNewTreatmentForm() throws Exception {
		mockMvc.perform(get("/vets/pets/{petId}/treatments/new", TEST_PET_ID))
			.andExpect(status().isOk())
			.andExpect(view().name(VIEWS_TREATMENT_FORM));
	}

//    @Test
//    @WithMockUser(username="vet1", password="v3terinarian_1", authorities=VET_ROLE)
//    void testProcessNewTreatmentForm() throws Exception {
//	    mockMvc.perform(post("/vets/pets/{petId}/treatments/new", TEST_PET_ID).with(csrf())
//            .flashAttr("treatment", treatment1))
//            .andExpect(status().is3xxRedirection())
//            .andExpect(view().name(REDIRECT_TO_VET_TREATMENT_LIST));
//    }
    
    @Test
    @WithMockUser(username="vet1", password="v3terinarian_1", authorities=VET_ROLE)
    void testInitTreatmentEditForm() throws Exception {
	    mockMvc.perform(get("/vets/pets/{petId}/treatments/{treatmentId}/edit", TEST_PET_ID, TEST_TREATMENT_ID_1))
            .andExpect(model().attributeExists("treatment"))
            .andExpect(model().attributeExists("edit"))
            .andExpect(status().isOk())
            .andExpect(view().name(VIEWS_TREATMENT_FORM));
    }
    
    @Test
    @WithMockUser(username="vet1", password="v3terinarian_1", authorities=VET_ROLE)
    void testNotInitTreatmentEditForm() throws Exception {
	    mockMvc.perform(get("/vets/pets/{petId}/treatments/{treatmentId}/edit", TEST_PET_ID, TEST_TREATMENT_ID_2))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(REDIRECT_TO_OUPS));
    }
    
//    @Test
//    @WithMockUser(username="vet1", password="v3terinarian_1", authorities=VET_ROLE)
//    void testProcessTreatmentEditForm() throws Exception {
//	    mockMvc.perform(post("/vets/pets/{petId}/treatments/{treatmentId}/edit", TEST_PET_ID, TEST_TREATMENT_ID_1).with(csrf())
//	    	.flashAttr("treatment", treatment3))
//		    .andExpect(status().is3xxRedirection())
//	        .andExpect(view().name(REDIRECT_TO_TREATMENT_SHOW));
//    }
    
//    @Test
//    @WithMockUser(username="vet1", password="v3terinarian_1", authorities=VET_ROLE)
//    void testNotProcessTreatmentEditForm() throws Exception {
//    	mockMvc.perform(post("/vets/pets/{petId}/treatments/{treatmentId}/edit", TEST_PET_ID, TEST_TREATMENT_ID_2).with(csrf())
//    	    	.flashAttr("treatment", treatment3))
//    		    .andExpect(status().is3xxRedirection())
//    	        .andExpect(view().name(REDIRECT_TO_OUPS));
//    }
    
    @Test
    @WithMockUser(username="vet1", password="v3terinarian_1", authorities=VET_ROLE)
    void testDeleteTreatmentHistoryRegister() throws Exception {
    	mockMvc.perform(get("/vets/pets/{petId}/treatments/{treatmentId}/history/{treatmentHistoryId}/delete", TEST_PET_ID, TEST_TREATMENT_ID_1, TEST_TREATMENT_HISTORY_ID))
    		.andExpect(status().is3xxRedirection())
    		.andExpect(view().name(REDIRECT_TO_TREATMENT_SHOW));
    }
	
}
