package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.Treatment;
import org.springframework.samples.petclinic.model.TreatmentHistory;
import org.springframework.samples.petclinic.repository.TreatmentHistoryRepository;
import org.springframework.samples.petclinic.repository.TreatmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TreatmentService {

    private TreatmentRepository treatmentRepository;

    private TreatmentHistoryRepository treatmentHistoryRepository;

	@Autowired
	public TreatmentService(TreatmentRepository treatmentRepository, TreatmentHistoryRepository treatmentHistoryRepository) {
		this.treatmentRepository = treatmentRepository;
		this.treatmentHistoryRepository = treatmentHistoryRepository;
	}

	public List<Treatment> findCurrentTreatmentsByPet(int petId) {
		return this.treatmentRepository.findCurrenTreatmenttWithMedicineByPet(petId);
	}

	public List<Treatment> findExpiredTreatmentsByPet(int petId) {
		return this.treatmentRepository.findExpiredTreatmentsByPet(petId);
    }

    public Treatment findById(Integer id) {
        return this.treatmentRepository.findById(id);
    }
    
    public List<TreatmentHistory> findHistoryByTreatment(int treatmentId) {
    	return this.treatmentHistoryRepository.findHistoryByTreatment(treatmentId);
    }

	@Transactional
    public void saveTreatment(final Treatment treatment) {
        this.treatmentRepository.save(treatment);
    }

    @Transactional
    public void editTreatment(final Treatment treatment) {
        Treatment treatment2Edit = this.treatmentRepository.findById(treatment.getId());
        TreatmentHistory treatmentCopy = new TreatmentHistory();
        treatmentCopy.setName(treatment2Edit.getName());
        treatmentCopy.setDescription(treatment2Edit.getDescription());
        treatmentCopy.setTimeLimit(treatment2Edit.getTimeLimit());
        treatmentCopy.setTreatment(treatment);
        treatmentCopy.setMedicines(medicinesToString(treatment2Edit.getMedicines()));
        treatmentCopy.setPetId(treatment2Edit.getPet().getId());
        this.treatmentHistoryRepository.save(treatmentCopy);
        treatment.setPet(treatment2Edit.getPet());
        saveTreatment(treatment);
    }
    
    public TreatmentHistory findTreatmentHistoryById(int treatmentHistoryId) {
		return this.treatmentHistoryRepository.findById(treatmentHistoryId);
	}
    
    @Transactional
    public void deleteTreatmentHistoryRegister(final TreatmentHistory register) {
    	this.treatmentHistoryRepository.delete(register);
    }

    private String medicinesToString(List<Medicine> medicines) {
        String res = "";
        int size = medicines.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                res += "("+medicines.get(i).getCode()+")-"+medicines.get(i).getName();
                if (i < size-1) {
                    res += "#";
                }
            }
        }
        return res;
    }

}
