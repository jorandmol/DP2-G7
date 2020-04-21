package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.MedicalTest;
import org.springframework.samples.petclinic.repository.MedicalTestRepository;

public interface SpringDataMedicalTestRepository extends MedicalTestRepository, Repository<MedicalTest, Integer> {
	
}
