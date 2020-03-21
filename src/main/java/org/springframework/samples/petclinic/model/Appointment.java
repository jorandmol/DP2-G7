
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "appointments")
public class Appointment extends BaseEntity {

	@Column(name = "appointment_date")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@FutureOrPresent
	private LocalDate	appointmentDate;

	@Column(name = "appointment_request_date")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate	appointmentRequestDate;

	@Column(name = "description")
	@NotEmpty
	private String		description;

	@ManyToOne
	private Owner		owner;

	@ManyToOne
	private Pet			pet;

	@ManyToOne
	private Vet			vet;


	public void addPet(final Pet pet) {
		this.setPet(pet);
	}
}
