package org.springframework.samples.petclinic.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDate;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.springframework.samples.petclinic.util.PetclinicDates;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class DeleteAppointmentStepDefinitions extends AbstractStep{
	
	 private String appointmentDateToDelete = PetclinicDates.getFormattedFutureDate(LocalDate.now(), 1000, "yyyy/MM/dd");
	
	@Then("An error message appears")
    public void canNotDeleteAppointment() {
		Assert.assertEquals("You cannot cancel an appointment two or less days in advance", getDriver().findElement(By.className("error-text")).getText().trim());
    }
	
	@And("I try to delete an appointment with 2 days or left to go")
	public void tryDeleteAppointment() {
		getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
		int nAppointments = getNumberOfAppointments();
		getDriver().findElement(By.xpath("//td[3]/table/tbody/tr["+ (nAppointments) +"]/td[4]/a")).click();
	}
	
	@Then("The number of appointments decrease in one")
    public void canDeleteAppointment() {
		int nAppointments = getNumberOfAppointments();
	    getDriver().findElement(By.xpath("//td[3]/table/tbody/tr[1]/td[4]/a")).click();
		assertEquals(getNumberOfAppointments(), nAppointments - 1);
	}
	
	@And("I delete an appointment")
	public void deleteAppointment() {
		getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
		getDriver().findElement(By.linkText("Add Appointment")).click();
		getDriver().findElement(By.xpath("//input[@id='appointmentDate']")).click();
		getDriver().findElement(By.id("appointmentDate")).clear();
		getDriver().findElement(By.id("appointmentDate")).sendKeys(appointmentDateToDelete);
		getDriver().findElement(By.id("description")).clear();
		getDriver().findElement(By.id("description")).sendKeys("Description");
	    getDriver().findElement(By.name("vet")).click();
	    getDriver().findElement(By.xpath("//option[@value='1']")).click();
	    getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	private int getNumberOfAppointments() {
		  return getDriver().findElements(By.xpath("//td[3]/table/tbody/tr")).size()-1;
	  }

}
