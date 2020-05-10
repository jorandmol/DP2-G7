package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.MedicalTest;

public interface MedicalTestRepository {

	Collection<MedicalTest> findAll() throws DataAccessException;
	
	void save(MedicalTest medicalTest) throws DataAccessException;

	MedicalTest findById(int medicalTestId);

}
