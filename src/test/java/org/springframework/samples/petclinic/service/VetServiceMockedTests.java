package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class VetServiceMockedTests {

	@Mock
    private VetRepository vetRepository;

    protected VetService vetService;

    @BeforeEach
    void setup() {
        vetService = new VetService(vetRepository);
    }

    @Test
	@Transactional
	void testFindVetById() {
    	Vet newVet= new Vet();
    	newVet.setId(1);
    	newVet.setFirstName("Elena");
    	Mockito.when(vetRepository.findById(1)).thenReturn(newVet);

    	Vet vet = this.vetService.findVetById(1);

    	assertThat(vet.getFirstName()).isEqualTo("Elena");
    	assertThat(vet.getNrOfSpecialties()).isEqualTo(0);
    }

}
