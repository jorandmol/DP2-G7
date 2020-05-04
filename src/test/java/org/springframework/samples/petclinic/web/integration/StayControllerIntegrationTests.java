package org.springframework.samples.petclinic.web.integration;

import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Status;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.StayService;
import org.springframework.samples.petclinic.web.StayController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.servlet.ModelAndView;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StayControllerIntegrationTests {
	
	@Autowired
	private StayController  stayController;

	@Autowired
	private StayService stayService;
	
	@Autowired
	private PetService petService;

	private ModelMap modelMap = new ModelMap();

	private BindingResult result = new MapBindingResult(Collections.emptyMap(), "");

	private static final String VIEWS_STAY_CREATE_OR_UPDATE_FORM = "pets/createOrUpdateStayForm";
	private static final String VIEWS_STAY_CREATE_OR_UPDATE_ADMIN_FORM = "pets/createOrUpdateStayFormAdmin";
	

	@WithMockUser(username = "owner3", password = "0wn3333r_3", authorities = "owner")
    @Test
	void testListStays() throws Exception {
		String view = stayController.initStayList(3, 7, modelMap);
		Assert.assertEquals(view, "pets/staysList");
		assertNotNull(modelMap.get("stays"));
	}
	
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testListStaysWrongOwner() throws Exception {
		String view = stayController.initStayList(3, 7, modelMap);
		Assert.assertEquals(view, "redirect:/oups");
	}
	
	@WithMockUser(username = "owner3", password = "0wn3333r_3", authorities = "owner")
    @Test
	void testInitNewStayForm() throws Exception {
		String view = stayController.initNewStayForm(3, 7, modelMap);
		Assert.assertEquals(view, VIEWS_STAY_CREATE_OR_UPDATE_FORM);
		assertNotNull(modelMap.get("stay"));
	}	
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testInitNewStayFormWrongOwner() throws Exception {
		String view = stayController.initNewStayForm(3, 7, modelMap);
		Assert.assertEquals(view, "redirect:/oups");
		
	}	
	
	@WithMockUser(username = "owner3", password = "0wn3333r_3", authorities = "owner")
    @Test
	void testProcessNewStayFormSuccess() throws Exception {
		Stay stay = new Stay();
		stay.setReleaseDate(LocalDate.of(2021, 10, 11));
		stay.setRegisterDate(LocalDate.of(2021, 10, 10));
		stay.setPet(petService.findPetById(7));
		
		String view = stayController.processNewStayForm(stay, result, 3, 7);

		Assert.assertEquals(view, "redirect:/owners/{ownerId}/pets/{petId}/stays");
	}
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testProcessNewStayFormWrongUser() throws Exception {
		Stay stay = new Stay();
		stay.setReleaseDate(LocalDate.of(2022, 10, 11));
		stay.setRegisterDate(LocalDate.of(2022, 10, 10));
		stay.setPet(petService.findPetById(7));
		
		

		String view = stayController.processNewStayForm(stay, result, 3, 7);

		Assert.assertEquals(view, "redirect:/oups");
	}
		
	@WithMockUser(username = "owner3", password = "0wn3333r_3", authorities = "owner")
	@Test
	void testProcessNewStayFormHasErrors() throws Exception {
		Stay stay = new Stay();
		stay.setReleaseDate(LocalDate.of(2022, 10, 11));
		stay.setPet(petService.findPetById(7));
		
		
        result.reject("registerDate" , "Register date must be included");
		String view = stayController.processNewStayForm(stay, result, 3, 7);

		Assert.assertEquals(view, VIEWS_STAY_CREATE_OR_UPDATE_FORM);
	}
	
	@WithMockUser(username = "owner3", password = "0wn3333r_3", authorities = "owner")
	@Test
	void testProcessNewStayFormHasErrorsMaximumStaysReached() throws Exception {
		
		Stay stay = new Stay();
		stay.setReleaseDate(LocalDate.of(2020, 10, 04));
		stay.setRegisterDate(LocalDate.of(2020, 10, 02));
		stay.setPet(petService.findPetById(7));
		
		
        result.reject("registerDate" , "Register date must be included");
		String view = stayController.processNewStayForm(stay, result, 3, 7);

		Assert.assertEquals(view, VIEWS_STAY_CREATE_OR_UPDATE_FORM);
	
	}
		
	@WithMockUser(username = "owner3", password = "0wn3333r_3", authorities = "owner")
	@Test
	void testProcessDeleteStaySuccess() throws Exception {
	
		String view = stayController.processDeleteForm(3, 3, 7, modelMap).getViewName();
		Assert.assertEquals(view, "pets/staysList");
		
	}
	
	@WithMockUser(username = "owner2", password = "0wn3333r_2", authorities = "owner")
	@Test
	void testProcessDeleteStayWrongUser() throws Exception {

		String view = stayController.processDeleteForm(3, 3, 7, modelMap).getViewName();
		Assert.assertEquals(view,  new ModelAndView("exception").getViewName());

	}
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testProcessDeleteStayErrorAlreadyConfirmed() throws Exception {
		ModelAndView view = stayController.processDeleteForm(1, 1, 1, modelMap);
		Assert.assertEquals(view.getViewName(), "pets/staysList");
		assertNotNull(view.getModel().get("errors"));
		
	}

	@WithMockUser(username = "owner3", password = "0wn3333r_3", authorities = "owner")
    @Test
	void testInitEditStayForm() throws Exception {
		String view = stayController.initStayEditForm(2, 7, 3, modelMap);
		Assert.assertEquals(view, VIEWS_STAY_CREATE_OR_UPDATE_FORM);
		assertNotNull(modelMap.get("stay"));
		
	}	
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testInitEditStayFormWrongOwner() throws Exception {
		String view = stayController.initStayEditForm(2, 7, 3, modelMap);
		Assert.assertEquals(view, "redirect:/oups");

		
	}	
	
	@WithMockUser(username = "owner3", password = "0wn3333r_3", authorities = "owner")
    @Test
	void testProcessEditStayFormSuccess() throws Exception {
		Stay stay = this.stayService.findStayById(2);
		Stay stayCopy = new Stay();
		BeanUtils.copyProperties(stay, stayCopy);
        stayCopy.setReleaseDate(LocalDate.of(2020,10,06));
		String view = this.stayController.processStayEditForm(stayCopy, result, 7, 3, 2, modelMap);
		Assert.assertEquals(view, "redirect:/owners/{ownerId}/pets/{petId}/stays");
		Assert.assertEquals(this.stayService.findStayById(2).getReleaseDate(),LocalDate.of(2020,10,06));

	}
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
    @Test
	void testProcessNewEditFormWrongUser() throws Exception {
		Stay stay = this.stayService.findStayById(2);
		Stay stayCopy = new Stay();
		BeanUtils.copyProperties(stay, stayCopy);
        stayCopy.setReleaseDate(LocalDate.of(2020,10,06));
		String view = this.stayController.processStayEditForm(stayCopy, result, 7, 3, 2, modelMap);
		Assert.assertEquals(view, "redirect:/oups");
	}
		
	@WithMockUser(username = "owner3", password = "0wn3333r_3", authorities = "owner")
	@Test
	void testProcessEditStayFormHasErrors() throws Exception {
		Stay stay = this.stayService.findStayById(2);
		Stay stayCopy = new Stay();
		BeanUtils.copyProperties(stay, stayCopy);
        stayCopy.setReleaseDate(null);
        result.reject("releaseDate","You must include release date for the stay");
		String view = this.stayController.processStayEditForm(stayCopy, result, 7, 3, 2, modelMap);
		Assert.assertEquals(view, VIEWS_STAY_CREATE_OR_UPDATE_FORM);
	}
	
	@WithMockUser(username = "owner1", password = "0wn3333r_1", authorities = "owner")
	@Test
	void testProcessEditStayFormHasAlreadyConfirmed() throws Exception {
		Stay stay = this.stayService.findStayById(1);
		Stay stayCopy = new Stay();
		BeanUtils.copyProperties(stay, stayCopy);
        stayCopy.setReleaseDate(LocalDate.of(2020, 10, 06));
		String view = this.stayController.processStayEditForm(stayCopy, result, 1, 1, 1, modelMap);
		Assert.assertEquals(view, VIEWS_STAY_CREATE_OR_UPDATE_FORM);
	}
	
	@WithMockUser(username = "owner3", password = "0wn3333r_3", authorities = "owner")
	@Test
	void testProcessEditStayFormHasMaximumStaysReached() throws Exception {
		Stay stay = this.stayService.findStayById(2);
		Stay stayCopy = new Stay();
		BeanUtils.copyProperties(stay, stayCopy);
	
		stayCopy.setRegisterDate(LocalDate.of(2020,11,02));
        stayCopy.setReleaseDate(LocalDate.of(2020,11,04));
		String view = this.stayController.processStayEditForm(stayCopy, result, 7, 3, 2, modelMap);
		Assert.assertEquals(view, VIEWS_STAY_CREATE_OR_UPDATE_FORM);
	}
	
	@WithMockUser(username = "owner3", password = "0wn3333r_3", authorities = "owner")
	@Test
	void testProcessEditStayFormHasDateNotAllowed() throws Exception {
		Stay stay = this.stayService.findStayById(2);
		Stay stayCopy = new Stay();
		BeanUtils.copyProperties(stay, stayCopy);

		String view = this.stayController.processStayEditForm(stayCopy, result, 7, 3, 2, modelMap);
		Assert.assertEquals(view, VIEWS_STAY_CREATE_OR_UPDATE_FORM);
	}
	
	// Admin confirms or rejects stays
	
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
    @Test
	void testListStaysAdmin() throws Exception {
		String view = stayController.initStayListForAdm(modelMap);
		Assert.assertEquals(view, "pets/staysListAdmin");
		assertNotNull(modelMap.get("stays"));
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
    @Test
	void testInitEditStatusStayFormAdmin() throws Exception {
	String view = this.stayController.initStayEditFormAdmin(3, modelMap);
	Assert.assertEquals(view, VIEWS_STAY_CREATE_OR_UPDATE_ADMIN_FORM);
	}	
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
    @Test
	void testProcessEditStatusStayFormAdminSuccess() throws Exception {
		Stay stay = this.stayService.findStayById(2);
		Stay stayCopy = new Stay();
		BeanUtils.copyProperties(stay, stayCopy);
        stayCopy.setStatus(Status.ACCEPTED);
		String view = this.stayController.processStayEditFormAdmin(stayCopy, result, 2, modelMap);
		Assert.assertEquals(view, "redirect:/admin/stays");
		Assert.assertEquals(this.stayService.findStayById(2).getStatus(),Status.ACCEPTED);
	
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
    @Test
	void testProcessEditStatusStayFormAdminErrors() throws Exception {
		Stay stay = this.stayService.findStayById(2);
		Stay stayCopy = new Stay();
		BeanUtils.copyProperties(stay, stayCopy ,"registerDate","releaseDate");
        stayCopy.setStatus(Status.ACCEPTED);
        result.reject("registerDate","Must introduce a register date");
        result.reject("releaseDate","Must introduce a release date");
		String view = this.stayController.processStayEditFormAdmin(stayCopy, result, 2, modelMap);
		Assert.assertEquals(view, VIEWS_STAY_CREATE_OR_UPDATE_ADMIN_FORM);
	}
	
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
    @Test
	void testProcessEditStatusStayFormAdminAlreadyConfirmed() throws Exception {
		Stay stay = this.stayService.findStayById(1);
		Stay stayCopy = new Stay();
		BeanUtils.copyProperties(stay, stayCopy);
        stayCopy.setStatus(Status.REJECTED);
		String view = this.stayController.processStayEditFormAdmin(stayCopy, result, 2, modelMap);
		Assert.assertEquals(view, VIEWS_STAY_CREATE_OR_UPDATE_ADMIN_FORM);
	}
	

}
