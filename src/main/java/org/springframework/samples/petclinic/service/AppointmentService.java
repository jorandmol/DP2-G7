
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.AppointmentRepository;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.service.exceptions.VeterinarianNotAvailableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppointmentService {

	private AppointmentRepository 	appointmentRepository;

	private VetRepository 			vetRepository;

	@Autowired
	public AppointmentService(AppointmentRepository appointmentRepository, VetRepository vetRepository) {
		this.appointmentRepository = appointmentRepository;
		this.vetRepository = vetRepository;
	}

	@Transactional
	public void saveAppointment(final Appointment appointment, Integer vetId) throws VeterinarianNotAvailableException {
		if (!isPossibleAppointment(appointment, vetId)) {
		    throw new VeterinarianNotAvailableException();
        } else {
            Vet vet = this.vetRepository.findById(vetId);
            LocalDate requestDate = LocalDate.now();
            appointment.setVet(vet);
            appointment.setAppointmentRequestDate(requestDate);
            this.appointmentRepository.save(appointment);
        }
	}

	@Transactional
	public void deleteAppointment(final Appointment appointment) {
		this.appointmentRepository.delete(appointment);
	}

    @Transactional
    public void editAppointment(final Appointment appointment) throws VeterinarianNotAvailableException {
	    int vetId = appointment.getVet().getId();
	    Appointment appointmentToUpdate = this.appointmentRepository.findById(appointment.getId());
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
    
    public Appointment getAppointmentById(int appointmentId) {
        return this.appointmentRepository.findById(appointmentId);
    }

    public Collection<Appointment> getAllAppointments() {
    	return this.appointmentRepository.findAll();
    	
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
    
    private int countAppointmentsByVetAndDay(int vetId, LocalDate date) {
	    return this.appointmentRepository.countAppointmentsByVetAndDay(vetId, date);
    }
}
