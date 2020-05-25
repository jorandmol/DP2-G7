package org.springframework.samples.petclinic.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class UpdateThreatmentStepsDefinition extends AbstractStep {

	@And("Update a threatment correctly")
	public void updateThreatment() {
		getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span")).click();
		getDriver().findElement(By.linkText("Treatments")).click();
		getDriver().findElement(By.linkText("Show Treatment")).click();
		getDriver().findElement(By.id("editBtn")).click();
		getDriver().findElement(By.id("name")).click();
		getDriver().findElement(By.id("name")).clear();
		getDriver().findElement(By.id("name")).sendKeys("Treatment 2");
		getDriver().findElement(By.id("description")).click();
		getDriver().findElement(By.id("description")).clear();
		getDriver().findElement(By.id("description")).sendKeys("Description 2");
		getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}

	@Then("Threatment data changes")
	public void threatmentDataChanges() {
	    assertEquals("Description 2", getDriver().findElement(By.xpath("//table[@id='treatmentTable']/tbody/tr/td")).getText());
	}

	@And("Update a threatment with no changes")
	public void updateThreatmentWithNoChanges() {
		getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span")).click();
		getDriver().findElement(By.linkText("Treatments")).click();
		getDriver().findElement(By.linkText("Show Treatment")).click();
		getDriver().findElement(By.id("editBtn")).click();
		getDriver().findElement(By.xpath("//button[@type='submit']")).click();

	}

	@Then("Threatment data doesnt changes")
	public void threatmentDataDoesntChanges() {
	    assertEquals("Description 2", getDriver().findElement(By.xpath("//table[@id='treatmentTable']/tbody/tr/td")).getText());

	}
}
