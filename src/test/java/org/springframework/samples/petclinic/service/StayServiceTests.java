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
package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Status;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.service.exceptions.DateNotAllowed;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.samples.petclinic.service.exceptions.MaximumStaysReached;
import org.springframework.samples.petclinic.service.exceptions.StayAlreadyConfirmed;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test of the Service and the Repository layer.
 * <p>
 * ClinicServiceSpringDataJpaTests subclasses benefit from the following services provided
 * by the Spring TestContext Framework:
 * </p>
 * <ul>
 * <li><strong>Spring IoC container caching</strong> which spares us unnecessary set up
 * time between test execution.</li>
 * <li><strong>Dependency Injection</strong> of test fixture instances, meaning that we
 * don't need to perform application context lookups. See the use of
 * {@link Autowired @Autowired} on the <code>{@link
 * ClinicServiceTests#clinicService clinicService}</code> instance variable, which uses
 * autowiring <em>by type</em>.
 * <li><strong>Transaction management</strong>, meaning each test method is executed in
 * its own transaction, which is automatically rolled back by default. Thus, even if tests
 * insert or otherwise change database state, there is no need for a teardown or cleanup
 * script.
 * <li>An {@link org.springframework.context.ApplicationContext ApplicationContext} is
 * also inherited and can be used for explicit bean lookup if necessary.</li>
 * </ul>
 *
 * @author Ken Krebs
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Dave Syer
 */

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StayServiceTests {
	
    @Autowired
	protected PetService petService;
        
    @Autowired
    protected StayService stayService;
        
    @Autowired
	protected OwnerService ownerService;	
        
    @Test
	@Transactional
	public void shouldAddNewStayForPet() {
		Pet pet7 = this.petService.findPetById(7);
		int found = pet7.getStays().size();
		Stay stay = new Stay();
		pet7.addStay(stay);
		stay.setRegisterDate(LocalDate.now());
		stay.setReleaseDate(LocalDate.now().plusDays(3));
		stay.setStatus(Status.PENDING);
            try {
            	this.stayService.saveStay(stay);
                this.petService.savePet(pet7);
            } catch (DuplicatedPetNameException | MaximumStaysReached ex) {
                Logger.getLogger(PetServiceTests.class.getName()).log(Level.SEVERE, null, ex);
            }

		pet7 = this.petService.findPetById(7);
		assertThat(pet7.getStays().size()).isEqualTo(found + 1);
		assertThat(stay.getId()).isNotNull();
	}

	@Test
	void shouldFindStaysByPetId() throws Exception {
		Collection<Stay> stays = this.stayService.findStaysByPetId(1);
		assertThat(stays.size()).isEqualTo(1);
		Stay[] stayArr = stays.toArray(new Stay[stays.size()]);
		assertThat(stayArr[0].getPet()).isNotNull();
		assertThat(stayArr[0].getRegisterDate()).isNotNull();
		assertThat(stayArr[0].getPet().getId()).isEqualTo(1);
	}

	@Test
	void shouldFindStayWithCorrectId() {
		Stay stay = this.stayService.findStayById(1);
		assertThat(stay.getPet().getName()).startsWith("Leo");
		assertThat(stay.getRegisterDate()).isEqualTo(LocalDate.of(2020,10,1));
	}
	
	@Test
	void shouldDeleteStayWithCorrectId() {
		Stay stay = this.stayService.findStayById(2);
		Pet pet = this.petService.findPetById(2);
		int numStays = pet.getStays().size();
		try {
			this.stayService.deleteStay(stay);
		} catch (StayAlreadyConfirmed e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertThat(pet.getStays().size()).isEqualTo(numStays - 1);
	}
	
	@Test
	void shouldNotDeleteStayConfirmed() {
		Stay stay = this.stayService.findStayById(1);
		Pet pet = this.petService.findPetById(1);
		int numStays = pet.getStays().size();
	
		assertThrows(StayAlreadyConfirmed.class, () -> {
			this.stayService.deleteStay(stay);
		});
		assertThat(pet.getStays().size()).isEqualTo(numStays);
	}
	
	@Test
	void shouldEditStay() {
		Stay stay = this.stayService.findStayById(2);
		LocalDate d1 = LocalDate.now().plusYears(1);
		LocalDate d2 = d1.plusDays(3);
		try {
			stay.setRegisterDate(d1);
			stay.setReleaseDate(d2);
			this.stayService.editStay(stay);
		} catch (MaximumStaysReached | DateNotAllowed | StayAlreadyConfirmed e) {
			e.printStackTrace();
		}
		assertThat(this.stayService.findStayById(2).getRegisterDate()).isEqualTo(d1);
		assertThat(this.stayService.findStayById(2).getReleaseDate()).isEqualTo(d2);
	}
	
	@Test
	void shouldNotEditStayMaximumStays() {
		Stay stay = this.stayService.findStayById(2);
		Stay stay2 = new Stay();
		BeanUtils.copyProperties(stay, stay2);
		
		assertThrows(MaximumStaysReached.class, () -> {
			stay2.setRegisterDate(LocalDate.of(2020, 10, 30));
			stay2.setReleaseDate(LocalDate.of(2020, 11, 3));
			this.stayService.editStay(stay2);
		});
	}
	
	@Test
	void shouldNotEditStayStatus() {
		Stay stay = this.stayService.findStayById(1);
		Stay stay2 = new Stay();
		BeanUtils.copyProperties(stay, stay2);
		
		assertThrows(StayAlreadyConfirmed.class, () -> {
			stay2.setRegisterDate(LocalDate.of(2020, 10, 30));
			stay2.setReleaseDate(LocalDate.of(2020, 11, 3));
			this.stayService.editStay(stay2);
		});
	}
	
	@Test
	void shouldNotEditStayRepeatedDate() {
		Stay stay = this.stayService.findStayById(2);
		
		assertThrows(DateNotAllowed.class, () -> {
			this.stayService.editStay(stay);
		});
	}
	
	@Test
	void shouldEditStayChangeStatus() {
		Stay stay = this.stayService.findStayById(2);
		try {
			stay.setStatus(Status.ACCEPTED);
			this.stayService.editStatus(stay);
		} catch (StayAlreadyConfirmed e) {
			e.printStackTrace();
		}
		assertThat(this.stayService.findStayById(2).getStatus()).isEqualTo(Status.ACCEPTED);
	}
	
	@Test
	void shouldNotEditStayChangeStatus() {
		Stay stay = this.stayService.findStayById(1);
			
		assertThrows(StayAlreadyConfirmed.class, () -> {
			stay.setStatus(Status.REJECTED);
			this.stayService.editStatus(stay);
		});
	}
}
