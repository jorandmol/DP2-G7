package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VisitService {

	private VisitRepository visitRepository;
	
	@Autowired
	public VisitService(VisitRepository visitRepository) {
		this.visitRepository = visitRepository;
	}

	@Transactional(readOnly = true)
	public Visit findVisitById(int visitId) {
		return visitRepository.findById(visitId);
	}

	@Transactional(readOnly = true)
	public Integer countVisitsByDate(Integer petId, LocalDate date) {
		return visitRepository.countByDate(petId, date);
	}
	
	@Transactional
	public void saveVisit(Visit visit) throws DataAccessException {
		visitRepository.save(visit);
	}

	public List<Visit> findVisitsByPetId(int petId) {
		return visitRepository.findByPetId(petId);
	}
}
