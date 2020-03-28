
package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface AppointmentRepository extends CrudRepository<Appointment, Integer> {

	@Query("SELECT COUNT(a) FROM Appointment a WHERE a.vet.id=:vetId AND a.appointmentDate=:date")
    int countAppointmentsByVetAndDay(@Param("vetId") int vetId, @Param("date")LocalDate date);

}
