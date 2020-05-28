package org.springframework.samples.petclinic.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.xmlunit.builder.Input;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

public class AddVisitStepDefinitions extends AbstractStep{

	@Then("Error message appears")
    public void canNotAddVisit() {
		assertEquals("no puede estar vacío", getDriver().findElement(By.xpath("//form[@id='visit']/div/div[2]/div/span[2]")).getText());
	}
	
	@And("I try to add a visit with errors")
	public void tryAddVisitWithErrors() {
		getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
	    getDriver().findElement(By.xpath("//a[contains(text(),'Add visit')]")).click();
	    getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	@Then("A registered message appears")
    public void canAddVisit() {
		assertEquals("Already registered", getDriver().findElement(By.xpath("//table[@id='AppointmentsTodayTable']/tbody/tr[2]/td[5]")).getText());
	}
	
	@And("I add a visit")
	public void addVisit() {
		getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		getDriver().findElement(By.xpath("(//a[contains(text(),'Add visit')])[2]")).click();
		getDriver().findElement(By.id("description")).click();
		getDriver().findElement(By.id("description")).clear();
		getDriver().findElement(By.id("description")).sendKeys("Clinical examination of the pet");
		getDriver().findElement(By.id("medicalTests1")).click();
		getDriver().findElement(By.id("medicalTests2")).click();
	    getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}
}
