package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class AppointmentServiceTests {

	@Autowired
	protected AppointmentService appointmentService;

	@Test
	@Transactional
	void shouldDeleteAppointment() {
		Appointment appointment = this.appointmentService.getAppointmentById(1);

		this.appointmentService.deleteAppointment(appointment);

		assertThat(this.appointmentService.getAppointmentById(1)).isEqualTo(null);
	}

}
