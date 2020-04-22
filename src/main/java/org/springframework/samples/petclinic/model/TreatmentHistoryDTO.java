package org.springframework.samples.petclinic.model;

import java.util.List;

import lombok.Data;

@Data
public class TreatmentHistoryDTO {
	
	private TreatmentHistory treatment;
	private List<String> medicines;
	
	public TreatmentHistoryDTO(TreatmentHistory treatment, List<String> medicines) {
		this.treatment = treatment;
		this.medicines = medicines;
	}
	
}
