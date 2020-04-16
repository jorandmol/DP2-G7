package org.springframework.samples.petclinic.service.exceptions;

public class VeterinarianNotAvailableException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "save error";
	}
	
}
