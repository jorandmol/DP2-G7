package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.TreatmentHistory;

public interface TreatmentHistoryRepository extends Repository<TreatmentHistory, Integer>{

    List<TreatmentHistory> findAll();

    List<TreatmentHistory> findAllByPetId(Integer petId);

    TreatmentHistory findById(Integer id);

    void save(TreatmentHistory treatmentHistory);

    @Query("SELECT th FROM TreatmentHistory th WHERE th.treatment.id=:treatmentId ORDER BY th.id DESC")
	List<TreatmentHistory> findHistoryByTreatment(@Param("treatmentId") int treatmentId);

}
