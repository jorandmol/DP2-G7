package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Treatment;

public interface TreatmentRepository extends CrudRepository<Treatment, Integer>{
	
	@Query("SELECT t FROM Treatment t WHERE t.pet.id=:petId ORDER BY timeLimit asc")
	public List<Treatment> findTreatmentsByPetId(@Param("petId") int petId);
}
