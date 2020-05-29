package org.springframework.samples.petclinic.service;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.samples.petclinic.model.Banner;
import org.springframework.samples.petclinic.repository.BannerRepository;
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

	@Cacheable("cacheFindRandomBanner")
	@Transactional(readOnly = true)
	public Banner findRandomBanner() {
		return this.bannerRepository.findRandomBanner();
	}

	@CacheEvict(cacheNames="cacheFindRandomBanner", allEntries = true)
	@Transactional
	public void saveBanner(@Valid Banner banner) {
		bannerRepository.save(banner);
	}
	
	@CacheEvict(cacheNames="cacheFindRandomBanner", allEntries = true)
	@Transactional
	public void deleteBannerById(int bannerId) {
		this.bannerRepository.deleteById(bannerId);
	}

	public Banner findBannerById(int bannerId) {
		return this.bannerRepository.findById(bannerId);
	}

}
