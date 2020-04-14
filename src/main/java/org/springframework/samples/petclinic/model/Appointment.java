
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "appointments")
public class Appointment extends BaseEntity {

	@Column(name = "appointment_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@FutureOrPresent
	@NotNull
	private LocalDate	appointmentDate;

	@Column(name = "appointment_request_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate	appointmentRequestDate;

	@Column(name = "description")
	@NotEmpty
	private String		description;

	@ManyToOne
	@JoinColumn(name = "owner_id")
	private Owner		owner;

	@ManyToOne
	@JoinColumn(name = "pet_id")
	private Pet			pet;

	@ManyToOne
	@JoinColumn(name = "vet_id")
	private Vet			vet;

}
