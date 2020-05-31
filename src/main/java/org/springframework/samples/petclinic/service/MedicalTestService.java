package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.samples.petclinic.model.MedicalTest;
import org.springframework.samples.petclinic.repository.MedicalTestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicalTestService {
	
private MedicalTestRepository medicalTestRepository;
	
	@Autowired
	public MedicalTestService(MedicalTestRepository medicalTestRepository) {
		this.medicalTestRepository = medicalTestRepository;
	}

	@Transactional(readOnly = true)	
	@Cacheable("medicalTests")
	public Collection<MedicalTest> findMedicalTests() {
		return this.medicalTestRepository.findAll();
	}

	@Transactional
	@CacheEvict(cacheNames="medicalTests", allEntries=true)
	public void saveMedicalTest(MedicalTest medicalTest) {
		this.medicalTestRepository.save(medicalTest);
	}

	@Transactional(readOnly = true)
	public MedicalTest findMedicalTestById(int medicalTestId) {
		return this.medicalTestRepository.findById(medicalTestId);
	}
}
