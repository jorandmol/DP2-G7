package org.springframework.samples.petclinic.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
public class NewMedicalTestStepDefinitions extends AbstractStep {
    
    @Then("Not created because the description is empty")
    public void staysGotErrors() {
		List<String> spans = getDriver().findElements(By.tagName("span")).stream().map(s -> s.getText()).collect(Collectors.toList());
		Assert.assertTrue(spans.contains("no puede estar vacío"));		
    }
	
    @And("I see medical tests and I want to create a new one")
	public void thereAreMedicalTestsAndClickNew() {
    	getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).click();
	    getDriver().findElement(By.linkText("Medical tests")).click();
    	getDriver().findElement(By.xpath("//a[contains(@href, 'medical-tests/new')]")).click();
    }
    
    @And("I introduce correct data")
    public void introduceCorrectData() {
    	getDriver().findElement(By.id("name")).click();
		getDriver().findElement(By.id("name")).clear();
		getDriver().findElement(By.id("name")).sendKeys("Blood test");
		getDriver().findElement(By.id("description")).click();
		getDriver().findElement(By.id("description")).clear();
		getDriver().findElement(By.id("description")).sendKeys("blood test");
		getDriver().findElement(By.xpath("//button[@type='submit']")).click();
    }
    
    @And("I introduce incorrect data")
    public void introduceIncorrectData() {
    	getDriver().findElement(By.id("name")).click();
		getDriver().findElement(By.id("name")).clear();
		getDriver().findElement(By.id("name")).sendKeys("Blood test");
		getDriver().findElement(By.xpath("//button[@type='submit']")).click();
    }
    
    
    @Then("There are more medical tests")
	public void thereAreMoreMedicalTests() {
    	WebElement medTable = getDriver().findElement(By.id("medicalTestsTable"));
		List<WebElement> medicalTestList = medTable.findElements(By.id("medicalTest"));
		int medicalTest = medicalTestList.size();
		
		assertTrue(getDriver().findElement(By.xpath("//table[@id='medicalTestsTable']/tbody/tr["+medicalTest+"]/td[2]")).getText()
				.matches("blood test"));
		assertTrue(getDriver().findElement(By.xpath("//table[@id='medicalTestsTable']/tbody/tr["+medicalTest+"]/td")).getText()
				.matches("Blood test"));
		
	}
}
