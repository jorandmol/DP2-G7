package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.samples.petclinic.model.Banner;
import org.springframework.samples.petclinic.repository.BannerRepository;
import org.springframework.data.repository.Repository;

public interface SpringDataBannerRepository extends BannerRepository, Repository<Banner, Integer> {

}
