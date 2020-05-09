package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Banner;
import org.springframework.samples.petclinic.service.BannerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@ControllerAdvice
public class BannerController {

	private final BannerService bannerService;

	private static final String VIEWS_BANNER_CREATE_FORM = "banners/createBannerForm";

	private static final String VIEWS_BANNERS_LIST = "banners/bannersList";

	@Autowired
	public BannerController(BannerService bannerService) {
		this.bannerService = bannerService;
	}

	@InitBinder("banner")
	public void initBannerBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new BannerValidator());
	}

	@GetMapping(value = { "/banners" })
	public String showBannersList(Map<String, Object> model) {
		Collection<Banner> banners = this.bannerService.findBanners();
		model.put("banners", banners);
		return VIEWS_BANNERS_LIST;
	}

	@GetMapping(value = "/banners/new")
	public String initCreationForm(ModelMap model) {
		Banner banner = new Banner();
		model.put("banner", banner);
		return VIEWS_BANNER_CREATE_FORM;
	}

	@PostMapping(value = "/banners/new")
	public String processCreationFrom(@Valid Banner banner, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.put("banner", banner);
			return VIEWS_BANNER_CREATE_FORM;
		} else {
			banner.setInitColabDate(LocalDate.now());
			this.bannerService.saveBanner(banner);
		}
		return "redirect:/banners";
	}

	@GetMapping(value = "/banners/{bannerId}/delete")
	public String processDeleteBanner(@PathVariable("bannerId") int bannerId, ModelMap model) {

		model.addAttribute("banners", this.bannerService.findBanners());
		Banner banner = this.bannerService.findBannerById(bannerId);

		if (!banner.getEndColabDate().isBefore(LocalDate.now())) {
			model.addAttribute("error", "cannot be deleted if the collaboration end date has not expired");
			return VIEWS_BANNERS_LIST;
		} else {
			this.bannerService.deleteBannerById(bannerId);
			return "redirect:/banners";
		}

	}

	@ModelAttribute("bannerPhoto")
	public Banner getBanner() {
		Banner result = this.bannerService.findRandomBanner();
		return result;
	}

}
