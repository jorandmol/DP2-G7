package org.springframework.samples.petclinic.bdd.stepdefinitions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Assert;
import org.openqa.selenium.By;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class CreateTreatmentStepDefinitions extends AbstractStep{
	
	int nAppointments;

	@Then("An error appears")
    public void canNotCreateTreatment() {
		Assert.assertEquals("must not be empty", getDriver().findElement(By.xpath("//form[@id='treatment']/div/div[2]/div/span[2]")).getText().trim());
    }
	
	@And("I try to create a new treatment with errors")
	public void createTreatmentWithErrors() {
		getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span")).click();
		getDriver().findElement(By.linkText("Treatments")).click();
		getDriver().findElement(By.linkText("Add New Treatment")).click();
		getDriver().findElement(By.id("name")).click();
		getDriver().findElement(By.id("name")).clear();
		getDriver().findElement(By.id("name")).sendKeys("Bad treatment");
		getDriver().findElement(By.id("timeLimit")).click();
		getDriver().findElement(By.id("timeLimit")).clear();
		getDriver().findElement(By.id("timeLimit")).sendKeys("2020/11/11");
		getDriver().findElement(By.xpath("//form[@id='treatment']/div[2]/div")).click();
		getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	@Then("A new treatment appears")
    public void canCreateTreatment() {
		assertEquals(getNumberOfTreatments(), nAppointments + 1);
	}
	
	@And("I create a new treatment")
	public void createTreatment() {
		getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span")).click();
		getDriver().findElement(By.linkText("Treatments")).click();
		nAppointments = getNumberOfTreatments();
		getDriver().findElement(By.linkText("Add New Treatment")).click();
		getDriver().findElement(By.id("name")).click();
		getDriver().findElement(By.id("name")).clear();
		getDriver().findElement(By.id("name")).sendKeys("Codein injection");
		getDriver().findElement(By.id("description")).click();
		getDriver().findElement(By.id("description")).clear();
		getDriver().findElement(By.id("description")).sendKeys("Pet should be injected with codein one time per month");
		getDriver().findElement(By.id("timeLimit")).click();
		getDriver().findElement(By.id("timeLimit")).clear();
		getDriver().findElement(By.id("timeLimit")).sendKeys("2020/12/01");
		getDriver().findElement(By.xpath("//option[@value='5']")).click();
		getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	private int getNumberOfTreatments() {
		  return getDriver().findElements(By.xpath("//table[@id='treatmentsTable']/tbody/tr")).size()-1;
	  }
	
}
