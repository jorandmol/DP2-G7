package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.repository.MedicineRepository;

public interface SpringDataMedicineRepository extends MedicineRepository, Repository<Medicine, Integer>{
	
	@Override
	@Query("select m from Medicine m where m.code=?1")
	Medicine findByCode(String code);

}
