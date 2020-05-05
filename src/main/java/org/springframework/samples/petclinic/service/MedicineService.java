package org.springframework.samples.petclinic.service;



import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.repository.MedicineRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedMedicineCodeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service
public class MedicineService {
	
	private MedicineRepository medicineRepository;
	
	@Autowired
	public MedicineService(MedicineRepository medicineRepository) {
		this.medicineRepository = medicineRepository;
	}
	
	@Transactional
	public void saveMedicine(Medicine medicine) throws DuplicatedMedicineCodeException {
		String code = medicine.getCode();	
		if (StringUtils.hasLength(code) && this.codeAlreadyExists(code)) {            	
            throw new DuplicatedMedicineCodeException();
        } else {
             this.medicineRepository.save(medicine);  
        }
	}

	public Medicine findMedicineById(int id) {
		return medicineRepository.findById(id);
	}

	public Collection<Medicine> findAll() {
		return this.medicineRepository.findAll();
	}

	public boolean codeAlreadyExists(String code) {
		return this.medicineRepository.findByCode(code) != null;
	}

	@Transactional
	public void editMedicine(final Medicine medicine) throws DuplicatedMedicineCodeException{
		Medicine medicineToUpdate = this.findMedicineById(medicine.getId());
		String newCode = medicine.getCode();
		if(this.codeAlreadyExists(newCode) && !newCode.equals(medicineToUpdate.getCode())) {
			throw new DuplicatedMedicineCodeException();
		} else {
			this.medicineRepository.save(medicine);
		}
	}

}
