package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineRepository {

	Medicine findByCode(String code);
	
	void save(Medicine medicine);
	
	Medicine findById(Integer id);
	
	Collection<Medicine> findAll();
}
