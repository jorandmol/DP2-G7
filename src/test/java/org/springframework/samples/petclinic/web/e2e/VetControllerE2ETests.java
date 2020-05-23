package org.springframework.samples.petclinic.web.e2e;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class VetControllerE2ETests {
	
	private static final int TEST_VET_ID_1 = 1;
	private static final int TEST_VET_ID_2 = 2;
	private static final int TEST_VET_ID_3 = 3;
	private static final int TEST_VET_ID_4 = 4;
	private static final int TEST_VET_ID_5 = 5;
	
	private static final int TEST_PET_ID_1 = 1;

	private static final String REDIRECT_TO_OUPS = "redirect:/oups";
	private static final String VIEWS_VET_CREATE_OR_UPDATE_FORM = "vets/createOrUpdateVetForm";

	private Vet vet3 ;
	
	@Autowired
	private MockMvc mockMvc;
	
	@BeforeEach
	void setup() {

	Specialty surgery= new Specialty();
	Specialty dentistry= new Specialty();
	Specialty nutrition= new Specialty();
	dentistry.setName("dentistry");
	dentistry.setId(3);
	surgery.setName("surgery");
	surgery.setId(2);
	nutrition.setName("nutrition");
	nutrition.setId(7);
	
	vet3 = new Vet();
	User user3 = new User();
	user3.setUsername("vet3");
	user3.setPassword("veterinarian_3");
	vet3.setUser(user3);
	vet3.setId(TEST_VET_ID_3);
	vet3.setFirstName("Juan");
	vet3.setLastName("Ortega");
	vet3.setAddress("110 W. Liberty St.");
	vet3.setCity("Madison");
	vet3.setTelephone("608555102");
	vet3.addSpecialty(surgery);
	vet3.addSpecialty(dentistry);
	
	}
	
	// TEST para usuario que SI cumple la seguridad
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testShowVetList() throws Exception {
		mockMvc.perform(get("/vets"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("vets"))
				.andExpect(view().name("vets/vetList"));
	}
	
	// TEST para usuario que NO cumple la seguridad
	@WithMockUser(username = "vet1", password = "veter1n4ri0_1", authorities = "veterinarian")
	@Test
	void testShowVetListWithoutAccess() throws Exception {
		mockMvc.perform(get("/vets"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}

	
	// TEST para usuario que SI cumple la seguridad
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/vets/new"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("vet"))
				.andExpect(view().name(VIEWS_VET_CREATE_OR_UPDATE_FORM));
	}

	// TEST para usuario que NO cumple la seguridad
	@WithMockUser(username = "vet1", password = "veter1n4ri0_1", authorities = "veterinarian")
	@Test
	void testInitCreationFormWithoutAccess() throws Exception {
		mockMvc.perform(get("/vets/new"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}

	

	// TEST para usuario que SI cumple la seguridad
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/vets/new").with(csrf())
				.param("firstName", "Jorge")
				.param("lastName", "Martín")
				.param("city", "Cáceres")
				.param("address", "C/Almendralejo, 44")
				.param("telephone", "636211578")
				.param("user.username", "yorch06")
				.param("user.password", "jcveterin_06"))
				.andExpect(status().is3xxRedirection());
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessCreationFormCatchException() throws Exception {
		mockMvc.perform(post("/vets/new").with(csrf())
				.param("firstName", "Jorge")
				.param("lastName", "Martín")
				.param("city", "Cáceres")
				.param("address", "C/Almendralejo, 44")
				.param("telephone", "636211578")
				.param("user.username", "vet1")
				.param("user.password", "jcveterin_06"))
				.andExpect(model().errorCount(1))
				.andExpect(model().hasErrors())
				.andExpect(view().name(VIEWS_VET_CREATE_OR_UPDATE_FORM));
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@ParameterizedTest
	@CsvSource({
		"'','Llano','Avenida de la Paz,30','Madrid','636566789','juanito1','veterinarian_juan1'",
		"'Marta','','Avenida de la Constitucion,30','Badajoz','636988789','marta1','veterinarian_marta1'",
		"'Lucia','Belaire','','Madrid','636514437','lucia1','veterinarian_lucia1'",
		"'Marco','Ortiz','c/San Antonio,3','','636598701','marco1','veterinarian_marco1'",
		"'Rocio','Virues','c/America,10','Cordoba','','ro1','veterinarian_rocio1'",
		"'Maria','Ponce','c/Los naranjos,15','Madrid','63656','maria1','veterinarian_maria1'",
		"'Julia','Salgero','c/Real,20','Sevilla','636598789','julia1',''",
		"'Antonio','Alberto','Avenida Reina Mercedes,1','Sevilla','621566789','antonio1','veterinarian_'"
	})
	void testProcessCreationFormHasErrors(String firstname, String lastname, String address, String city, String telephone, String username, String password) throws Exception {
		mockMvc.perform(post("/vets/new").with(csrf())
				.param("firstName", firstname)
				.param("lastName", lastname)
				.param("address", address)
				.param("city", city)
				.param("telephone", telephone)
				.param("user.username", username)
				.param("user.password", password))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("vet"))
				.andExpect(view().name(VIEWS_VET_CREATE_OR_UPDATE_FORM));
	}
	
	// TEST para usuario que NO cumple la seguridad
	@WithMockUser(username = "vet1", password = "v3terinarian_1", authorities = "veterinarian")
	@Test
	void testProcessCreationFormWithoutSecurity() throws Exception {
		mockMvc.perform(post("/vets/new").with(csrf())
				.param("firstName", "Jorge")
				.param("lastName", "Martín")
				.param("city", "Cáceres")
				.param("address", "C/Almendralejo, 44")
				.param("telephone", "636211578")
				.param("user.username", "yorch06")
				.param("user.password", "jcveterin_06"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}
	
	
	
	
	// TEST para usuarios que SI cumple la seguridad
	@WithMockUser(username = "vet1", password = "veter1n4ri0_1", authorities = "veterinarian")
	@Test
	void testInitUpdateProfileForm() throws Exception {
		mockMvc.perform(get("/vets/{vetId}/edit", TEST_VET_ID_1))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("edit"))
				.andExpect(model().attributeExists("vet"))
				.andExpect(model().attribute("vet", hasProperty("firstName", is("James"))))
				.andExpect(model().attribute("vet", hasProperty("lastName", is("Carter"))))
				.andExpect(model().attribute("vet", hasProperty("address", is("110 W. Liberty St."))))
				.andExpect(model().attribute("vet", hasProperty("city", is("Madison"))))
				.andExpect(model().attribute("vet", hasProperty("telephone", is("608555102"))))
				.andExpect(view().name(VIEWS_VET_CREATE_OR_UPDATE_FORM));
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testInitUpdateVetForm() throws Exception {
		mockMvc.perform(get("/vets/{vetId}/edit", TEST_VET_ID_1))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("edit"))
				.andExpect(model().attributeExists("vet"))
				.andExpect(model().attribute("vet", hasProperty("firstName", is("James"))))
				.andExpect(model().attribute("vet", hasProperty("lastName", is("Carter"))))
				.andExpect(model().attribute("vet", hasProperty("address", is("110 W. Liberty St."))))
				.andExpect(model().attribute("vet", hasProperty("city", is("Madison"))))
				.andExpect(model().attribute("vet", hasProperty("telephone", is("608555102"))))
				.andExpect(view().name(VIEWS_VET_CREATE_OR_UPDATE_FORM));
	}
	
	// TEST para usuarios que NO cumplen la seguridad
	@WithMockUser(username = "vet2", password = "veter1n4ri0_2", authorities = "veterinarian")
	@Test
	void testInitUpdateVetFormWithoutAccess() throws Exception {
		mockMvc.perform(get("/vets/{vetId}/edit", TEST_VET_ID_1))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}
	
	
	
	
	// TEST para usuarios que SI cumple la seguridad
	@WithMockUser(username = "vet2", password = "veter1n4ri0_2", authorities = "veterinarian")
	@Test
	void testProcessUpdateProfileFormSuccess() throws Exception {
		mockMvc.perform(post("/vets/{vetId}/edit", TEST_VET_ID_2).with(csrf())
				.param("firstName", "Helen")
				.param("lastName", "Bloggs")
				.param("telephone", "123456789")
				.param("user.password", "v3terinarian_2")
				.param("address", "c/Andalusia, 45")
				.param("city", "Sevilla"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/vets/{vetId}"));

	}
		
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessUpdateVetFormSuccess() throws Exception {		
		
		mockMvc.perform(post("/vets/{vetId}/edit", TEST_VET_ID_3).with(csrf())
				.flashAttr("vet", vet3))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/vets/{vetId}"));
	}

	@WithMockUser(username = "vet1", password = "veter1n4ri0_1", authorities = "veterinarian")
	@ParameterizedTest
	@CsvSource({
		"'','Llano','Avenida de la Paz,30','Madrid','636566789','juanito1','veterinarian_juan1'",
		"'Marta','','Avenida de la Constitucion,30','Badajoz','636988789','marta1','veterinarian_marta1'",
		"'Lucia','Belaire','','Madrid','636514437','lucia1','veterinarian_lucia1'",
		"'Marco','Ortiz','c/San Antonio,3','','636598701','marco1','veterinarian_marco1'",
		"'Rocio','Virues','c/America,10','Cordoba','','ro1','veterinarian_rocio1'",
		"'Maria','Ponce','c/Los naranjos,15','Madrid','63656','maria1','veterinarian_maria1'",
		"'Julia','Salgero','c/Real,20','Sevilla','636598789','julia1',''",
		"'Antonio','Alberto','Avenida Reina Mercedes,1','Sevilla','621566789','antonio1','veterinarian_'"
	})
	void testProcessUpdateFormHasErrors(String firstname, String lastname, String address, String city, String telephone, String username, String password) throws Exception {
		mockMvc.perform(post("/vets/{vetId}/edit", TEST_VET_ID_1).with(csrf())
				.param("firstName", firstname)
				.param("lastName", lastname)
				.param("address", address)
				.param("city", city)
				.param("telephone", telephone)
				.param("user.username", username)
				.param("user.password", password))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("vet"))
				.andExpect(view().name(VIEWS_VET_CREATE_OR_UPDATE_FORM));
	}
	
	// TEST para usuario que NO cumple la seguridad
	@WithMockUser(username = "vet2", password = "veter1n4ri0_2", authorities = "veterinarian")
	@Test
	void testProcessUpdateVetFormSuccessWithoutAccess() throws Exception {
		mockMvc.perform(post("/vets/{vetId}/edit", TEST_VET_ID_5).with(csrf())
				.param("firstName", "Rafael")
				.param("lastName", "Bloggs")
				.param("address", "123 Caramel Street")
				.param("city", "London")
				.param("telephone", "123456789")
				.param("user.password", "str0ng_passw0rd"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}
	
	
	
	
	@WithMockUser(username = "vet1", password = "veter1n4ri0_1", authorities = "veterinarian")
	@Test
	void testShowAppoimentsByVetList() throws Exception {
		mockMvc.perform(get("/appointments"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("appointmentsToday"))
				.andExpect(model().attributeExists("nextAppointments"))
				.andExpect(model().attributeExists("appointmentsWithVisit"))
				.andExpect(view().name("vets/appointmentsList"));
	}

	
	
	// TEST para usuarios que SI cumple la seguridad
	@WithMockUser(username = "vet4", password = "veter1n4ri0_4", authorities = "veterinarian")
	@Test
	void testShowVetProfile() throws Exception {
		mockMvc.perform(get("/vets/{vetId}", TEST_VET_ID_4)).andExpect(status().isOk())
				.andExpect(model().attribute("vet", hasProperty("firstName", is("Rafael"))))
				.andExpect(model().attribute("vet", hasProperty("lastName", is("Ortega"))))
				.andExpect(model().attribute("vet", hasProperty("address", is("110 W. Liberty St."))))
				.andExpect(model().attribute("vet", hasProperty("city", is("Madison"))))
				.andExpect(model().attribute("vet", hasProperty("telephone", is("608555102"))))
				.andExpect(view().name("vets/vetDetails"));
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testShowVet() throws Exception {
		mockMvc.perform(get("/vets/{vetId}", TEST_VET_ID_4)).andExpect(status().isOk())
				.andExpect(model().attribute("vet", hasProperty("firstName", is("Rafael"))))
				.andExpect(model().attribute("vet", hasProperty("lastName", is("Ortega"))))
				.andExpect(model().attribute("vet", hasProperty("address", is("110 W. Liberty St."))))
				.andExpect(model().attribute("vet", hasProperty("city", is("Madison"))))
				.andExpect(model().attribute("vet", hasProperty("telephone", is("608555102"))))
				.andExpect(view().name("vets/vetDetails"));
	}
	
	// TEST para usuarios que NO cumplen la seguridad
	@WithMockUser(username = "vet2", password = "veter1n4ri0_2", authorities = "veterinarian")
	@Test
	void testShowVetWithoutAccess() throws Exception {
		mockMvc.perform(get("/vets/{vetId}", TEST_VET_ID_1))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(REDIRECT_TO_OUPS));
	}
	
	
	
	@WithMockUser(username="vet1", password="veter1n4ri0_1", authorities="veterinarian")
	@Test
	void testShowPetsList() throws Exception {
		mockMvc.perform(get("/vets/pets")).andExpect(status().isOk())
				.andExpect(model().attributeExists("pets"))
				.andExpect(view().name("pets/petsList"));
	}
	
	@WithMockUser(username="vet1", password="veter1n4ri0_1", authorities="veterinarian")
	@Test
	void testShowVisitsList() throws Exception {
		mockMvc.perform(get("/vets/pets/{petId}/visits", TEST_PET_ID_1))
		.andExpect(model().attributeExists("pet"))
		.andExpect(model().attributeExists("visits"))
		.andExpect(status().isOk())
		.andExpect(view().name("visits/visitsList"));
	}

}
