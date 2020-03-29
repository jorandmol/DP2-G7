
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.AppointmentRepository;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.service.exceptions.VeterinarianNotAvailableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppointmentService {

	private AppointmentRepository appointmentRepository;

	@Autowired
	public AppointmentService(AppointmentRepository appointmentRepository) {
		this.appointmentRepository = appointmentRepository;
	}

	@Autowired
	private VetRepository vetRepository;

	@Transactional
	public void saveAppointment(final Appointment appointment, Integer vetId) throws VeterinarianNotAvailableException {
		if (countAppointmentsByVetAndDay(vetId, appointment.getAppointmentDate()) > 3) {
		    throw new VeterinarianNotAvailableException();
        } else {
            Vet vet = this.vetRepository.findById(vetId).get();
            LocalDate requestDate = LocalDate.now();
            appointment.setVet(vet);
            appointment.setAppointmentRequestDate(requestDate);
            this.appointmentRepository.save(appointment);
        }
	}

	@Transactional
	public void deleteAppointment(final Appointment appointment) throws DataAccessException {
		this.appointmentRepository.delete(appointment);
	}

	@Transactional
    public int countAppointmentsByVetAndDay(int vetId, LocalDate date) {
	    return this.appointmentRepository.countAppointmentsByVetAndDay(vetId, date);
    }

    @Transactional
    public Appointment getAppointmentById(int appointmentId) {
        Optional<Appointment> appointment = this.appointmentRepository.findById(appointmentId);
        if (appointment.isPresent()) {
            return appointment.get();
        }
        return null;
    }

    @Transactional
    public void editAppointment(final Appointment appointment) throws VeterinarianNotAvailableException {
	    int vetId = appointment.getVet().getId();
	    LocalDate newDate = appointment.getAppointmentDate();
	    if (countAppointmentsByVetAndDay(vetId, newDate) > 3) {
	        throw  new VeterinarianNotAvailableException();
        } else {
	        LocalDate date = LocalDate.now();
	        appointment.setAppointmentDate(newDate);
	        appointment.setAppointmentRequestDate(date);
	        this.appointmentRepository.save(appointment);
        }
    }
}
