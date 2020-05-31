package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.PetTypeRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PetTypeService {

	private PetTypeRepository petTypeRepository;
	
	@Autowired
	public PetTypeService(PetTypeRepository petTypeRepository) {
		this.petTypeRepository = petTypeRepository;
	}
	
	@CacheEvict(cacheNames = "petTypes", allEntries = true)
	public void addPetType(PetType petType) throws DuplicatedPetNameException{
		if(!typeNameDontExists(petType.getName())) {
			throw new DuplicatedPetNameException();
		} else {
			petTypeRepository.save(petType);
		}
	}

	@Transactional(readOnly = true)
	public Iterable<PetType> findAll() {
		return this.petTypeRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public boolean typeNameDontExists(String typeName) {
		int res = this.petTypeRepository.countTypeName(typeName);
		return res == 0;
	}

	@Transactional(readOnly = true)
	public PetType findById(Integer petTypeId) {

		return this.petTypeRepository.findById(petTypeId).get();
	}

	@Transactional(readOnly = true)
	public PetType findByName(String petType) {
		return this.petTypeRepository.findByName(petType);
	}


}
