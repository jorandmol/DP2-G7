package org.springframework.samples.petclinic.web.e2e;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class MedicalTestControllerE2ETests {

	private static final String VIEWS_MEDICAL_TEST_CREATE_OR_UPDATE_FORM = "medical-tests/createOrUpdateMedicalTestForm";

	private static final String VIEWS_MEDICAL_TEST_LIST = "medical-tests/medicalTestsList";
	
	private static final int TEST_MT_ID = 1;
	
	@Autowired
	private MockMvc mockMvc;

	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testShowMedicalTestsList() throws Exception {
		mockMvc.perform(get("/medical-tests")).andExpect(status().isOk())
				.andExpect(model().attributeExists("medicalTests"))
				.andExpect(view().name(VIEWS_MEDICAL_TEST_LIST));
	}

	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testIinitCreationForm() throws Exception {
		mockMvc.perform(get("/medical-tests/new")).andExpect(status().isOk())
				.andExpect(model().attributeExists("medicalTest"))
				.andExpect(view().name(VIEWS_MEDICAL_TEST_CREATE_OR_UPDATE_FORM));
	}

	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/medical-tests/new").with(csrf())
				.param("name", "Radiography")
				.param("description","Images of the internal structure of the body to assess the presence of foreign objects, and structural damage or anomaly"))
				.andExpect(status().is3xxRedirection());
	}

	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/medical-tests/new").with(csrf())
				.param("name", "Radiography")
				.param("description", ""))
				.andExpect(model().attributeHasErrors("medicalTest"))
				.andExpect(model().attributeHasFieldErrors("medicalTest", "description"))
				.andExpect(view().name(VIEWS_MEDICAL_TEST_CREATE_OR_UPDATE_FORM));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testIinitUpdateForm() throws Exception {
		mockMvc.perform(get("/medical-tests/{medicalTestId}/edit", TEST_MT_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("medicalTest"))
				.andExpect(view().name(VIEWS_MEDICAL_TEST_CREATE_OR_UPDATE_FORM));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		mockMvc.perform(post("/medical-tests/{medicalTestId}/edit", TEST_MT_ID).with(csrf())
				.param("name", "Radiography")
				.param("description","Images of the internal structure of the body"))
				.andExpect(status().is3xxRedirection());
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		mockMvc.perform(post("/medical-tests/{medicalTestId}/edit", TEST_MT_ID).with(csrf())
				.param("name", "Radiography")
				.param("description", ""))
				.andExpect(model().attributeHasErrors("medicalTest"))
				.andExpect(model().attributeHasFieldErrors("medicalTest", "description"))
				.andExpect(view().name(VIEWS_MEDICAL_TEST_CREATE_OR_UPDATE_FORM));
	}
}
