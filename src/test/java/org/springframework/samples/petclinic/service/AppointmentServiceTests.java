package org.springframework.samples.petclinic.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.DayOfWeek;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.AppointmentRepository;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.service.exceptions.VeterinarianNotAvailableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AppointmentServiceTests {

	@Mock
	private AppointmentRepository appointmentRepository;

	@Mock
	private VetRepository vetRepository;

	protected AppointmentService appointmentService;

	private Appointment appointment;
	private Vet vet;
	private Pet pet;
	private LocalDate appointmentDate;
	private LocalDate appointmentDateFuture;

	@BeforeEach
	void setup() {
		appointmentService = new AppointmentService(appointmentRepository, vetRepository);

		pet = new Pet();
		vet = new Vet();
		appointment = new Appointment();
		appointmentDate = LocalDate.now().getDayOfWeek().equals(DayOfWeek.SUNDAY) ?
				LocalDate.now().plusDays(1) : LocalDate.now();
		appointmentDateFuture = appointmentDate.plusDays(5).getDayOfWeek().equals(DayOfWeek.SUNDAY) ?
				appointmentDate.plusDays(6) : appointmentDate.plusDays(5);

		pet.setId(1);
		vet.setId(1);
		appointment.setId(1);
		appointment.setPet(pet);
		appointment.setAppointmentDate(appointmentDate);
	}


	@Test
	@Transactional
	void shouldDeleteAppointment() {
		this.appointmentService.deleteAppointment(appointment);
		verify(appointmentRepository).delete(appointment);
	}

	@Test
	@Transactional
	void shouldSaveAppointment() {
		when(appointmentRepository.countAppointmentsByPetAndDay(pet.getId(), appointmentDate)).thenReturn(0);
		when(appointmentRepository.countAppointmentsByVetAndDay(vet.getId(), appointmentDate)).thenReturn(0);
		when(vetRepository.findById(vet.getId())).thenReturn(vet);

		try {
			this.appointmentService.saveAppointment(appointment, vet.getId());
		} catch (VeterinarianNotAvailableException e) {
			e.getMessage();
		}

		verify(appointmentRepository).save(appointment);
	}

	@Test
	@Transactional
	void shouldNotSaveAppointmentByPet() {
		when(appointmentRepository.countAppointmentsByPetAndDay(pet.getId(), appointmentDate)).thenReturn(1);

		assertThrows(VeterinarianNotAvailableException.class,
				() -> this.appointmentService.saveAppointment(appointment, vet.getId()));
	}

	@Test
	@Transactional
	void shouldNotSaveAppointmentByVet() {
		when(appointmentRepository.countAppointmentsByVetAndDay(vet.getId(), appointmentDate)).thenReturn(6);

		assertThrows(VeterinarianNotAvailableException.class,
				() -> this.appointmentService.saveAppointment(appointment, vet.getId()));
	}

	@Test
	@Transactional
	void shouldEditAppointment() {
		appointment.setVet(vet);
		appointment.setDescription("Hello World!");

		when(appointmentRepository.countAppointmentsByPetAndDay(pet.getId(), appointmentDateFuture)).thenReturn(0);
		when(appointmentRepository.countAppointmentsByVetAndDay(vet.getId(), appointmentDateFuture)).thenReturn(0);

		Appointment newAppointment = appointment;
		newAppointment.setAppointmentDate(appointmentDateFuture);
		newAppointment.setDescription("Hola Mundo!");

		try {
			this.appointmentService.saveAppointment(newAppointment, vet.getId());
		} catch (VeterinarianNotAvailableException e) {
			e.getMessage();
		}

		verify(appointmentRepository).save(newAppointment);
		assertEquals("Hola Mundo!", newAppointment.getDescription());
	}

	@Test
	@Transactional
	void shouldNotEditAppointmentByPet() {
		appointment.setVet(vet);
		appointment.setDescription("Hello World!");

		when(appointmentRepository.countAppointmentsByPetAndDay(pet.getId(), appointmentDateFuture)).thenReturn(1);

		Appointment newAppointment = appointment;
		newAppointment.setAppointmentDate(appointmentDateFuture);
		newAppointment.setDescription("Hola Mundo!");

		assertThrows(VeterinarianNotAvailableException.class,
				() -> this.appointmentService.saveAppointment(newAppointment, vet.getId()));
	}

	@Test
	@Transactional
	void shouldNotEditAppointmentByVet() {
		appointment.setVet(vet);
		appointment.setDescription("Hello World!");

		when(appointmentRepository.countAppointmentsByVetAndDay(vet.getId(), appointmentDateFuture)).thenReturn(6);

		Appointment newAppointment = appointment;
		newAppointment.setAppointmentDate(appointmentDateFuture);
		newAppointment.setDescription("Hola Mundo!");

		assertThrows(VeterinarianNotAvailableException.class,
				() -> this.appointmentService.saveAppointment(newAppointment, vet.getId()));
	}

}
