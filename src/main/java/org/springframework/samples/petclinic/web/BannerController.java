package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Banner;
import org.springframework.samples.petclinic.service.BannerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BannerController {

	private final BannerService bannerService;

	@Autowired
	public BannerController(BannerService bannerService) {
			this.bannerService=bannerService;
	}
	
	@GetMapping(value = { "/Banners" })
	public String showBannerList(Map<String, Object> model) {
		Collection<Banner> banners= this.bannerService.findBanners();
		model.put("banners", banners);
		return "banners/bannersList";
	}
}
