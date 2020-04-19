package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Treatment;
import org.springframework.samples.petclinic.repository.TreatmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TreatmentService {

	private TreatmentRepository treatmentRepository;

	@Autowired
	public TreatmentService(TreatmentRepository treatmentRepository) {
		this.treatmentRepository = treatmentRepository;
	}

	public List<Treatment> findCurrentTreatmentsByPet(int petId) {
		return this.treatmentRepository.findCurrentTreatmentsByPet(petId);
	}

	public List<Treatment> findExpiredTreatmentsByPet(int petId) {
		return this.treatmentRepository.findExpiredTreatmentsByPet(petId);
	}

	@Transactional
    public void saveTreatment(final Treatment treatment) {
        this.treatmentRepository.save(treatment);
    }

}
