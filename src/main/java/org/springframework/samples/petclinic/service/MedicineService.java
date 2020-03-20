package org.springframework.samples.petclinic.service;



import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.repository.MedicineRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class MedicineService {
	
	private MedicineRepository medicineRepository;
	

	
	@Autowired
	public MedicineService(MedicineRepository medicineRepository) {
		this.medicineRepository = medicineRepository;

	}
	
	
	
	@Transactional
	public void saveMedicine(Medicine medicine) throws DataAccessException {
		medicineRepository.save(medicine);

	 }



	@Transactional(readOnly = true)
	public Medicine findMedicineById(int id) throws DataAccessException {
		return medicineRepository.findById(id).get();
	}



	public Iterable<Medicine> findAll() {
		return this.medicineRepository.findAll();
	}



	public Boolean codeAlreadyExists(String code) {
		return this.medicineRepository.codeAlreadyExists(code).size()>0;
	}



	public Boolean pastDate(LocalDate expirationDate) {
		return expirationDate.isBefore(LocalDate.now());
	}



}
