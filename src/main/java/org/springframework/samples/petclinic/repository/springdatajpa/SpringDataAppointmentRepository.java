package org.springframework.samples.petclinic.repository.springdatajpa;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.repository.AppointmentRepository;

public interface SpringDataAppointmentRepository extends AppointmentRepository, Repository<Appointment, Integer> {
	
	@Override
	@Query("SELECT COUNT(a) FROM Appointment a WHERE a.vet.id=:vetId AND a.appointmentDate=:date")
    int countAppointmentsByVetAndDay(@Param("vetId") int vetId, @Param("date") LocalDate date);

	@Override
	@Query("SELECT COUNT(a) FROM Appointment a WHERE a.pet.id=:petId AND a.appointmentDate=:date")
    int countAppointmentsByPetAndDay(@Param("petId") int petId, @Param("date") LocalDate date);

	@Query("SELECT a FROM Appointment a WHERE a.vet.id=:vetId AND a.appointmentDate=:date")
	List<Appointment> getAppointmentsByVetAndDate(@Param("vetId") Integer vetId, @Param("date") LocalDate date);
	
	@Query("SELECT a FROM Appointment a WHERE a.vet.id=:vetId AND a.appointmentDate>:date")
	List<Appointment> getNextAppointmentsByVetId(@Param("vetId") Integer vetId, @Param("date") LocalDate date);

	@Query("SELECT a FROM Appointment a WHERE a.pet.id=:petId AND a.appointmentDate=:date")
	Appointment findByDate(@Param("petId") Integer petId, @Param("date") LocalDate date);

}
