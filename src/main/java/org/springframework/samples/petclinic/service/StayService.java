package org.springframework.samples.petclinic.service;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.repository.StayRepository;
import org.springframework.stereotype.Service;
@Service
public class StayService {
	

	private StayRepository stayRepository;
	
	@Autowired
	public StayService(StayRepository stayRepository) {
		
		this.stayRepository = stayRepository;
	}
	
	@Transactional
	public void saveStay(Stay stay) throws DataAccessException {
		stayRepository.save(stay);
	}

	public Collection<Stay> findStancesByPetId(int petId) {
		return stayRepository.findByPetId(petId);
	}

}
