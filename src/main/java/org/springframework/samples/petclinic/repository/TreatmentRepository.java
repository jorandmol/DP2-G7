package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Treatment;
import org.springframework.stereotype.Repository;

@Repository
public interface TreatmentRepository {

	List<Treatment> findCurrentTreatmentsByPet(@Param("petId") Integer petId);

	List<Treatment> findExpiredTreatmentsByPet(@Param("petId") Integer petId);

    Treatment findById(Integer id);

    void save(Treatment treatment);
    
	List<Treatment> findCurrenTreatmenttWithMedicineByPet(@Param("petId") Integer petId);

}
