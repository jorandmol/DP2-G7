package org.springframework.samples.petclinic.bdd.stepdefinitions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.springframework.samples.petclinic.util.PetclinicDates;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class CreateAppointmentStepDefinitions extends AbstractStep{

	private String appointmentDate = PetclinicDates.getFormattedFutureDate(LocalDate.now(), 5, "yyyy/MM/dd");
	
	int appointmentsAfter;
	int appointmentsBefore;
	
	@Then("An error message will appear")
    public void canNotCreateAppointment() {
		 assertEquals("Impossible to register an appointment with this fields", getDriver().findElement(By.id("vetError")).getText());
	}
	
	@And("I try to create an appointment with errors")
	public void tryCreateAppointment() {
		getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
		getDriver().findElement(By.linkText("Add Appointment")).click();
		getDriver().findElement(By.xpath("//input[@id='appointmentDate']")).click();
		getDriver().findElement(By.id("appointmentDate")).clear();
		getDriver().findElement(By.id("appointmentDate")).sendKeys(appointmentDate);
		getDriver().findElement(By.id("description")).click();
		getDriver().findElement(By.id("description")).clear();
		getDriver().findElement(By.id("description")).sendKeys("Malestar general");
		getDriver().findElement(By.name("vet")).click();
        new Select(getDriver().findElement(By.name("vet"))).selectByVisibleText("Rafael Ortega");
        getDriver().findElement(By.xpath("//option[@value='4']")).click();
        getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	@Then("A new appointment appears")
    public void canCreateAppointment() {
		assertEquals(appointmentsBefore, appointmentsAfter + 1);
	}
	
	@And("I create an appointment")
	public void createAppointment() {
		getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
        appointmentsAfter = getNumberOfAppointments();
        getDriver().findElement(By.linkText("Add Appointment")).click();
        getDriver().findElement(By.xpath("//input[@id='appointmentDate']")).click();
        getDriver().findElement(By.id("appointmentDate")).clear();
        getDriver().findElement(By.id("appointmentDate")).sendKeys(appointmentDate);
        getDriver().findElement(By.id("description")).click();
        getDriver().findElement(By.id("description")).clear();
        getDriver().findElement(By.id("description")).sendKeys("Malestar general");
        new Select(getDriver().findElement(By.name("vet"))).selectByVisibleText("Rafael Ortega");
        getDriver().findElement(By.xpath("//option[@value='4']")).click();
        getDriver().findElement(By.xpath("//button[@type='submit']")).click();
        appointmentsBefore = getNumberOfAppointments();
	}
	
	private int getNumberOfAppointments( ) {
	  	  return getDriver().findElements(By.xpath("//td[3]/table/tbody/tr")).size()-1;
	    }
}
