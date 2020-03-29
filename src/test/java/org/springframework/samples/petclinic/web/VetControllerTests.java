package org.springframework.samples.petclinic.web;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.xml.HasXPath.hasXPath;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.BannerService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for the {@link VetController}
 */
@WebMvcTest(controllers = VetController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class VetControllerTests {

	private static final int TEST_VET_ID = 4;

	@Autowired
	private VetController vetController;

	@MockBean
	private BannerService bannerService;

	@MockBean
	private VetService vetService;

	@MockBean
	private UserService userService;

	@MockBean
	private AuthoritiesService authoritiesService;

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private Vet rafael;

	@BeforeEach
	void setup() {

		Vet james = new Vet();
		james.setFirstName("James");
		james.setLastName("Carter");
		james.setId(1);
		Vet helen = new Vet();
		helen.setFirstName("Helen");
		helen.setLastName("Leary");
		helen.setId(2);
		Specialty radiology = new Specialty();
		radiology.setId(1);
		radiology.setName("radiology");
		helen.addSpecialty(radiology);
		given(this.vetService.findVets()).willReturn(Lists.newArrayList(james, helen));

		rafael = new Vet();
		rafael.setId(TEST_VET_ID);
		rafael.setFirstName("Rafael");
		rafael.setLastName("Ortega");
		rafael.setAddress("110 W. Liberty St.");
		rafael.setCity("Madison");
		rafael.setTelephone("608555102");
		User user = new User();
		user.setUsername("vet1");
		user.setPassword("veter1n4ri0_");
		user.setEnabled(true);
		rafael.setUser(user);
		given(this.vetService.findVetById(TEST_VET_ID)).willReturn(rafael);

	}

	@WithMockUser(value = "spring")
	@Test
	void testShowVetListHtml() throws Exception {
		mockMvc.perform(get("/vets")).andExpect(status().isOk()).andExpect(model().attributeExists("vets"))
				.andExpect(view().name("vets/vetList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowVetListXml() throws Exception {
		mockMvc.perform(get("/vets.xml").accept(MediaType.APPLICATION_XML)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_XML_VALUE))
				.andExpect(content().node(hasXPath("/vets/vetList[id=1]/id")));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/vets/new")).andExpect(status().isOk()).andExpect(model().attributeExists("vet"))
				.andExpect(view().name("vets/createOrUpdateVetForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/vets/new")
				.param("firstName", "Elena")
				.param("id", "100")
				.param("lastName", "Molino")
				.with(csrf())
				.param("address", "38 Avenida América")
				.param("city", "London")
				.param("telephone", "123456789")
				.param("user.username", "vet55")
				.param("user.password", "v3terinario_55"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/vets/100"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/vets/new").with(csrf())
				.param("firstName", "")
				.param("lastName", "Bloggs")
				.param("city", "London")
				.param("user.username", "joeBloggs")
				.param("address", "44, Los Rosales")
				.param("telephone", "1234567")
				.param("user.password", "noNumbersPass_"))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("vet"))
				.andExpect(model().attributeHasFieldErrors("vet", "telephone"))
				.andExpect(model().attributeHasFieldErrors("vet", "user.password"))
				.andExpect(view().name("vets/createOrUpdateVetForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasEmptyFields() throws Exception {
		mockMvc.perform(post("/vets/new").with(csrf())
				.param("firstName", "")
				.param("lastName", "")
				.param("city", "")
				.param("address", "")
				.param("telephone", "")
				.param("user.username", "")
				.param("user.password", ""))
				.andExpect(status().isOk()).andExpect(model().attributeHasErrors("vet"))
				.andExpect(model().attributeHasFieldErrors("vet", "firstName"))
				.andExpect(model().attributeHasFieldErrors("vet", "lastName"))
				.andExpect(model().attributeHasFieldErrors("vet", "city"))
				.andExpect(model().attributeHasFieldErrors("vet", "address"))
				.andExpect(model().attributeHasFieldErrors("vet", "telephone"))
				.andExpect(model().attributeHasFieldErrors("vet", "user.password"))
				.andExpect(view().name("vets/createOrUpdateVetForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateVetForm() throws Exception {
		mockMvc.perform(get("/vets/{vetId}/edit", TEST_VET_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("username"))
				.andExpect(model().attributeExists("edit"))
				.andExpect(model().attributeExists("vet"))
				.andExpect(model().attribute("vet", hasProperty("firstName", is("Rafael"))))
				.andExpect(model().attribute("vet", hasProperty("lastName", is("Ortega"))))
				.andExpect(model().attribute("vet", hasProperty("address", is("110 W. Liberty St."))))
				.andExpect(model().attribute("vet", hasProperty("city", is("Madison"))))
				.andExpect(model().attribute("vet", hasProperty("telephone", is("608555102"))))
				.andExpect(view().name("vets/createOrUpdateVetForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateVetFormSuccess() throws Exception {
		mockMvc.perform(post("/vets/{vetId}/edit", TEST_VET_ID).with(csrf())
				.param("firstName", "Rafael")
				.param("lastName", "Bloggs")
				.param("address", "123 Caramel Street")
				.param("city", "London")
				.param("telephone", "123456789")
				.param("user.password", "holi-Elen4"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/vets/{vetId}"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateVetFormHasErrors() throws Exception {
		mockMvc.perform(post("/vets/{vetId}/edit", TEST_VET_ID).with(csrf())
				.param("firstName", "Joe")
				.param("lastName", "Bloggs")
				.param("telephone", "123456789")
				.param("user.password", "v3terin4ri0_1")
				.param("address", "")
				.param("city", ""))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("vet"))
				.andExpect(model().attributeHasFieldErrors("vet", "address"))
				.andExpect(model().attributeHasFieldErrors("vet", "city"))
				.andExpect(view().name("vets/createOrUpdateVetForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowVet() throws Exception {
		mockMvc.perform(get("/vets/{vetId}", TEST_VET_ID)).andExpect(status().isOk())
				.andExpect(model().attribute("vet", hasProperty("firstName", is("Rafael"))))
				.andExpect(model().attribute("vet", hasProperty("lastName", is("Ortega"))))
				.andExpect(model().attribute("vet", hasProperty("address", is("110 W. Liberty St."))))
				.andExpect(model().attribute("vet", hasProperty("city", is("Madison"))))
				.andExpect(model().attribute("vet", hasProperty("telephone", is("608555102"))))
				.andExpect(view().name("vets/vetDetails"));
	}

}