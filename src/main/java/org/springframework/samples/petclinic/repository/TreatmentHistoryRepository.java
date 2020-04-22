package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.TreatmentHistory;

public interface TreatmentHistoryRepository extends Repository<TreatmentHistory, Integer>{

    List<TreatmentHistory> findAll();

    List<TreatmentHistory> findAllByPetId(Integer petId);

    TreatmentHistory findById(Integer id);

    void save(TreatmentHistory treatmentHistory);

	List<TreatmentHistory> findHistoryById(int treatmentId);

}
