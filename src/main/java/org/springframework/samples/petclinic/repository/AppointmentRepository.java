
package org.springframework.samples.petclinic.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends CrudRepository<Appointment, Integer> {

	//	void save(Pet pet) throws DataAccessException;
	
	//	void delete(Appointment appointment) throws DataAccessException;
	
	Appointment findById(int id) throws DataAccessException;
}
