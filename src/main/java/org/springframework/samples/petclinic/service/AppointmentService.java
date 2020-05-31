
package org.springframework.samples.petclinic.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
	@CacheEvict(cacheNames = {"appointmentsByVetAndDate", "appointmentByPetAndDate", "nextAppointmentsByVetAndDate"}, allEntries = true)
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
	@CacheEvict(cacheNames = {"appointmentsByVetAndDate", "appointmentByPetAndDate", "nextAppointmentsByVetAndDate"}, allEntries = true)
	public void deleteAppointment(final Appointment appointment) {
		this.appointmentRepository.delete(appointment);
	}

    @Transactional
	@CacheEvict(cacheNames = {"appointmentsByVetAndDate", "appointmentByPetAndDate", "nextAppointmentsByVetAndDate"}, allEntries = true)
    public void editAppointment(final Appointment appointment) throws VeterinarianNotAvailableException {
	    int vetId = appointment.getVet().getId();
	    Appointment appointmentToUpdate = this.appointmentRepository.findById(appointment.getId());
	    LocalDate newDate = appointment.getAppointmentDate();
	    if (!isPossibleAppointment(appointment, vetId) && !appointmentToUpdate.getAppointmentDate()
            .equals(appointment.getAppointmentDate())) {
	        throw new VeterinarianNotAvailableException();
        } else {
            appointmentToUpdate.setAppointmentDate(appointment.getAppointmentDate());
	        LocalDate date = LocalDate.now();
	        appointment.setAppointmentDate(newDate);
	        appointment.setAppointmentRequestDate(date);
	        this.appointmentRepository.save(appointment);
        }
    }
    
    @Transactional(readOnly=true)
    public Appointment getAppointmentById(int appointmentId) {
        return this.appointmentRepository.findById(appointmentId);
    }

    @Transactional(readOnly=true)
    public Collection<Appointment> getAllAppointments() {
    	return this.appointmentRepository.findAll();
    	
    }

    private boolean isPossibleAppointment(Appointment appointment, Integer vetId) {
	    boolean res = false;
    	LocalDate appointmentDate = appointment.getAppointmentDate();
	    
    	if (appointmentDate != null && !appointmentDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
	    	int petId = appointment.getPet().getId();
	    	res = appointmentRepository.countAppointmentsByPetAndDay(petId, appointmentDate) == 0 && 
	    			appointmentRepository.countAppointmentsByVetAndDay(vetId, appointmentDate) < 6;	    	
	    }
	    
	    return res;
    }

    @Transactional(readOnly=true)
    @Cacheable("appointmentsByVetAndDate")
	public List<Appointment> getAppointmentsByVetAndDate(Integer vetId, LocalDate date) {
		return this.appointmentRepository.getAppointmentsByVetAndDate(vetId, date);
	}

    @Transactional(readOnly=true)
    @Cacheable("nextAppointmentsByVetAndDate")
	public List<Appointment> getNextAppointmentsByVetId(Integer vetId, LocalDate date) {
		return this.appointmentRepository.getNextAppointmentsByVetId(vetId, date);
	}
	
    @Transactional(readOnly=true)
	public Appointment findAppointmentByPetAndDate(Integer id, LocalDate date) {
		return this.appointmentRepository.findByDate(id, date);
	}
}
