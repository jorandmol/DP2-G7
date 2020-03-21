
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.AppointmentRepository;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppointmentService {

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Autowired
	private VetRepository vetRepository;

	@Autowired
	private OwnerRepository ownerRepository;

	@Transactional
	public void saveAppointment(final Appointment appointment, Integer ownerId, Integer vetId) throws DataAccessException {
		Owner owner = this.ownerRepository.findById(ownerId);
		Vet vet = this.vetRepository.findById(vetId).get();
		LocalDate requestDate = LocalDate.now();
		appointment.setOwner(owner);
		appointment.setVet(vet);
		appointment.setAppointmentRequestDate(requestDate);
		this.appointmentRepository.save(appointment);
	}
}
