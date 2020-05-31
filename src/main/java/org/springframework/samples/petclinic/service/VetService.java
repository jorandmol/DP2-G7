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

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class VetService {

	private VetRepository vetRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthoritiesService authoritiesService;

	@Autowired
	public VetService(VetRepository vetRepository) {
		this.vetRepository = vetRepository;
	}		

	@Transactional(readOnly = true)	
	public Collection<Vet> findVets() throws DataAccessException {
		return vetRepository.findAll();
	}
	
	@Transactional(readOnly = true)	
	@Cacheable("specialties")
	public Collection<Specialty> findSpecialties() {
		return vetRepository.findSpecialties();
	}

	@Transactional
	@CacheEvict(cacheNames = "vetById", allEntries = true)
	public void saveVet(Vet vet) throws DataAccessException {
		//creating vet
		vetRepository.save(vet);		
		//creating user
		userService.saveUser(vet.getUser());
		//creating authorities
		authoritiesService.saveAuthorities(vet.getUser().getUsername(), "veterinarian");
	}

	@Transactional(readOnly = true)
	@Cacheable("vetById")
	public Vet findVetById(Integer vetId) {
		return vetRepository.findById(vetId);
	}
	
	@Transactional(readOnly = true)
	public Vet findVetByUsername(String username) {
		return vetRepository.findByUsername(username);
	}

}
