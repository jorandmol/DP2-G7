package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Banner;
import org.springframework.samples.petclinic.repository.BannerRepository;

public interface SpringDataBannerRepository extends BannerRepository, Repository<Banner, Integer> {
	
	@Query("SELECT COUNT(banner) FROM Banner banner")
	int countBanners();

	@Query("SELECT banner FROM Banner banner")
	List<Banner> findManyBanners(PageRequest pageRequest);

	default Banner findRandomBanner() {
		Banner result = null;
		int bannerCount, bannerIndex;
		ThreadLocalRandom random;
		PageRequest page;
		List<Banner> list;

		bannerCount = this.countBanners();

		if (bannerCount > 0) {
			random = ThreadLocalRandom.current();
			bannerIndex = random.nextInt(0, bannerCount);

			page = PageRequest.of(bannerIndex, 1);
			list = this.findManyBanners(page);
			result = list.isEmpty() ? null : list.get(0);
		}

		return result;
	}
	
}
