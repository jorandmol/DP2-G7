package org.springframework.samples.petclinic.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.PetType;

public interface PetTypeRepository extends CrudRepository<PetType, Integer>{

	@Query("select pt from PetType pt where pt.name=?1")
	Optional<PetType> countTypeName(String typeName);

}
