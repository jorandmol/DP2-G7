package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.PetTypeRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.stereotype.Service;

@Service
public class PetTypeService {

	private PetTypeRepository petTypeRepository;
	
	@Autowired
	public PetTypeService(PetTypeRepository petTypeRepository) {
		this.petTypeRepository = petTypeRepository;
	}
	
	public void addPetType(PetType petType) throws DuplicatedPetNameException{
		if(!typeNameDontExists(petType.getName())) {
			throw new DuplicatedPetNameException();
		} else {
			petTypeRepository.save(petType);
		}
	}

	public Iterable<PetType> findAll() {
		return this.petTypeRepository.findAll();
	}
	
	public boolean typeNameDontExists(String typeName) {
		int res = this.petTypeRepository.countTypeName(typeName);
		return res == 0;
	}

	public Optional<PetType> findById(Integer petTypeId) {
		
		return this.petTypeRepository.findById(petTypeId);
	}
}
