
package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;

@Repository
public interface AppointmentRepository {

	Appointment findById(int id);
	
    int countAppointmentsByVetAndDay(@Param("vetId") int vetId, @Param("date") LocalDate date);

    int countAppointmentsByPetAndDay(@Param("petId") int petId, @Param("date") LocalDate date);
    
    void save(Appointment appointment);
    
    void delete(Appointment appointment);
    
    Collection<Appointment> findAll();

}
