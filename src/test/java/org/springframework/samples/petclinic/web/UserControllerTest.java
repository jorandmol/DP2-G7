package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.BannerService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UserController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class UserControllerTest {
	
	private static final int TEST_OWNER_ID = 1;
	
	private static final int TEST_VET_ID = 1;

	@MockBean
	private UserService userService;
	
	@MockBean
	private BannerService bannerService;
	
	@MockBean
	private VetService vetService;
	
	@MockBean
	private AuthoritiesService authoritiesService;
	
	@MockBean
	private OwnerService ownerService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@BeforeEach
	void setup() {
		
		//Owner1
		User user1 = new User();
		user1.setEnabled(true);
		user1.setUsername("owner1");
		user1.setPassword("0wn3333r_1");
		Owner owner1 = new Owner();
		owner1.setUser(user1);
		owner1.setId(TEST_OWNER_ID);
		this.authoritiesService.saveAuthorities("owner1", "owner");
		given(this.ownerService.findOwnerByUsername("owner1")).willReturn(owner1);

		//Vet1
		User user2 =new User();
		user2.setEnabled(true);
		user2.setUsername("vet1");
		user2.setPassword("V3t_erinarian");
		Vet vet1 = new Vet();
		vet1.setUser(user2);
		vet1.setId(TEST_VET_ID);
		this.authoritiesService.saveAuthorities("vet1", "veterinarian");
		given(this.vetService.findVetByUsername("vet1")).willReturn(vet1);

		
	}
	
	//Usuario con acceso permitido a su perfil
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testFindUserProfileOwner() throws Exception {
		
		mockMvc.perform(get("/users/profile")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/" + TEST_OWNER_ID));
	}
	
	//Usuario con acceso permitido a su perfil
	@WithMockUser(username = "vet1", password = "V3t_erinarian", authorities = "veterinarian")
	@Test
	void testFindUserProfileVet() throws Exception {
		
		mockMvc.perform(get("/users/profile")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/vets/" + TEST_VET_ID));
	}
	
	//Usuario con acceso NO permitido al perfil
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testFindUserProfileVetWithoutAccess() throws Exception {
		
		mockMvc.perform(get("/users/profile")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
}
