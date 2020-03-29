package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class PetTypeServiceTests {
	
	 @Autowired
     protected PetTypeService petTypeService;
	 
	 
	@Test
	void shouldFindPetTypeWithCorrectId() {
			Optional<PetType> petType1 = this.petTypeService.findById(1);
			assertThat(petType1.get().getName()).isEqualTo("cat");
			

		}
	
	@Test
	@Transactional
	public void shouldInsertPetTypeIntoDatabaseAndGenerateId() {
		Iterable<PetType> petTypes = this.petTypeService.findAll();
		int found = ((Collection<PetType>) petTypes).size();

		PetType petType = new PetType();
		petType.setName("Platipus");
		this.petTypeService.savePetType(petType);
		Iterable<PetType> petTypes2 = this.petTypeService.findAll();
		
		assertThat(((Collection<PetType>) petTypes2).size()).isEqualTo(found + 1);

          
		
		assertThat(petType.getId()).isNotNull();
	}
	
	@Test
	@Transactional
	public void PetTypeAlreadyExists() {
		Optional<PetType> petType1 = this.petTypeService.findById(1);
		assertThat(petType1.get().getName()).isEqualTo("cat");

		Boolean exists = this.petTypeService.typeNameDontExists("cat");
	  
		assertThat(exists.equals(false));
		
		
	}
	
	@Test
	@Transactional
	public void PetTypeDontExists() {
		Iterable<PetType> petTypes = this.petTypeService.findAll();
		List<PetType> lsPetType = ((Collection<PetType>) petTypes).stream().collect(Collectors.toList());
		assertThat(!lsPetType.contains("Shark"));

		Boolean exists = this.petTypeService.typeNameDontExists("Shark");
	  
		assertThat(exists.equals(true));
		
		
	}
	

}
