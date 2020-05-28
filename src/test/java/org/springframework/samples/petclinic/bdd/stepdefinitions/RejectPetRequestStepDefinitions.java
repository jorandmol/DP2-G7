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
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
public class RejectPetRequestStepDefinitions extends AbstractStep {
    
	public int pets=0;
	
    @Then("Not rejected because the justification is needed")
    public void staysGotErrors() {
		List<String> spans = getDriver().findElements(By.tagName("span")).stream().map(s -> s.getText()).collect(Collectors.toList());
		Assert.assertTrue(spans.contains("justification is mandatory if the application is rejected"));		
    }
    
    @Then("Is accepted so there are less pet requests")
    public void ninaAccepted() {
    	WebElement petsTable = getDriver().findElement(By.id("petsTable"));
		List<WebElement> petsList = petsTable.findElements(By.id("pet"));
		assertEquals(petsList.size(), 3);	
    }
	
    @And("There are pet requests")
	public void thereArePetRequests() {
    	getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).click();
	    getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/ul/li/div/div/p[5]/strong/a")).click();
    }
    
    @And("I want to accept that request")
    public void clickAndAcceptRequest() {
    	getDriver().findElement(By.xpath("//table[@id='petsTable']/tbody/tr[3]/td[2]/a/strong")).click();
		getDriver().findElement(By.name("status")).click();
		new Select(getDriver().findElement(By.name("status"))).selectByVisibleText("ACCEPTED");
		getDriver().findElement(By.xpath("//option[@value='ACCEPTED']")).click();
		getDriver().findElement(By.xpath("//button[@type='submit']")).click();
    }
    
    @And("I want to reject other pet request")
    public void clickAndRejectRequest() {
    	getDriver().findElement(By.xpath("//table[@id='petsTable']/tbody/tr[2]/td[2]/a/strong")).click();
		getDriver().findElement(By.name("status")).click();
		new Select(getDriver().findElement(By.name("status"))).selectByVisibleText("REJECTED");
		getDriver().findElement(By.xpath("//option[@value='REJECTED']")).click();
		getDriver().findElement(By.xpath("//button[@type='submit']")).click();
    }
}
