package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Treatment;
import org.springframework.samples.petclinic.repository.TreatmentRepository;
import org.springframework.stereotype.Service;

@Service
public class TreatmentService {

	private TreatmentRepository treatmentRepository;
	
	@Autowired
	public TreatmentService(TreatmentRepository treatmentRepository) {
		this.treatmentRepository = treatmentRepository;
	}
	
	public List<Treatment> findTreatmentsByPet(int petId) {
		List<Treatment> treatments = this.treatmentRepository.findTreatmentsByPetId(petId);
		
		return treatments.stream().filter(x->x.getTimeLimit().isAfter(LocalDate.now()))
				.collect(Collectors.toList());
	}
	
}
