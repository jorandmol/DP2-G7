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
package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetRegistrationStatus;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.PetRepository;

/**
 * Spring Data JPA specialization of the {@link PetRepository} interface
 *
 * @author Michael Isvy
 * @since 15.1.2013
 */
public interface SpringDataPetRepository extends PetRepository, Repository<Pet, Integer> {

	@Override
	@Query("SELECT ptype FROM PetType ptype ORDER BY ptype.name")
	List<PetType> findPetTypes() throws DataAccessException;

	@Query("SELECT p FROM Pet p WHERE p.status=:pending")
	List<Pet> findPetsRequests(@Param("pending") PetRegistrationStatus pending);
	
	@Query("SELECT p FROM Pet p WHERE (p.status=:pending OR p.status=:rejected) AND p.owner.id=:ownerId")
	List<Pet> findPetsRequests(@Param("pending") PetRegistrationStatus pending, @Param("rejected") PetRegistrationStatus rejected, @Param("ownerId") Integer ownerId);
	
	@Query("SELECT p FROM Pet p WHERE p.status=:accepted AND p.active=:active AND p.owner.id=:ownerId")
	List<Pet> findMyPetsAcceptedByActive(@Param("accepted") PetRegistrationStatus accepted, @Param("active") boolean active, @Param("ownerId") Integer ownerId);

	@Query("SELECT COUNT(p) FROM Pet p WHERE p.status=:accepted AND p.active=:active AND p.owner.id=:ownerId")
	int countMyPetsAcceptedByActive(@Param("accepted") PetRegistrationStatus accepted, @Param("active") boolean active, @Param("ownerId") Integer ownerId);

}
