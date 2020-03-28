package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Treatment;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class TreatmentServiceTests {

	@Autowired
	protected TreatmentService treatmentService;
	
	@Test
	void shouldFindTreatmentsById() {
		List<Treatment> treatments = this.treatmentService.findTreatmentsByPet(1);
		
		Treatment treatment = treatments.get(0);
		assertThat(treatment.getId()).isEqualTo(1);
		assertThat(treatment.getPet().getId()).isEqualTo(1);
	}
}
