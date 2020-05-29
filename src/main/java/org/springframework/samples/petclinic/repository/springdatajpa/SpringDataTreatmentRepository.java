package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Treatment;
import org.springframework.samples.petclinic.repository.TreatmentRepository;

public interface SpringDataTreatmentRepository extends TreatmentRepository, Repository<Treatment, Integer> {
	
	@Override
	@Query("SELECT t FROM Treatment t WHERE t.pet.id=:petId AND timeLimit >= CURRENT_DATE ORDER BY timeLimit ASC")
	public List<Treatment> findCurrentTreatmentsByPet(@Param("petId") Integer petId);
	
	@Override
	@Query("SELECT t FROM Treatment t WHERE t.pet.id=:petId AND timeLimit < CURRENT_DATE ORDER BY timeLimit DESC")
	public List<Treatment> findExpiredTreatmentsByPet(@Param("petId") Integer petId);
	
	@Query("SELECT DISTINCT t FROM Treatment t LEFT JOIN FETCH t.medicines m WHERE t.pet.id=:petId AND t.timeLimit >= CURRENT_DATE ORDER BY t.timeLimit ASC")
	public List<Treatment> findCurrenTreatmenttWithMedicineByPet(@Param("petId") Integer petId);
	
}
