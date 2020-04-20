package org.springframework.samples.petclinic.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.MedicalTest;
import org.springframework.samples.petclinic.service.MedicalTestService;

@ExtendWith(MockitoExtension.class)
public class MedicalTestsFormatterTests {
	
	@Mock
	private MedicalTestService medicalTestService;
	
	private MedicalTestsFormatter medicalTestFormatter;
	
	@Mock
	private MedicalTest radiography;
	
	@Mock
	private Collection<MedicalTest> medicalTests;
	
	@BeforeEach
	void setUp() {
		this.medicalTestFormatter = new MedicalTestsFormatter(medicalTestService);
		
		medicalTests =new ArrayList<>();
		
		radiography = new MedicalTest();
		radiography.setName("Radiography");
		
		MedicalTest sonography = new MedicalTest();
		sonography.setName("Sonography");
		
		medicalTests.add(sonography);medicalTests.add(radiography);
				
	}

	@Test
	void testPrint() {
		String medicalTestName = medicalTestFormatter.print(radiography, Locale.ENGLISH);
		assertEquals("Radiography", medicalTestName);
	}
	
	@Test
	void testParse() throws ParseException {
		given(medicalTestService.findMedicalTests()).willReturn(medicalTests);
		MedicalTest medicalTest = this.medicalTestFormatter.parse("Sonography", Locale.ENGLISH);
		assertEquals("Sonography", medicalTest.getName());
	}
	
	@Test
	void testParseException() throws ParseException {
		given(medicalTestService.findMedicalTests()).willReturn(medicalTests);
		Assertions.assertThrows(ParseException.class, () -> {
			this.medicalTestFormatter.parse("Blood analysis", Locale.ENGLISH);
		});
	}
}
