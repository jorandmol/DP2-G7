package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "treatments_history")
public class TreatmentHistory extends NamedEntity {

    @ManyToOne
    private Treatment treatment;

    private int petId;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate timeLimit;

    private String description;

    private String medicines;
    
    public List<String> getMedicineList() {
    	return Arrays.asList(this.medicines.split("#"));
    }

}
