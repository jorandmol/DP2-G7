package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Data
@Table(name = "stays")
public class Stay extends BaseEntity {
	
	@Column(name = "register_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate registerDate;

	@Column(name = "release_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate releaseDate;
	
	@Column(name= "status")
	private Boolean status;

	@ManyToOne
	@JoinColumn(name = "pet_id")
	private Pet pet;

}
