package org.springframework.samples.petclinic.repository;



import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Medicine;

public interface MedicineRepository {
	
	void save(Medicine medicine) throws DataAccessException;

	Medicine findById(int id) throws DataAccessException;
	

}
