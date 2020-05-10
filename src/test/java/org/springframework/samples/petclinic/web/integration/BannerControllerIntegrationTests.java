package org.springframework.samples.petclinic.web.integration;

import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Banner;
import org.springframework.samples.petclinic.web.BannerController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BannerControllerIntegrationTests {

	private static final String VIEWS_BANNER_CREATE_FORM = "banners/createBannerForm";
	
	private static final String VIEWS_BANNERS_LIST = "banners/bannersList";
	
	private static final int TEST_BANNER_ID_1 = 1;
	
	private static final int TEST_BANNER_ID_2 = 3;

	@Autowired
	private BannerController bannerController;

	private ModelMap modelMap = new ModelMap();

	private BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
	
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testShowBannersList() throws Exception {
		modelMap.addAttribute("bannerPhoto", bannerController.getBanner());
		String view = bannerController.showBannersList(modelMap);
		Assert.assertEquals(view, VIEWS_BANNERS_LIST);
		assertNotNull(modelMap.get("banners"));
		assertNotNull(modelMap.get("bannerPhoto"));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testInitCreationForm() throws Exception {
		String view = bannerController.initCreationForm(modelMap);
		Assert.assertEquals(view, VIEWS_BANNER_CREATE_FORM);
		assertNotNull(modelMap.get("banner"));
	}

	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		Banner banner = new Banner();
		banner.setPicture("https://www.cocacola.es/content/dam/GO/one-brand/RO/updates/67786/coca%20cola%20logo%20260x260-01.png");
		banner.setSlogan("SIENTE EL SABOR");
		banner.setTargetUrl("https://www.cocacola.es/es/home/");
		banner.setOrganizationName("CocaCola ES");
		banner.setInitColabDate(LocalDate.of(2020, 1, 1));
		banner.setEndColabDate(LocalDate.of(2020, 11, 1));
		String view = bannerController.processCreationFrom(banner, result, modelMap);
		Assert.assertEquals(view, "redirect:/banners");
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		Banner banner = new Banner();
		banner.setPicture("https://www.cocacola.es/content/dam/GO/one-brand/RO/updates/67786/coca%20cola%20logo%20260x260-01.png");
		banner.setSlogan("");
		banner.setTargetUrl("https://www.cocacola.es/es/home/");
		banner.setOrganizationName("");
		banner.setInitColabDate(LocalDate.of(2020, 1, 1));
		banner.setEndColabDate(LocalDate.of(2020, 11, 1));
		result.reject("slogan", "no puede estar vacío");
		result.reject("organizationName", "no puede estar vacío");
		String view = bannerController.processCreationFrom(banner, result, modelMap);
		Assert.assertEquals(view, VIEWS_BANNER_CREATE_FORM);
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testProcessCreationFormHasEmptyPicture() throws Exception {
		Banner banner = new Banner();
		banner.setPicture("");
		banner.setSlogan("SIENTE EL SABOR");
		banner.setTargetUrl("https://www.cocacola.es/es/home/");
		banner.setOrganizationName("CocaCola ES");
		banner.setInitColabDate(LocalDate.of(2020, 1, 1));
		banner.setEndColabDate(LocalDate.of(2020, 11, 1));
		result.reject("picture", "no puede estar vacío");
		String view = bannerController.processCreationFrom(banner, result, modelMap);
		Assert.assertEquals(view, VIEWS_BANNER_CREATE_FORM);
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testProcessCreationFormHasEmptyTargetUrl() throws Exception {
		Banner banner = new Banner();
		banner.setPicture("https://www.cocacola.es/content/dam/GO/one-brand/RO/updates/67786/coca%20cola%20logo%20260x260-01.png");
		banner.setSlogan("SIENTE EL SABOR");
		banner.setTargetUrl("");
		banner.setOrganizationName("CocaCola ES");
		banner.setInitColabDate(LocalDate.of(2020, 1, 1));
		banner.setEndColabDate(LocalDate.of(2020, 11, 1));
		result.reject("targetUrl", "no puede estar vacío");
		String view = bannerController.processCreationFrom(banner, result, modelMap);
		Assert.assertEquals(view, VIEWS_BANNER_CREATE_FORM);
	}

	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testProcessDeleteBannerNotExpiredEndDate() throws Exception {
		String view = bannerController.processDeleteBanner(TEST_BANNER_ID_1, modelMap);
		Assert.assertEquals(view, VIEWS_BANNERS_LIST);
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testProcessDeleteBannerExpiredEndDate() throws Exception {
		String view = bannerController.processDeleteBanner(TEST_BANNER_ID_2, modelMap);
		Assert.assertEquals(view, "redirect:/banners");
	}
}
