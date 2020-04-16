package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Treatment;
import org.springframework.samples.petclinic.repository.TreatmentRepository;

public interface SpringDataTreatmentRepository extends TreatmentRepository, Repository<Treatment, Integer> {

	@Override
	@Query("SELECT t FROM Treatment t WHERE t.pet.id=:petId ORDER BY timeLimit asc")
	public List<Treatment> findTreatmentsByPetId(@Param("petId") int petId);
	
}
