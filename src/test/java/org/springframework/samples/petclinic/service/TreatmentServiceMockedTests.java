package org.springframework.samples.petclinic.service;

import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Treatment;
import org.springframework.samples.petclinic.repository.TreatmentRepository;
import org.springframework.stereotype.Service;

@ExtendWith(MockitoExtension.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class TreatmentServiceMockedTests {

	private static final int TEST_PET_ID = 1;
	private static final int TEST_TREATMENT_ID_1 = 1;
	private static final int TEST_TREATMENT_ID_2 = 2;

	@Mock
	private TreatmentRepository treatmentRepository;

	protected TreatmentService treatmentService;

	private Treatment treatment1, treatment2;
	private List<Treatment> treatments;

	@BeforeEach
	void setup() {
		treatmentService = new TreatmentService(treatmentRepository);

		Pet pet = new Pet();
		pet.setId(TEST_PET_ID);

		Medicine medicine1 = new Medicine();
		medicine1.setName("Ibuprofeno");
		Medicine medicine2 = new Medicine();
		medicine2.setName("Dalsy Pet");
		Set<Medicine> medicines = new HashSet<>();
		medicines.add(medicine1);
		medicines.add(medicine2);

		treatment1 = new Treatment();
		treatment1.setId(TEST_TREATMENT_ID_1);
		treatment1.setName("Tratamiento post-operatorio");
		treatment1.setDescription("Ingerir una pastilla de ibuprefono cada 6 horas");
		treatment1.setTimeLimit(LocalDate.now().plusWeeks(2));
		treatment1.setMedicines(medicines);
		treatment1.setPet(pet);

		treatment2 = new Treatment();
		treatment2.setId(TEST_TREATMENT_ID_2);
		treatment2.setName("Tratamiento para curación de fractura");
		treatment2.setDescription("Ingerir una pastilla de ibuprefono cada 6 horas");
		treatment2.setTimeLimit(LocalDate.now().minusMonths(2));
		treatment2.setMedicines(medicines);
		treatment2.setPet(pet);

		treatments = new ArrayList<>();
		treatments.add(treatment1);
		treatments.add(treatment2);
	}

	@Test
    void shouldSaveTreatment() {
	    this.treatmentRepository.save(treatment1);
	    verify(treatmentRepository).save(treatment1);
    }
}