package org.springframework.samples.petclinic.web.e2e;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class OwnerControllerE2ETests {

	private static final int TEST_OWNER_ID1 = 1;
	private static final int TEST_OWNER_ID2 = 2;
	private static final int TEST_OWNER_ID3 = 3;
	
	private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";
	private static final String REDIRECT_TO_OUPS = "redirect:/oups";
	
	@Autowired
	private MockMvc mockMvc;
	
	// TEST para usuario que SI cumple la seguridad
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/owners/new"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("owner"))
				.andExpect(view().name(VIEWS_OWNER_CREATE_OR_UPDATE_FORM));
	}
		
	// TEST para usuario que NO cumple la seguridad
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
	@Test
	void testInitCreationFormWithoutAccess() throws Exception {
		mockMvc.perform(get("/owners/new"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}
		

	// TESTs para usuarios que SI cumplen la seguridad
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/new")
				.param("firstName", "Jorge")
				.param("lastName", "Lozano").with(csrf())
				.param("address", "123 Caramel Street")
				.param("city", "London")
				.param("telephone", "131676160")
				.param("user.username", "owner23")
				.param("user.password", "0wn333r_23"))
				.andExpect(status().is3xxRedirection());
	}
		

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessCreationFormCatchException() throws Exception {
		mockMvc.perform(post("/owners/new").with(csrf())
				.param("firstName", "Raúl")
				.param("lastName", "Hurtado")
				.param("address", "12 Rosales Street")
				.param("city", "London")
				.param("telephone", "671676160")
				.param("user.username", "owner3")
				.param("user.password", "0wn333r_33"))
				.andExpect(status().isOk())
				.andExpect(view().name(VIEWS_OWNER_CREATE_OR_UPDATE_FORM));
	}
		
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/owners/new").with(csrf())
				.param("firstName", "Joe")
				.param("lastName", "Bloggs")
				.param("address", "123 Caramel Street")
				.param("city", "London")
				.param("telephone", "013167")
				.param("user.username", "owner1")
				.param("user.password", "noNumbersPass_"))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("owner"))
				.andExpect(model().attributeHasFieldErrors("owner", "telephone"))
				.andExpect(model().attributeHasFieldErrors("owner", "user.password"))
				.andExpect(view().name(VIEWS_OWNER_CREATE_OR_UPDATE_FORM));
	}
	
	// TEST para usuario que NO cumple la seguridad
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
	@Test
	void testProcessCreationFormSuccessWithoutAccess() throws Exception {
		mockMvc.perform(post("/owners/new")
				.param("firstName", "Joe")
				.param("lastName", "Bloggs").with(csrf())
				.param("address", "123 Caramel Street")
				.param("city", "London")
				.param("telephone", "013167616")
				.param("user.username", "owner20").param("user.password", "0wn333r_20"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}
	
	
		
	
	// TEST para usuario que SI cumple la seguridad
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testInitFindForm() throws Exception {
		mockMvc.perform(get("/owners/find"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("owner"))
				.andExpect(view().name("owners/findOwners"));
	}
		
	// TEST para usuario que NO cumple la seguridad
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
	@Test
	void testInitFindFormWithoutSecurity() throws Exception {
		mockMvc.perform(get("/owners/find"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}
	
	
	
	
	// TESTs para usuarios que SI cumplen la seguridad
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessFindFormSuccess() throws Exception {
		mockMvc.perform(get("/owners"))
				.andExpect(status().isOk())
				.andExpect(view().name("owners/ownersList"));
	}
		
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessFindFormByLastName() throws Exception {
		mockMvc.perform(get("/owners").param("lastName", "Franklin")).andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/owners/" + TEST_OWNER_ID1));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessFindFormNoOwnersFound() throws Exception {
		mockMvc.perform(get("/owners")
				.param("lastName", "Unknown Surname"))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasFieldErrors("owner", "lastName"))
				.andExpect(model().attributeHasFieldErrorCode("owner", "lastName", "notFound"))
				.andExpect(view().name("owners/findOwners"));
	}
	
	// TEST para usuario que NO cumple la seguridad
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
	@Test
	void testProcessFindFormSuccessWithoutAccess() throws Exception {
		mockMvc.perform(get("/owners"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}
	
	
	
	// TESTs para usuarios que SI cumplen la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testInitUpdateProfileForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/edit", TEST_OWNER_ID1)).andExpect(status().isOk())
				.andExpect(model().attributeExists("edit")).andExpect(model().attributeExists("owner"))
				.andExpect(model().attribute("owner", hasProperty("lastName", is("Franklin"))))
				.andExpect(model().attribute("owner", hasProperty("firstName", is("George"))))
				.andExpect(model().attribute("owner", hasProperty("address", is("110 W. Liberty St."))))
				.andExpect(model().attribute("owner", hasProperty("city", is("Madison"))))
				.andExpect(model().attribute("owner", hasProperty("telephone", is("608555102"))))
				.andExpect(view().name(VIEWS_OWNER_CREATE_OR_UPDATE_FORM));
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testInitUpdateOwnerForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/edit", TEST_OWNER_ID1))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("edit"))
				.andExpect(model().attributeExists("owner"))
				.andExpect(model().attribute("owner", hasProperty("lastName", is("Franklin"))))
				.andExpect(model().attribute("owner", hasProperty("firstName", is("George"))))
				.andExpect(model().attribute("owner", hasProperty("address", is("110 W. Liberty St."))))
				.andExpect(model().attribute("owner", hasProperty("city", is("Madison"))))
				.andExpect(model().attribute("owner", hasProperty("telephone", is("608555102"))))
				.andExpect(view().name(VIEWS_OWNER_CREATE_OR_UPDATE_FORM));
	}
		
	// TEST para usuarios que NO cumplen la seguridad
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
	@Test
	void testInitUpdateOwnerFormWithoutAccess() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/edit", TEST_OWNER_ID1))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}
		
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "veterinarian")
	@Test
	void testInitUpdateOwnerFormWithoutAuthorities() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/edit", TEST_OWNER_ID1))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}
	
	
	
	
	// TESTs para usuarios que SI cumplen la seguridad
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
	@Test
	void testProcessUpdateProfileFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/edit", TEST_OWNER_ID2).with(csrf())
				.param("firstName", "Betty")
				.param("lastName", "Davis")
				.param("address", "3, Los Rosales Street")
				.param("city", "London")
				.param("telephone", "545622389")
				.param("user.password", "own3333r_2"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessUpdateOwnerFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/edit", TEST_OWNER_ID3).with(csrf())
				.param("firstName", "Eduardo")
				.param("lastName", "Rodriquez")
				.param("address", "2693 Commerce St.")
				.param("city", "McFarland")
				.param("telephone", "636788921")
				.param("user.password", "own3333r_3"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
	}
		
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testProcessUpdateOwnerFormHasErrors() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/edit", TEST_OWNER_ID1).with(csrf())
				.param("firstName", "")
				.param("lastName", "Bloggs")
				.param("address", "123 Caramel Street")
				.param("city", "London")
				.param("telephone", "123456")
				.param("user.password", "noNumbersPass_"))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("owner"))
				.andExpect(model().attributeHasFieldErrors("owner", "firstName"))
				.andExpect(model().attributeHasFieldErrors("owner", "telephone"))
				.andExpect(model().attributeHasFieldErrors("owner", "user.password"))
				.andExpect(view().name(VIEWS_OWNER_CREATE_OR_UPDATE_FORM));
	}
		
	// TEST para usuarios que NO cumplen la seguridad
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
	@Test
	void testProcessUpdateOwnerFormSuccessWithoutAccess() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/edit", TEST_OWNER_ID1).with(csrf())
				.param("firstName", "Joe")
				.param("lastName", "Bloggs")
				.param("address", "123 Caramel Street")
				.param("city", "London")
				.param("telephone", "016162915")
				.param("user.password", "str0ng-passw0rd"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}


	

	// TESTs para usuarios que SI cumplen la seguridad
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testShowProfile() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}", TEST_OWNER_ID1)).andExpect(status().isOk())
				.andExpect(model().attributeExists("owner"))
				.andExpect(model().attributeExists("pets"))
				.andExpect(model().attribute("disabled", false))
				.andExpect(view().name("owners/ownerDetails"));
	}
	
	// TEST para usuario que SI cumple la seguridad
	// TEST para owner que tiene pets disabled
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testShowOwnerWithPetsDisabled() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}", TEST_OWNER_ID3)).andExpect(status().isOk())
				.andExpect(model().attributeExists("owner"))
				.andExpect(model().attributeExists("pets"))
				.andExpect(model().attribute("disabled", true))
				.andExpect(view().name("owners/ownerDetails"));
	}

	// TEST para usuario que NO cumple la seguridad
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
	@Test
	void testShowOwnerWithoutAccess() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}", TEST_OWNER_ID1))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}

}
