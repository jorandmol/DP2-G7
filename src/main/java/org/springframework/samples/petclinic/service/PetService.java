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


import java.time.ZoneId;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.StayRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class PetService {

	private PetRepository petRepository;
	
	private VisitRepository visitRepository;
	
	private StayRepository stayRepository;

	@Autowired
	public PetService(PetRepository petRepository,
			VisitRepository visitRepository, StayRepository stayRepository) {
		this.petRepository = petRepository;
		this.visitRepository = visitRepository;
		this.stayRepository = stayRepository;
	}

	@Transactional(readOnly = true)
	public Collection<PetType> findPetTypes() throws DataAccessException {
		return petRepository.findPetTypes();
	}
	
	@Transactional
	public void saveVisit(Visit visit) throws DataAccessException {
		visitRepository.save(visit);
	}

	@Transactional(readOnly = true)
	public Pet findPetById(int id) throws DataAccessException {
		return petRepository.findById(id);
	}

	@Transactional(rollbackFor = DuplicatedPetNameException.class)
	public void savePet(Pet pet) throws DataAccessException, DuplicatedPetNameException {
			Pet otherPet=pet.getOwner().getPetwithIdDifferent(pet.getName(), pet.getId());
            if (StringUtils.hasLength(pet.getName()) &&  (otherPet!= null && otherPet.getId()!=pet.getId())) {            	
            	throw new DuplicatedPetNameException();
            }else
                petRepository.save(pet);                
	}


	public Collection<Visit> findVisitsByPetId(int petId) {
		return visitRepository.findByPetId(petId);
	}

	@Transactional
	public void saveStay(Stay stay) {
		stayRepository.save(stay);
		
	}

	@Transactional(readOnly = true)
	public Stay findStayById(int stayId) {
		return stayRepository.findById(stayId).orElse(null);
	}

	@Transactional
	public void deleteStay(Stay stay) {
		Pet pet = stay.getPet();
		pet.deleteStay(stay);
		stayRepository.delete(stay);
	}
	
	public Collection<Stay> findStaysByPetId(int petId) {
		return stayRepository.findByPetId(petId);
	}

//	public Boolean existsStayInThatDates(Stay s) {
//		Date rgDate = Date.from(s.getRegisterDate().atStartOfDay()
//			      .atZone(ZoneId.systemDefault())
//			      .toInstant());
//		Date rlDate = Date.from(s.getReleaseDate().atStartOfDay()
//			      .atZone(ZoneId.systemDefault())
//			      .toInstant());
//		return this.stayRepository.numOfStaysThatDates(rgDate, rlDate) > 0;
//	}
}
