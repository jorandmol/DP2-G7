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

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Status;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.StayRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.samples.petclinic.service.exceptions.DateNotAllowed;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.samples.petclinic.service.exceptions.MaximumStaysReached;
import org.springframework.samples.petclinic.service.exceptions.StayAlreadyConfirmed;
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
	public PetService(PetRepository petRepository, VisitRepository visitRepository, StayRepository stayRepository) {
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
		Pet otherPet = pet.getOwner().getPetwithIdDifferent(pet.getName(), pet.getId());
		if (StringUtils.hasLength(pet.getName()) && (otherPet != null && otherPet.getId() != pet.getId())) {
			throw new DuplicatedPetNameException();
		} else
			petRepository.save(pet);
	}

	public Collection<Visit> findVisitsByPetId(int petId) {
		return visitRepository.findByPetId(petId);
	}

	@Transactional
	public void saveStay(Stay stay) throws MaximumStaysReached {
		Boolean dayExists = this.stayRepository.numOfStaysThatDates(stay.getRegisterDate(), stay.getReleaseDate(),
				stay.getPet().getId(), 0) > 0;
		if (dayExists) {
			throw new MaximumStaysReached();
		} else {
			stay.setStatus(Status.PENDING);
			stayRepository.save(stay);
		}

	}

	@Transactional(readOnly = true)
	public Stay findStayById(int stayId) {
		return stayRepository.findById(stayId).orElse(null);
	}

	@Transactional
	public void deleteStay(Stay stay) throws StayAlreadyConfirmed {
		if (stay.getStatus() != Status.PENDING) {
			throw new StayAlreadyConfirmed();
		} else {
			Pet pet = stay.getPet();
			pet.deleteStay(stay);
			stayRepository.delete(stay);
		}
	}

	public Collection<Stay> findStaysByPetId(int petId) {
		return stayRepository.findByPetId(petId);
	}

	@Transactional
	public void editStay(final Stay stay) throws MaximumStaysReached, DateNotAllowed, StayAlreadyConfirmed {

		Stay stayToUpdate = this.stayRepository.findById(stay.getId()).get();
		LocalDate newDate = stay.getRegisterDate();
		LocalDate newDate2 = stay.getReleaseDate();
		if ((stayToUpdate.getRegisterDate().equals(stay.getRegisterDate())
				&& stayToUpdate.getReleaseDate().equals(stay.getReleaseDate()))) {
			throw new DateNotAllowed();
		} else if (this.stayRepository.numOfStaysThatDates(stay.getRegisterDate(), stay.getReleaseDate(),
				stay.getPet().getId(), stay.getId()) > 0) {
			throw new MaximumStaysReached();
		} else if (!stayToUpdate.getStatus().equals(Status.PENDING)) {
			throw new StayAlreadyConfirmed();
		}

		else {
			stayToUpdate.setRegisterDate(stay.getRegisterDate());
			stayToUpdate.setReleaseDate(stay.getReleaseDate());
			stay.setRegisterDate(newDate);
			stay.setReleaseDate(newDate2);
			this.stayRepository.save(stay);
		}
	}

	public List<Pet> findAll() {
		return this.petRepository.findAll();
	}

	public List<Stay> findAllStays() {
		return (List<Stay>) this.stayRepository.findAll();
	}
	
	@Transactional
	public void editStatus(final Stay stay) throws StayAlreadyConfirmed {
		Stay stayToUpdate = this.findStayById(stay.getId());
		if (!stayToUpdate.getStatus().equals(Status.PENDING)) {
			throw new StayAlreadyConfirmed();
		} else {
//			stayToUpdate.setStatus(stay.getStatus());
//			stay.setStatus(stay.getStatus());
			this.stayRepository.save(stay);
		}
	}
}
