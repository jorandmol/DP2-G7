package org.springframework.samples.petclinic.web;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.PetRegistrationStatus;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.BannerService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for {@link OwnerController}
 *
 * @author Colin But
 */

@WebMvcTest(controllers = {
		OwnerController.class }, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class OwnerControllerTests {

	private static final int TEST_OWNER_ID1 = 1;
	private static final int TEST_OWNER_ID2 = 2;
	private static final int TEST_OWNER_ID3 = 3;
	
	private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";
	private static final String REDIRECT_TO_OUPS = "redirect:/oups";

	@MockBean
	private OwnerService ownerService;
	
	@MockBean
	private PetService petService;

	@MockBean
	private UserService userService;

	@MockBean
	private BannerService bannerService;

	@MockBean
	private AuthoritiesService authoritiesService;

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private Owner owner1;
	
	@Mock
	private Owner owner3;

	@BeforeEach
	void setup() {

		//owner1
		owner1 = new Owner();
		owner1.setId(TEST_OWNER_ID1);
		owner1.setFirstName("George");
		owner1.setLastName("Franklin");
		owner1.setAddress("110 W. Liberty St.");
		owner1.setCity("Madison");
		owner1.setTelephone("608555102");
		User user1 = new User();
		user1.setUsername("owner1");
		user1.setPassword("0wn3333r_1");
		user1.setEnabled(true);
		owner1.setUser(user1);
		this.authoritiesService.saveAuthorities("owner1", "owner");

		//owner2 Se emplea para los accesos no permitidos por seguridad
		User user2 = new User();
		user2.setUsername("owner2");
		user2.setPassword("0wn3333r_2");
		user2.setEnabled(true);
		Owner owner2 = new Owner();
		owner2.setUser(user2);
		owner2.setId(TEST_OWNER_ID2);
		owner2.setFirstName("Angel");
		owner2.setLastName("Martin");
		this.authoritiesService.saveAuthorities("owner2", "owner");
		
		//owner3 
		owner3 = new Owner();
		owner3.setId(TEST_OWNER_ID3);
		owner3.setFirstName("George");
		owner3.setLastName("Franklin");
		owner3.setAddress("110 W. Liberty St.");
		owner3.setCity("Madison");
		owner3.setTelephone("608555102");
		User user3 = new User();
		user3.setUsername("owner1");
		user3.setPassword("0wn3333r_3");
		user3.setEnabled(true);
		owner3.setUser(user3);
		
		
		given(this.petService.countMyPetsAcceptedByActive(PetRegistrationStatus.ACCEPTED, false, TEST_OWNER_ID1)).willReturn(1);
		given(this.petService.countMyPetsAcceptedByActive(PetRegistrationStatus.ACCEPTED, false, TEST_OWNER_ID2)).willReturn(0);
		given(this.ownerService.findOwnerById(TEST_OWNER_ID1)).willReturn(owner1);
		given(this.ownerService.findOwnerById(TEST_OWNER_ID2)).willReturn(owner2);
		
		Mockito.doThrow(DataIntegrityViolationException.class)
	       .when(this.ownerService)
	       .saveOwner(owner3);

	}

	
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
				.param("firstName", "Joe")
				.param("lastName", "Bloggs").with(csrf())
				.param("address", "123 Caramel Street")
				.param("city", "London")
				.param("telephone", "013167616")
				.param("user.username", "owner23").param("user.password", "0wn333r_23"))
				.andExpect(status().is3xxRedirection());
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessCreationFormCatchException() throws Exception {
		mockMvc.perform(post("/owners/new").with(csrf())
				.flashAttr("owner", owner3))
				.andExpect(status().isOk())
				.andExpect(view().name(VIEWS_OWNER_CREATE_OR_UPDATE_FORM));
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@ParameterizedTest
	@CsvSource({
		"'','Llano','Avenida de la Paz,30','Madrid','636566789','juanito1','owner_juan1'",
		"'Marta','','Avenida de la Constitucion,30','Badajoz','636988789','marta1','owner_marta1'",
		"'Lucia','Belaire','','Madrid','636514437','lucia1','owner_lucia1'",
		"'Marco','Ortiz','c/San Antonio,3','','636598701','marco1','owner_marco1'",
		"'Rocio','Virues','c/America,10','Cordoba','','ro1','owner_rocio1'",
		"'Maria','Ponce','c/Los naranjos,15','Madrid','63656','maria1','owner_maria1'",
		"'Julia','Salgero','c/Real,20','Sevilla','636598789','julia1',''",
		"'Antonio','Alberto','Avenida Reina Mercedes,1','Sevilla','621566789','antonio1','owner_'"
	})
	void testProcessCreationFormHasErrors(String firstname, String lastname, String address, String city, String telephone, String username, String password) throws Exception {
		mockMvc.perform(post("/owners/new").with(csrf())
				.param("firstName", firstname)
				.param("lastName", lastname)
				.param("address", address)
				.param("city", city)
				.param("telephone", telephone)
				.param("user.username", username)
				.param("user.password", password))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("owner"))
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
				.param("user.username", "owner23").param("user.password", "0wn333r_23"))
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
		given(this.ownerService.findOwnerByLastName("")).willReturn(Lists.newArrayList(owner1, new Owner()));

		mockMvc.perform(get("/owners")).andExpect(status().isOk()).andExpect(view().name("owners/ownersList"));
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessFindFormByLastName() throws Exception {
		given(this.ownerService.findOwnerByLastName(owner1.getLastName())).willReturn(Lists.newArrayList(owner1));

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
		given(this.ownerService.findOwnerByLastName("")).willReturn(Lists.newArrayList(owner1, new Owner()));
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
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testProcessUpdateProfileFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/edit", TEST_OWNER_ID1).with(csrf())
				.param("firstName", "Joe")
				.param("lastName", "Bloggs")
				.param("address", "123 Caramel Street")
				.param("city", "London")
				.param("telephone", "016162915")
				.param("user.password", "str0ng-passw0rd"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessUpdateOwnerFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/edit", TEST_OWNER_ID1).with(csrf())
				.param("firstName", "Joe")
				.param("lastName", "Bloggs")
				.param("address", "123 Caramel Street")
				.param("city", "London")
				.param("telephone", "016162915")
				.param("user.password", "str0ng-passw0rd"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@ParameterizedTest
	@CsvSource({
		"'','Llano','Avenida de la Paz,30','Madrid','636566789','owner_juan1'",
		"'Marta','','Avenida de la Constitucion,30','Badajoz','636988789','owner_marta1'",
		"'Lucia','Belaire','','Madrid','636514437','owner_lucia1'",
		"'Marco','Ortiz','c/San Antonio,3','','636598701','owner_marco1'",
		"'Rocio','Virues','c/America,10','Cordoba','','owner_rocio1'",
		"'Maria','Ponce','c/Los naranjos,15','Madrid','63656','owner_maria1'",
		"'Julia','Salgero','c/Real,20','Sevilla','636598789',''",
		"'Antonio','Alberto','Avenida Reina Mercedes,1','Sevilla','621566789','owner_'"
	})
	void testProcessUpdateFormHasErrors(String firstname, String lastname, String address, String city, String telephone, String password) throws Exception {
		mockMvc.perform(post("/owners/new").with(csrf())
				.param("firstName", firstname)
				.param("lastName", lastname)
				.param("address", address)
				.param("city", city)
				.param("telephone", telephone)
				.param("user.password", password))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("owner"))
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
	
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "veterinarian")
	@Test
	void testProcessUpdateOwnerFormSuccessWithoutAuthorities() throws Exception {
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
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
	@Test
	void testShowProfile() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}", TEST_OWNER_ID2)).andExpect(status().isOk())
				.andExpect(model().attributeExists("owner"))
				.andExpect(model().attributeExists("pets"))
				.andExpect(model().attribute("disabled", false))
				.andExpect(view().name("owners/ownerDetails"));
	}
	// TEST para usuario que SI cumple la seguridad
	// TEST para owner que tiene pets disabled
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@ParameterizedTest
	@CsvSource({
		"1, true",
		"2, false"
	})
	void testShowOwnerWithPetsDisabled(Integer TEST_OWNER, Boolean petDisabled) throws Exception {
		mockMvc.perform(get("/owners/{ownerId}", TEST_OWNER)).andExpect(status().isOk())
				.andExpect(model().attributeExists("owner"))
				.andExpect(model().attributeExists("pets"))
				.andExpect(model().attribute("disabled", petDisabled))
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

	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "veterinarian")
	@Test
	void testShowOwnerWithoutAuthorities() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}", TEST_OWNER_ID1))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}


}
