
package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.repository.AppointmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppointmentService {

	private AppointmentRepository appointmentRepository;
	
	@Autowired
	public AppointmentService(AppointmentRepository appointmentRepository) {	
		this.appointmentRepository = appointmentRepository;
	}


	@Transactional
	public void saveAppointment(final Appointment appointment) throws DataAccessException {
		this.appointmentRepository.save(appointment);
	}
	
	public void deleteAppointment(final Appointment appointment) throws DataAccessException {
		this.appointmentRepository.delete(appointment);
	}
	
	public Appointment findAppointmentById(int appointmentId) throws DataAccessException {
		return this.appointmentRepository.findById(appointmentId);
	}
}
