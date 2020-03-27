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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.service.VetService;

@ExtendWith(MockitoExtension.class)
public class VetSpecialtiesFormatterTests {
	
	//Necesario poner Mock para que sea un dobleeeee!!!
	@Mock
	private VetService vetService;
	
	private VetSpecialtiesFormatter vetSpecialtiesFormatter;
		
	@BeforeEach
	void setUp() {
		
		this.vetSpecialtiesFormatter= new VetSpecialtiesFormatter(vetService);
		
	}

	@Test
	void testPrint() {
		Specialty specialty = new Specialty();
		specialty.setName("cardiology");
		String specialtyName = vetSpecialtiesFormatter.print(specialty, Locale.ENGLISH);
		assertEquals("cardiology", specialtyName);
	}
	
	
	@Test
	void testParse() throws ParseException {
		Mockito.when(vetService.findSpecialties()).thenReturn(specialties());
		Specialty s= vetSpecialtiesFormatter.parse("cardiology", Locale.ENGLISH);
		assertEquals("cardiology", s.getName());
	}

	@Test
	void testParseException() throws ParseException {
		Mockito.when(vetService.findSpecialties()).thenReturn(specialties());
		Assertions.assertThrows(ParseException.class, () -> {
			vetSpecialtiesFormatter.parse("surgery", Locale.ENGLISH);
		});
	}
	
	private Collection<Specialty> specialties(){
		Collection<Specialty> specialties= new ArrayList<>();
		
		Specialty radiology = new Specialty();
		radiology.setId(1);
		radiology.setName("radiology");
		
		Specialty cardiology = new Specialty();
		cardiology.setId(2);
		cardiology.setName("cardiology");
		
		specialties.add(radiology);
		specialties.add(cardiology);
		
		return specialties;
	}

}
