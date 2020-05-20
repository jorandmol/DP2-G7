package org.springframework.samples.petclinic.web.e2e;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
  webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
/*@TestPropertySource(
  locations = "classpath:application-mysql.properties")*/
public class PetTypeControllerE2ETests {

	@Autowired
	private MockMvc mockMvc;

	private PetType pt;

	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testList() throws Exception {
		mockMvc.perform(get("/pet-type")).andExpect(status().isOk()).andExpect(view().name("pet-type/typeList"))
				.andExpect(model().attributeExists("petTypes"));
	}

	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/pet-type/new")).andExpect(status().isOk()).andExpect(view().name("pet-type/typeForm"))
				.andExpect(model().attributeExists("petType"));
	}

	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/pet-type/new")
				.with(csrf())
				.param("name", "shark"))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/pet-type"));
	}

	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/pet-type/new")
				.with(csrf())
				.param("name", ""))
		.andExpect(status().isOk())
		.andExpect(view().name("pet-type/typeForm"));

	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testProcessCreationFormHasErrorsNameExists() throws Exception {
		mockMvc.perform(post("/pet-type/new")
				.with(csrf())
				.param("name", "bird"))
		.andExpect(status().isOk())
		.andExpect(view().name("pet-type/typeForm"));

	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
    @Test
	void testInitEditPetTypeForm() throws Exception {
		mockMvc.perform(get("/pet-type/{petTypeId}/edit", 1))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("petType"))
				.andExpect(view().name("pet-type/typeForm"));
	}	
	

	
	@WithMockUser(username="admin1",authorities= {"admin"})
    @Test
	void testProcessEditPetTypeFormSuccess() throws Exception {
		mockMvc.perform(post("/pet-type/{petTypeId}/edit",1)
							.with(csrf())
							.param("name", "shark"))  
	            .andExpect(status().isOk())
				.andExpect(view().name("pet-type/typeForm"));
	}
	
	
		
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testProcessEditPetTypeFormHasErrors() throws Exception {
		mockMvc.perform(post("/pet-type/{petTypeId}/edit",1)
							.with(csrf())
	                        .param("releaseDate", ""))  
				.andExpect(model().attributeHasErrors("petType")).andExpect(status().isOk())
				.andExpect(view().name("pet-type/typeForm"));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testProcessEditFormHasRepeatedName() throws Exception {
		
		pt = new PetType();
		pt.setId(1);
		pt.setName("hamster");
		
		mockMvc.perform(post("/pet-type/{petTypeId}/edit",1)
				.with(csrf())
				.flashAttr("petType", pt))
		.andExpect(status().isOk())
		.andExpect(view().name("pet-type/typeForm"));

	}

}
