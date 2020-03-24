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

import java.time.LocalDate;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.samples.petclinic.util.EntityUtils;
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
class MedicineServiceTests {        
        @Autowired
	protected MedicineService medicineService;	

	@Test
	void shouldFindMedicineWithCorrectId() {
		Medicine med2 = this.medicineService.findMedicineById(2);
		assertThat(med2.getName().contentEquals("Dalsy"));
		assertThat(med2.getExpirationDate().getYear() == 2024);
	}

	@Test
	@Transactional
	public void shouldInsertMedicineIntoDatabaseAndGenerateId() {
		Collection<Medicine> medicines = (Collection<Medicine>) this.medicineService.findAll();
		int found = medicines.size();
		
		Medicine med = new Medicine();
		med.setName("Virbaninte");
		med.setExpirationDate(LocalDate.now().plusYears(2));
		med.setDescription("Desparasitante");
		med.setCode("VET-123");
            try {
                this.medicineService.saveMedicine(med);
            } catch (DataAccessException ex) {
                Logger.getLogger(MedicineServiceTests.class.getName()).log(Level.SEVERE, null, ex);
            }
        Collection<Medicine> medicines2 = (Collection<Medicine>) this.medicineService.findAll();
		assertThat(medicines2.size()).isEqualTo(found + 1);
		// checks that id has been generated
		assertThat(med.getId()).isNotNull();
	}
	
	@Test
	void shouldFindAllMedicines() {
		Collection<Medicine> medicines = (Collection<Medicine>) this.medicineService.findAll();
		int found = medicines.size();
		assertThat(found == 3);
	}
	
	@Test
	void shouldFindUsedCode() {
		Medicine medicine = this.medicineService.findMedicineById(1);
		String code = medicine.getCode();
		assertThat(medicineService.codeAlreadyExists(code));
	}
	

}
