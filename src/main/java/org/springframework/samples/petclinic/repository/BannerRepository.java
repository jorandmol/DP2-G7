package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Banner;

public interface BannerRepository {

	Collection<Banner> findAll() throws DataAccessException;

	void save(@Valid Banner banner) throws DataAccessException;

	void deleteById(int bannerId) throws DataAccessException;
	
	Banner findRandomBanner() throws DataAccessException;

}
