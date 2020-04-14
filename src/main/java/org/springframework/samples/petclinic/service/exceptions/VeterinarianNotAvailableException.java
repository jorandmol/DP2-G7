package org.springframework.samples.petclinic.service.exceptions;

public class VeterinarianNotAvailableException extends Exception {

	@Override
	public String getMessage() {
		return "save error";
	}
	
}
