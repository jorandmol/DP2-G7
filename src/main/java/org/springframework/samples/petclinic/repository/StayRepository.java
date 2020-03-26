package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Stay;

public interface StayRepository {
	
	void save(Stay stay) throws DataAccessException;
	
	List<Stay> findByPetId(Integer petId);
	
	void delete(Stay stay) throws DataAccessException;

	Stay findById(int id) throws DataAccessException;

}
