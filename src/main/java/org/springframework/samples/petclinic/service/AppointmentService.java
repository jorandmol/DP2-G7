
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.AppointmentRepository;
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
		if (!isPossibleAppointment(appointment, vetId)) {
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
	    Appointment appointmentToUpdate = this.appointmentRepository.findById(appointment.getId()).get();
	    LocalDate newDate = appointment.getAppointmentDate();
	    if (!isPossibleAppointment(appointment, vetId) && !appointmentToUpdate.getAppointmentDate()
            .equals(appointment.getAppointmentDate())) {
	        throw  new VeterinarianNotAvailableException();
        } else {
            appointmentToUpdate.setAppointmentDate(appointment.getAppointmentDate());
	        LocalDate date = LocalDate.now();
	        appointment.setAppointmentDate(newDate);
	        appointment.setAppointmentRequestDate(date);
	        this.appointmentRepository.save(appointment);
        }
    }

    @Transactional
    public Collection<Appointment> getAllAppointments() {
    	Collection<Appointment> appointments = new ArrayList<Appointment>();
    	this.appointmentRepository.findAll().forEach(appointments::add);
    	return appointments;
    }

    private boolean isPossibleAppointment(Appointment appointment, Integer vetId) {
	    LocalDate appointmentDate = appointment.getAppointmentDate();
	    int petId = appointment.getPet().getId();
        return countAppointmentsByPetAndDay(petId, appointmentDate) == 0 &&
            countAppointmentsByVetAndDay(vetId, appointmentDate) < 6;
    }

    private int countAppointmentsByPetAndDay(int petId, LocalDate date) {
        return this.appointmentRepository.countAppointmentsByPetAndDay(petId, date);
    }
}
