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

import java.util.Collection;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Vet;
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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VetServiceTests {

	@Autowired
	protected VetService vetService;

	@Test
	void shouldFindVets() {
		Collection<Vet> vets = this.vetService.findVets();

		Vet vet = EntityUtils.getById(vets, Vet.class, 3);
		assertThat(vet.getLastName()).isEqualTo("Douglas");
		assertThat(vet.getNrOfSpecialties()).isEqualTo(2);
		assertThat(vet.getSpecialties().get(0).getName()).isEqualTo("dentistry");
		assertThat(vet.getSpecialties().get(1).getName()).isEqualTo("surgery");
	}

	@Test
	@Transactional
	public void shouldInsertVetWithoutSpecialties() {
		Collection<Vet> vets = this.vetService.findVets();
		int numberOfVets = vets.size();

		Vet vet = new Vet();
		vet.setFirstName("Elena");
		vet.setLastName("Molino");
		vet.setAddress("30, Avenida Reina Mercedes");
		vet.setCity("Sevilla");
		vet.setTelephone("123456789");
                User user=new User();
                user.setUsername("elenamolino");
                user.setPassword("v3terinarian_1");
                user.setEnabled(true);
                vet.setUser(user);

		this.vetService.saveVet(vet);
		assertThat(vet.getId()).isNotEqualTo(0);

		Collection<Vet> vets2 = this.vetService.findVets();
		assertThat(vets2.size()).isEqualTo(numberOfVets + 1);
	}


	@Test
	@Transactional
	public void shouldInsertVetWithSpecialties() {
		Collection<Vet> vets = this.vetService.findVets();
		int numberOfVets = vets.size();

		Vet vet = new Vet();
		vet.setFirstName("Elena");
		vet.setLastName("Molino");
		vet.setAddress("30, Avenida Reina Mercedes");
		vet.setCity("Sevilla");
		vet.setTelephone("123456789");
		Collection<Specialty> specialties= this.vetService.findSpecialties();
		vet.addSpecialty(EntityUtils.getById(specialties, Specialty.class, 1));
		vet.addSpecialty(EntityUtils.getById(specialties, Specialty.class, 2));
                User user=new User();
                user.setUsername("elenamolino1");
                user.setPassword("v3terinarian_1");
                user.setEnabled(true);
                vet.setUser(user);

		this.vetService.saveVet(vet);
		assertThat(vet.getId()).isNotEqualTo(0);

		Collection<Vet> vets2 = this.vetService.findVets();
		assertThat(vets2.size()).isEqualTo(numberOfVets + 1);
	}

	@Test
	@Transactional
	public void shouldThrowExceptionInsertingVetsWithUsernameDuplicate() {
		Vet vet= new Vet();
		vet.setFirstName("Elena");
		vet.setLastName("Molino");
		vet.setAddress("30, Avenida Reina Mercedes");
		vet.setCity("Almedralejo");
		vet.setTelephone("123456789");
				User user=new User();
				user.setUsername("elenamolino2");
				user.setPassword("v3terinarian_1");
				user.setEnabled(true);
				vet.setUser(user);
		try {
			vetService.saveVet(vet);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Vet vetWithSameUsername = new Vet();
			User user1=new User();
			user1.setUsername("elenamolino2");
			user1.setPassword("v3terinarian_1");
			user1.setEnabled(true);

		vetWithSameUsername.setUser(user1);
		Assertions.assertThrows(DataIntegrityViolationException.class, () ->{
			vetService.saveVet(vetWithSameUsername);
		});

	}

	@Test
	@Transactional
	void findVetByIdTest() {
		Vet vet = this.vetService.findVetById(1);
		String name = vet.getFirstName();
		String newName = name + " "+ "Maria";

	    vet.setFirstName(newName);
		this.vetService.saveVet(vet);

		Vet vet1 = this.vetService.findVetById(1);
		assertThat(vet1.getFirstName()).isEqualTo(newName);
	}


}
