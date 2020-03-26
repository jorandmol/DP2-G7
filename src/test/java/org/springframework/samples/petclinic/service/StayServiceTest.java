package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class StayServiceTest {
	
	 @Autowired
	protected StayService stayService;
	 
     @Autowired
	protected PetService petService;
     
     @Autowired
     
     protected OwnerService ownerService;
     
     @Test
 	void shouldFindStayInPetWithCorrectId() {
 		
 		Collection<Stay> cStay7 = this.stayService.findStancesByPetId(7);
 		Stay stay7 = cStay7.stream().collect(Collectors.toList()).get(0);
 		assertThat(stay7.getPet().getName()).startsWith("Samantha");
 		assertThat(stay7.getPet().getOwner().getFirstName()).isEqualTo("Jean");
 		assertThat(stay7.getRegisterDate().equals(LocalDate.of(2020, 10, 1)));
 		assertThat(stay7.getReleaseDate().equals(LocalDate.of(2020, 10, 5)));

 	}
     
     @Test
 	@Transactional
 	public void shouldInsertStayIntoDatabaseAndGenerateId() {
 	
 		
 		Pet pet7 = this.petService.findPetById(7);
 		int found = pet7.getStances().size();

 		Stay stay = new Stay();
 		stay.setRegisterDate(LocalDate.of(2020, 10, 20));
 		stay.setReleaseDate(LocalDate.of(2020, 10, 22));
 	
 		pet7.addStay(stay);
 		assertThat(pet7.getStances().size()).isEqualTo(found + 1);

             
        this.stayService.saveStay(stay);
       
        try {
			this.petService.savePet(pet7);
		} catch (DataAccessException | DuplicatedPetNameException e) {
		
			e.printStackTrace();
		}
           

 		pet7 = this.petService.findPetById(7);
 		assertThat(pet7.getStances().size()).isEqualTo(found + 1);
 		assertThat(stay.getId()).isNotNull();
 	}

}
