package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.PetTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class PetTypeService {

	private PetTypeRepository petTypeRepository;
	
	@Autowired
	public PetTypeService(PetTypeRepository petTypeRepository) {
		this.petTypeRepository = petTypeRepository;
	}
	
	public void savePetType(PetType petType) {
		petTypeRepository.save(petType);
	}

	public Iterable<PetType> findAll() {
		return this.petTypeRepository.findAll();
	}
	
	public boolean typeAlreadyExists(String typeName) {
		int res = this.petTypeRepository.countTypeName(typeName);
		return res == 0;
	}
}
