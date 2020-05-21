package org.springframework.samples.petclinic.model;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.service.AppointmentService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.exceptions.VeterinarianNotAvailableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AppointmentValidatorTest {
	
	@Autowired
	private AppointmentService appointmentService;
	
	@Autowired
	private PetService petService;

	@ParameterizedTest
	@CsvSource({
		"2020/08/12,''",
		"2019/08/12,Revision pet",
		",Anual Revision pet"
	})
	@Transactional
	void shouldNotSaveAppointment(String appointmentDate, String description) {
		Pet pet = this.petService.findPetById(1);
		Appointment appointment = new Appointment();
		appointment.setDescription(description);
		appointment.setPet(pet);
		appointment.setOwner(pet.getOwner());
		if (appointmentDate != null) {
			appointment.setAppointmentDate(LocalDate.parse(appointmentDate, DateTimeFormatter.ofPattern("yyyy/MM/dd")));			
			assertThrows(ConstraintViolationException.class, () -> {
				this.appointmentService.saveAppointment(appointment, 1);
			});
		} else {
			appointment.setAppointmentDate(null);
			assertThrows(VeterinarianNotAvailableException.class, () -> {
				this.appointmentService.saveAppointment(appointment, 1);
			});
		}
	}

}
