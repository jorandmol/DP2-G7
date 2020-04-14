package org.springframework.samples.petclinic.repository.springdatajpa;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.repository.AppointmentRepository;

public interface SpringDateAppointmentRepository extends AppointmentRepository, Repository<Appointment, Integer> {
	
	@Override
	@Query("SELECT COUNT(a) FROM Appointment a WHERE a.vet.id=:vetId AND a.appointmentDate=:date")
    int countAppointmentsByVetAndDay(@Param("vetId") int vetId, @Param("date") LocalDate date);

	@Override
	@Query("SELECT COUNT(a) FROM Appointment a WHERE a.pet.id=:petId AND a.appointmentDate=:date")
    int countAppointmentsByPetAndDay(@Param("petId") int petId, @Param("date") LocalDate date);

}
