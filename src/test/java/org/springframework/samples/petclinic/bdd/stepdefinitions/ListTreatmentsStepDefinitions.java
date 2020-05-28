package org.springframework.samples.petclinic.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

public class ListTreatmentsStepDefinitions extends AbstractStep {
	
	@LocalServerPort
	private int port;

	@Then("I get redirected to error page")
    public void canNotListTreatment() {
		assertEquals("Something happened...", getDriver().findElement(By.id("uops")).getText());
	}
	
	@And("I try to list other owner´s treatment")
	public void tryListOtherTreatments() {
		getDriver().findElement(By.xpath("//a[contains(@href, '/owner/pets')]")).click();
	    getDriver().findElement(By.xpath("//a[contains(text(),'Treatments')]")).click();
	    getDriver().get("http://localhost:"+port+"/owners/1/pets/4/treatments");
	}
	
	@Then("Two lists of current and expired treatments appear")
    public void canListTreatment() {
	    assertEquals("Current Treatments", getDriver().findElement(By.id("crtT")).getText());
	    assertEquals("Expired Treatments", getDriver().findElement(By.id("expT")).getText());
	}
	
	@And("I list my pets treatments")
	public void listTreatments() {
	    getDriver().findElement(By.xpath("//a[contains(@href, '/owner/pets')]")).click();
	    getDriver().findElement(By.xpath("//a[contains(text(),'Treatments')]")).click();
	}
}
