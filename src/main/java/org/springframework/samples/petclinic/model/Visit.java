/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Simple JavaBean domain object representing a visit.
 *
 * @author Ken Krebs
 */
@Entity
@Table(name = "visits")
public class Visit extends BaseEntity {

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "visit_medical_tests", joinColumns = @JoinColumn(name = "visit_id"), inverseJoinColumns = @JoinColumn(name = "medical_test_id"))
	private List<MedicalTest> medicalTests;

	/**
	 * Holds value of property date.
	 */
	@Column(name = "visit_date")        
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate date;

	/**
	 * Holds value of property description.
	 */
	@NotEmpty
	@Column(name = "description")
	private String description;

	/**
	 * Holds value of property pet.
	 */
	@ManyToOne
	@JoinColumn(name = "pet_id")
	private Pet pet;

	/**
	 * Creates a new instance of Visit for the current date
	 */
	public Visit() {
		this.date = LocalDate.now();
	}

	/**
	 * Getter for property date.
	 * @return Value of property date.
	 */
	public LocalDate getDate() {
		return this.date;
	}

	/**
	 * Setter for property date.
	 * @param date New value of property date.
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}

	/**
	 * Getter for property description.
	 * @return Value of property description.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Setter for property description.
	 * @param description New value of property description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Getter for property pet.
	 * @return Value of property pet.
	 */
	public Pet getPet() {
		return this.pet;
	}

	/**
	 * Setter for property pet.
	 * @param pet New value of property pet.
	 */
	public void setPet(Pet pet) {
		this.pet = pet;
	}
	
	protected List<MedicalTest> getMedicalTestsInternal() {
		if (this.medicalTests == null) {
			this.medicalTests = new ArrayList<>();
		}
		return this.medicalTests;
	}
	
	protected void setMedicalTestsInternal(List<MedicalTest> medicalTests) {
		this.medicalTests = medicalTests;
	}
	
	public List<MedicalTest> getMedicalTests() {
		List<MedicalTest> sortedMedicalTests = new ArrayList<>(getMedicalTestsInternal());
		PropertyComparator.sort(sortedMedicalTests, new MutableSortDefinition("name", true, true));
		return Collections.unmodifiableList(sortedMedicalTests);
	}
	
	public void setMedicalTests(List<MedicalTest> medicalTests) {
		setMedicalTestsInternal(medicalTests);
	}
}
