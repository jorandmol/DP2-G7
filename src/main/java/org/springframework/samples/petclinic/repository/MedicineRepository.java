package org.springframework.samples.petclinic.repository;



import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Medicine;

public interface MedicineRepository extends CrudRepository<Medicine, Integer>{

	@Query("select m from Medicine m where m.code=?1")
	Collection<Medicine> codeAlreadyExists(String code);
}
