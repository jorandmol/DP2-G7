package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.repository.StayRepository;

public interface SpringDataStayRepository extends StayRepository , org.springframework.data.repository.Repository<Stay, Integer>{

}
