package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Medicine;

import org.springframework.samples.petclinic.repository.MedicineRepository;


public interface SpringDataMedicineRepository extends MedicineRepository, Repository<Medicine, Integer> {
	
	@Override
	@Query("SELECT medicine FROM Medicine medicine  WHERE medicine.id =:id")
	public Medicine findById(@Param("id") int id);


}
