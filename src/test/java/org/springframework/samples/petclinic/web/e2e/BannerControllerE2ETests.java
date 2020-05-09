package org.springframework.samples.petclinic.web.e2e;

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
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class BannerControllerE2ETests {

	private static final String VIEWS_BANNER_CREATE_FORM = "banners/createBannerForm";
	
	private static final String VIEWS_BANNERS_LIST = "banners/bannersList";
	
	private static final int TEST_BANNER_ID_1 = 1;
	
	private static final int TEST_BANNER_ID_2 = 3;

	@Autowired
	private MockMvc mockMvc;
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testShowBannersList() throws Exception {
		mockMvc.perform(get("/banners")).andExpect(status().isOk()).andExpect(model().attributeExists("banners"))
				.andExpect(view().name(VIEWS_BANNERS_LIST));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/banners/new")).andExpect(status().isOk()).andExpect(model().attributeExists("banner"))
				.andExpect(view().name(VIEWS_BANNER_CREATE_FORM));
	}

	@WithMockUser(username="admin1",authorities= {"admin"})
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
	
	@WithMockUser(username="admin1",authorities= {"admin"})
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
				.andExpect(view().name(VIEWS_BANNER_CREATE_FORM));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
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
				.andExpect(view().name(VIEWS_BANNER_CREATE_FORM));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
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
				.andExpect(view().name(VIEWS_BANNER_CREATE_FORM));
	}

	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testProcessDeleteBannerNotExpiredEndDate() throws Exception {
		mockMvc.perform(get("/banners/{bannerId}/delete", TEST_BANNER_ID_1))
		.andExpect(status().isOk())
		.andExpect(view().name(VIEWS_BANNERS_LIST));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testProcessDeleteBannerExpiredEndDate() throws Exception {
		mockMvc.perform(get("/banners/{bannerId}/delete", TEST_BANNER_ID_2))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/banners"));
	}
}
