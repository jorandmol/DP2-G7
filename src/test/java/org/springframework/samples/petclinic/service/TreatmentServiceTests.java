package org.springframework.samples.petclinic.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.Treatment;
import org.springframework.samples.petclinic.model.TreatmentHistory;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TreatmentServiceTests {

	@Autowired
	private TreatmentService treatmentService;

	@Autowired
	private PetService petService;

	private Treatment treatment;

	@BeforeEach
	void setup() {
		treatment = new Treatment();
		treatment.setName("Treatment Name");
		treatment.setDescription("Esta es la descripción");
		treatment.setTimeLimit(LocalDate.now().plusWeeks(2));
		treatment.setMedicines(new HashSet<Medicine>());
		treatment.setPet(this.petService.findPetById(1));
		this.treatmentService.saveTreatment(treatment);
	}

	@Test
	public void shouldRegisterTreatmentHistory() {
		List<TreatmentHistory> history = this.treatmentService.findHistoryByTreatment(1);
		int size = history.size();
		treatment.setDescription("Nueva receta para la mascota");
		this.treatmentService.editTreatment(treatment);
		history = this.treatmentService.findHistoryByTreatment(1);
		assertEquals(size + 1, history.size());
	}

}
