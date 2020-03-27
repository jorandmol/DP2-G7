package org.springframework.samples.petclinic.service;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.repository.BannerRepository;
import org.springframework.samples.petclinic.model.Banner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BannerService {

	private BannerRepository bannerRepository;

	@Autowired
	public BannerService(BannerRepository bannerRepository) {
		this.bannerRepository = bannerRepository;
	}

	@Transactional(readOnly = true)
	public Collection<Banner> findBanners() {
		return bannerRepository.findAll();
	}

	@Transactional
	public Banner findRandomBanner() {
		return this.bannerRepository.findRandomBanner();
	}

	@Transactional
	public void saveBanner(@Valid Banner banner) {
		bannerRepository.save(banner);
	}

	@Transactional
	public void deleteBannerById(int bannerId) {
		this.bannerRepository.deleteById(bannerId);
	}

}
