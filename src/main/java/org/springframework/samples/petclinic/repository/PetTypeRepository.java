package org.springframework.samples.petclinic.repository;



import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.PetType;

public interface PetTypeRepository extends CrudRepository<PetType, Integer>{

	@Query("select count(pt) from PetType pt where pt.name=?1")
	int countTypeName(String typeName);

	@Query("SELECT p FROM PetType p WHERE p.name=:petType")
	PetType findByName(@Param("petType") String petType);
}
