package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Banner;

public interface BannerRepository {

	Collection<Banner> findAll() throws DataAccessException;

}
