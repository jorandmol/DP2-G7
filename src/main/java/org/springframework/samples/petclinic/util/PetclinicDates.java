package org.springframework.samples.petclinic.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PetclinicDates {

	public static String getFormattedFutureDate(LocalDate date, int plusDays, String format) {
		LocalDate requestedDate = date.plusDays(plusDays);
		String res = requestedDate.format(DateTimeFormatter.ofPattern(format));
		if (requestedDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
			res = requestedDate.plusDays(1).format(DateTimeFormatter.ofPattern(format));
		}
		return res;
	}
	
}
