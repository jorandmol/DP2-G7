package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.petclinic.web.MedicineValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * @author Michael Isvy Simple test to make sure that Bean Validation is working (useful
 * when upgrading to a new version of Hibernate Validator/ Bean Validation)
 */
class MedicineValidatorTest {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}
	
	@Test
	void codeShouldNotBeEmpty() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Medicine med = new Medicine();
		med.setName("Paracetamol");
		med.setDescription("Antinflamatorio para todo tipo de mascotas");
		med.setExpirationDate(LocalDate.now().plusYears(2));
		med.setCode("");
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Medicine>> constraintViolations = validator.validate(med);
		
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Medicine> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("code");
		assertThat(violation.getMessage()).isEqualTo("must not be empty");
	}
	
	@Test
	void descriptionShouldNotBeEmpty() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Medicine med = new Medicine();
		med.setName("Paracetamol");
		med.setDescription("");
		med.setExpirationDate(LocalDate.now().plusYears(2));
		med.setCode("BAY-123");
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Medicine>> constraintViolations = validator.validate(med);
		
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Medicine> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("description");
		assertThat(violation.getMessage()).isEqualTo("must not be empty");
	}
	
	@Test
	void dateShouldNotBeNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Medicine med = new Medicine();
		med.setName("Paracetamol");
		med.setDescription("Antinflamatorio");
		med.setExpirationDate(null);
		med.setCode("BAY-123");
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Medicine>> constraintViolations = validator.validate(med);
		
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Medicine> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("expirationDate");
		assertThat(violation.getMessage()).isEqualTo("must not be null");
	}

}
