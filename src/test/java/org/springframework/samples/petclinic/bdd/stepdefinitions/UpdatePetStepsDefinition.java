package org.springframework.samples.petclinic.bdd.stepdefinitions;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class UpdatePetStepsDefinition extends AbstractStep {



	private int contarMascotas() {
		WebElement tablaMascotas = getDriver().findElement(By.id("petsTable"));
		List<WebElement> filasDeTablaMascotas = tablaMascotas.findElements(By.id("pet"));
		return filasDeTablaMascotas.size();
	}

	@And("Disable my pet")
	public void disablePet() {
		getDriver().findElement(By.linkText("MY PETS")).click();
		getDriver().findElement(By.xpath("(//a[contains(text(),'Disable pet')])[2]")).click();

	}

	@Then("My pet dissapears")
	public void petDissapears() {
		assertTrue(contarMascotas() == 1 );
	}

	@And("Disable my pet with events")
	public void disablePetWrong() {

		getDriver().findElement(By.linkText("MY PETS")).click();
		getDriver().findElement(By.xpath("(//a[contains(text(),'Disable pet')])[1]")).click();

	}

	@Then("My pet doesnt dissapears")
	public void petDoesntDissapears() {
		assertTrue(contarMascotas() == 1 );

	}

}
