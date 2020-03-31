package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.exceptions.VeterinarianNotAvailableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class AppointmentServiceTests {

	@Autowired
	protected AppointmentService appointmentService;
	
	@Autowired
	private OwnerService ownerService;
	
	@Test
	@Transactional
	void shouldDeleteAppointment() {
		Appointment appointment = this.appointmentService.getAppointmentById(1);

		this.appointmentService.deleteAppointment(appointment);

		assertThat(this.appointmentService.getAppointmentById(1)).isEqualTo(null);
	}
	
	@Test
	@Transactional
	void shouldSaveAppointment() {
		Owner owner = this.ownerService.findOwnerById(1);
		Pet pet= owner.getPets().get(0);
		int vetId=1;
		int appointmentsFoundBefore = this.appointmentService.getAllAppointments().size();
		Appointment appointment = new Appointment();
		appointment.setOwner(owner);
		appointment.setPet(pet);
		appointment.setDescription("A simple description...");
		appointment.setAppointmentDate(LocalDate.now().plusDays(10));
		try {
			this.appointmentService.saveAppointment(appointment, vetId);
		}catch(VeterinarianNotAvailableException e){
			Logger.getLogger(AppointmentServiceTests.class.getName()).log(Level.SEVERE, null, e);
		}
		int appointmentsFoundAfter = this.appointmentService.getAllAppointments().size();
		assertThat(appointmentsFoundAfter).isEqualTo(appointmentsFoundBefore + 1);
	}

	@Test
	void shouldCountAppointmentsByVetAndDay() {
		int vetId = 1;
		LocalDate dateWithAppointments= this.appointmentService.getAppointmentById(1).getAppointmentDate();
		LocalDate dateWithoutAppointments= LocalDate.of(2000, 1, 1);
		int count3 = this.appointmentService.countAppointmentsByVetAndDay(vetId, dateWithAppointments);
		int count0 = this.appointmentService.countAppointmentsByVetAndDay(vetId, dateWithoutAppointments);
		assertThat(count3).isEqualTo(3);
		assertThat(count0).isEqualTo(0);
	}
	
	@Test
	@Transactional
	void shouldEditAppointment() {
		Appointment appointmentToUpdate= this.appointmentService.getAppointmentById(1);
		System.out.println(appointmentToUpdate);
		LocalDate newAppointmentDate= appointmentToUpdate.getAppointmentDate().plusDays(5);
		appointmentToUpdate.setAppointmentDate(newAppointmentDate);

		try {
			this.appointmentService.editAppointment(appointmentToUpdate);
		}catch(VeterinarianNotAvailableException e){
			Logger.getLogger(AppointmentServiceTests.class.getName()).log(Level.SEVERE, null, e);
		}
		
		assertThat(this.appointmentService.getAppointmentById(1).getAppointmentDate()).isEqualTo(newAppointmentDate);
	}
	
	
}
