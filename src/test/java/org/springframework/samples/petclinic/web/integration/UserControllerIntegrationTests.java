package org.springframework.samples.petclinic.web.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.web.UserController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTests {
	
	private static final int TEST_OWNER_ID = 1;
	
	private static final int TEST_VET_ID = 1;
	
	@Autowired
	private UserController userController;
	
	//Usuario con acceso permitido a su perfil
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testFindUserProfileOwner() throws Exception {	
			
		String view = this.userController.findUser();
		
		assertEquals(view, "redirect:/owners/" + TEST_OWNER_ID);
	}
	
	//Usuario con acceso permitido a su perfil
	@WithMockUser(username = "vet1", password = "V3t_erinarian", authorities = "veterinarian")
	@Test
	void testFindUserProfileVet() throws Exception {
		
		String view = this.userController.findUser();
		
		assertEquals(view, "redirect:/vets/" + TEST_VET_ID);
	}
	
	//Usuario con acceso NO permitido al perfil
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testFindUserProfileVetWithoutAccess() throws Exception {
		
		String view = this.userController.findUser();

		assertEquals(view, "redirect:/oups");
	}
}
