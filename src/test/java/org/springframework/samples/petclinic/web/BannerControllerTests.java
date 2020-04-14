package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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

	private static final int TEST_BANNER_ID1 = 1;
	
	private static final int TEST_BANNER_ID2 = 2;

	@Autowired
	private BannerController bannerController;

	@MockBean
	private BannerService bannerService;

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private Banner banner1;
	
	@Mock
	private Banner banner2;

	@BeforeEach
	void setup() {

		banner1 = new Banner();
		banner1.setId(TEST_BANNER_ID1);
		banner1.setPicture("https://www.us.es/sites/default/files/logoPNG_3.png");
		banner1.setSlogan("us-slogan");
		banner1.setTargetUrl("https://www.us.es/");
		banner1.setOrganizationName("US");
		banner1.setInitColabDate(LocalDate.of(2020, 01, 01));
		banner1.setEndColabDate(LocalDate.of(2020, 10, 01));
		given(this.bannerService.findBannerById(TEST_BANNER_ID1)).willReturn(banner1);
		
		banner2 = new Banner();
		banner2.setId(TEST_BANNER_ID2);
		banner2.setPicture("https://www.us.es/sites/default/files/logoPNG_3.png");
		banner2.setSlogan("us-slogan");
		banner2.setTargetUrl("https://www.us.es/");
		banner2.setOrganizationName("US");
		banner2.setInitColabDate(LocalDate.of(2020, 01, 01));
		banner2.setEndColabDate(LocalDate.of(2020, 03, 01));
		given(this.bannerService.findBannerById(TEST_BANNER_ID2)).willReturn(banner2);
		
		given(this.bannerService.findBanners()).willReturn(new ArrayList<>());

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
				.param("organizationName", "CocaCola ES")
				.param("initColabDate", "2020/01/01")
				.param("endColabDate", "2020/11/01"))
				.andExpect(status().is3xxRedirection());
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/banners/new").with(csrf())
				.param("picture", "https://www.cocacola.es/content/dam/GO/one-brand/RO/updates/67786/coca%20cola%20logo%20260x260-01.png")
				.param("slogan", "").with(csrf())
				.param("targetUrl", "https://www.cocacola.es/es/home/")
				.param("organizationName", "")
				.param("initColabDate", "2020/01/01")
				.param("endColabDate", "2020/11/01"))
				.andExpect(model().attributeHasErrors("banner"))
				.andExpect(model().attributeHasFieldErrors("banner", "slogan"))
				.andExpect(model().attributeHasFieldErrors("banner", "organizationName"))
				.andExpect(view().name("banners/createBannerForm"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasEmptyPicture() throws Exception {
		mockMvc.perform(post("/banners/new").with(csrf())
				.param("picture", "")
				.param("slogan", "SIENTE EL SABOR").with(csrf())
				.param("targetUrl", "https://www.cocacola.es/es/home/")
				.param("organizationName", "CocaCola ES")
				.param("initColabDate", "2020/01/01")
				.param("endColabDate", "2020/11/01"))
				.andExpect(model().attributeHasErrors("banner"))
				.andExpect(model().attributeHasFieldErrors("banner", "picture"))
				.andExpect(view().name("banners/createBannerForm"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasEmptyTargetUrl() throws Exception {
		mockMvc.perform(post("/banners/new").with(csrf())
				.param("picture", "https://www.cocacola.es/content/dam/GO/one-brand/RO/updates/67786/coca%20cola%20logo%20260x260-01.png")
				.param("slogan", "SIENTE EL SABOR").with(csrf())
				.param("targetUrl", "")
				.param("organizationName", "CocaCola ES")
				.param("initColabDate", "2020/01/01")
				.param("endColabDate", "2020/11/01"))
				.andExpect(model().attributeHasErrors("banner"))
				.andExpect(model().attributeHasFieldErrors("banner", "targetUrl"))
				.andExpect(view().name("banners/createBannerForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessDeleteBannerNotExpiredEndDate() throws Exception {
		mockMvc.perform(get("/banners/{bannerId}/delete", TEST_BANNER_ID1))
		.andExpect(status().isOk())
		.andExpect(view().name("banners/bannersList"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessDeleteBannerExpiredEndDate() throws Exception {
		mockMvc.perform(get("/banners/{bannerId}/delete", TEST_BANNER_ID2))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/banners"));
	}
}
