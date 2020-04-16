package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Treatment;
import org.springframework.stereotype.Repository;

@Repository
public interface TreatmentRepository {
	
	List<Treatment> findTreatmentsByPetId(@Param("petId") int petId);
	
}
