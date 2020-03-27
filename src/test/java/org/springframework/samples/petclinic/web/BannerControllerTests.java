package org.springframework.samples.petclinic.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
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
import org.springframework.samples.petclinic.model.Banner;
import org.springframework.samples.petclinic.service.BannerService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = BannerController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class BannerControllerTests {

	private static final int TEST_BANNER_ID = 1;

	@Autowired
	private BannerController bannerController;

	@MockBean
	private BannerService clinicService;

	@Autowired
	private MockMvc mockMvc;

	private Banner banner;

	@BeforeEach
	void setup() {

		banner = new Banner();
		banner.setId(TEST_BANNER_ID);
		banner.setPicture("https://www.us.es/sites/default/files/logoPNG_3.png");
		banner.setSlogan("us-slogan");
		banner.setTargetUrl("https://www.us.es/");
		banner.setOrganizationName("US");
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowBannersList() throws Exception {
		mockMvc.perform(get("/banners")).andExpect(status().isOk()).andExpect(model().attributeExists("banners"))
				.andExpect(view().name("banners/bannersList"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/banners/new")).andExpect(status().isOk()).andExpect(model().attributeExists("banner"))
				.andExpect(view().name("banners/createBannerForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/banners/new")
				.param("picture", "https://www.cocacola.es/content/dam/GO/one-brand/RO/updates/67786/coca%20cola%20logo%20260x260-01.png")
				.param("slogan", "SIENTE EL SABOR").with(csrf())
				.param("targetUrl", "https://www.cocacola.es/es/home/")
				.param("organizationName", "CocaCola ES"))
				.andExpect(status().is3xxRedirection());
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/banners/new").with(csrf())
				.param("picture", "https://www.cocacola.es/content/dam/GO/one-brand/RO/updates/67786/coca%20cola%20logo%20260x260-01.png")
				.param("slogan", "SIENTE EL SABOR"))
				.andExpect(model().attributeHasErrors("banner"))
				.andExpect(model().attributeHasFieldErrors("banner", "targetUrl"))
				.andExpect(model().attributeHasFieldErrors("banner", "organizationName"))
				.andExpect(view().name("banners/createBannerForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessDeleteBanner() throws Exception {
		mockMvc.perform(get("/banners/{bannerId}/delete", TEST_BANNER_ID))
		.andExpect(status().is3xxRedirection());
	}
}
