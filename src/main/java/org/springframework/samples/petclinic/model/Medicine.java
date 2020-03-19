package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Data
@Table(name = "medicines")
public class Medicine extends BaseEntity  {
	
	@Column(name = "expiration_date")    
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate expirationDate;
	
	@NotEmpty
	@Column(name = "description")
	private String description;
	
	

	@NotEmpty
	@Column(name = "identificator")
	private String identificator;
	
	
	

}
