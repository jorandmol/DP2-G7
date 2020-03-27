package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Banner;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class BannerServiceTests {

	@Autowired
	protected BannerService bannerService;
	
	@Test
	void shouldFindAllBanners() {
		Collection<Banner> banners = this.bannerService.findBanners();
		assertThat(banners.size()).isEqualTo(4);
	}
	
	@Test
	void shouldFindRandomBanner() {
		Banner banner = this.bannerService.findRandomBanner();
		assertThat(banner).isNotNull();
	}
	
	@Test
	void shouldInsertBanner() {
		Collection<Banner> banners = this.bannerService.findBanners();
		int found = banners.size();
		
		Banner banner = new Banner();
		banner.setPicture("https://www.us.es/sites/default/files/logoPNG_3.png");
		banner.setSlogan("us-slogan");
		banner.setTargetUrl("https://www.us.es/");
		banner.setOrganizationName("US");
		
		this.bannerService.saveBanner(banner);
		assertThat(banner.getId().longValue()).isNotEqualTo(0);
		
		banners = this.bannerService.findBanners();
		assertThat(banners.size()).isEqualTo(found + 1);
	}
	
	@Test
	void shouldDeleteBanner() {
		Collection<Banner> banners = this.bannerService.findBanners();
		int found = banners.size();
		
		Banner banner = this.bannerService.findRandomBanner();
		this.bannerService.deleteBannerById(banner.getId());
		
		banners = this.bannerService.findBanners();
		assertThat(banners.size()).isEqualTo(found - 1);
	}
}
