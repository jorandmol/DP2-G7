package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.MedicalTest;
import org.springframework.samples.petclinic.repository.MedicalTestRepository;
import org.springframework.stereotype.Service;

@ExtendWith(MockitoExtension.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MedicalTestServiceTests {
	
	private static final int TEST_MT_ID1 = 1;

	private static final int TEST_MT_ID2 = 2;

	private MedicalTestService medicalTestService;

	@Mock
	private MedicalTestRepository medicalTestRepository;

	private MedicalTest radiography;

	private MedicalTest sonography;

	private Collection<MedicalTest> medicalTests;

	@BeforeEach
	void setup() {
		medicalTestService = new MedicalTestService(medicalTestRepository);

		medicalTests= new ArrayList<>();

		radiography= new MedicalTest();
		radiography.setDescription("Images of the internal structure of the body to assess the presence of  foreign objects, and structural damage or anomaly");
		radiography.setId(TEST_MT_ID1);
		radiography.setName("Radiography");
		medicalTests.add(radiography);

		sonography= new MedicalTest();
		sonography.setDescription("Images of body structures based on the pattern of echoes reflected");
		sonography.setId(TEST_MT_ID2);
		sonography.setName("Sonography");
		medicalTests.add(sonography);

	}

	@Test
	void shouldfindMedicalTests() {
    	Mockito.when(medicalTestService.findMedicalTests()).thenReturn(medicalTests);
		Collection<MedicalTest> medicalTests= this.medicalTestService.findMedicalTests();
		assertThat(medicalTests.size()).isEqualTo(2);
	}

	@Test
	void shouldSaveMedicalTest() {
		this.medicalTestService.saveMedicalTest(radiography);
		verify(medicalTestRepository).save(radiography);
	}
	
	@Test
	void shouldFindMedicalTestById() {
		Mockito.when(medicalTestService.findMedicalTestById(TEST_MT_ID1)).thenReturn(radiography);
		MedicalTest mt = this.medicalTestService.findMedicalTestById(TEST_MT_ID1);
		assertThat(mt).isEqualTo(radiography);
	}
}
