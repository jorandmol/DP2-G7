package org.springframework.samples.petclinic.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;
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
public class CreateStayStepDefinitions extends AbstractStep {
    
    @Then("There is a stay request in those dates")
    public void staysGotErrors() {
		List<String> spans = getDriver().findElements(By.tagName("span")).stream().map(s -> s.getText()).collect(Collectors.toList());
		Assert.assertTrue(spans.contains("There exists already a Stay"));		
    }
	
    @And("I see stays and I want to create a new one")
	public void iSeeStays() {
    	getDriver().findElement(By.linkText("MY PETS")).click();
		getDriver().findElement(By.linkText("Stays")).click();
		getDriver().findElement(By.linkText("New stay")).click();
		
		getDriver().findElement(By.id("registerDate")).click();
		getDriver().findElement(By.id("registerDate")).clear();
		getDriver().findElement(By.id("registerDate")).sendKeys("2022/10/20");
		getDriver().findElement(By.id("releaseDate")).click();
		getDriver().findElement(By.id("releaseDate")).clear();
		getDriver().findElement(By.id("releaseDate")).sendKeys("2022/10/22");
		getDriver().findElement(By.xpath("//form[@id='stay']/div[2]/div")).click();
		getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}
	
    @Then("There are more stays")
	public void thereAreMoreStays() {
		WebElement stayTable = getDriver().findElement(By.id("medicinesTable"));
		List<WebElement> staysList = stayTable.findElements(By.id("stay"));
		assertEquals(staysList.size(), 1);
		
	}
}
