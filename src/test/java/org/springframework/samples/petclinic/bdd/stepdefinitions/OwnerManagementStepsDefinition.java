package org.springframework.samples.petclinic.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;


@Log
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OwnerManagementStepsDefinition extends AbstractStep {
	
	@LocalServerPort
	private int port;
	



    @Given("Im logged in the system as owner1")
  	private void logIn() {
		getDriver().get("http://localhost:" + port + "/");
		getDriver().findElement(By.xpath("//a[contains(@href, '/login')]")).click();
		getDriver().findElement(By.id("username")).click();
		getDriver().findElement(By.id("username")).clear();
		getDriver().findElement(By.id("username")).sendKeys("owner1");
		getDriver().findElement(By.id("password")).click();
		getDriver().findElement(By.id("password")).clear();
		getDriver().findElement(By.id("password")).sendKeys("0wn3333r_1");
		getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	  @When("I update my profile with an invalid password")
	  public void testUpdateOwnerProfileInvalidPassword() throws Exception {
		  getDriver().findElement(By.linkText("OWNER1")).click();
		  getDriver().findElement(By.linkText("My Profile")).click();
		  getDriver().findElement(By.linkText("Edit Owner")).click();
		  getDriver().findElement(By.id("telephone")).click();
		  getDriver().findElement(By.id("telephone")).clear();
		  getDriver().findElement(By.id("telephone")).sendKeys("708535103");
		  getDriver().findElement(By.id("city")).click();
		  getDriver().findElement(By.id("city")).clear();
		  getDriver().findElement(By.id("city")).sendKeys("New Jersey");
		  getDriver().findElement(By.id("user.password")).click();
		  getDriver().findElement(By.id("user.password")).clear();
		  getDriver().findElement(By.id("user.password")).sendKeys("badpass");
		  getDriver().findElement(By.xpath("//button[@type='submit']")).click();
		  
		 
		
	  }
	  
	  @When("I update my profile")
		private void changeProfilePositive() {
		  getDriver().findElement(By.linkText("OWNER1")).click();
		  getDriver().findElement(By.linkText("My Profile")).click();
		  getDriver().findElement(By.linkText("Edit Owner")).click();
		  getDriver().findElement(By.id("telephone")).click();
		  getDriver().findElement(By.id("telephone")).clear();
		  getDriver().findElement(By.id("telephone")).sendKeys("608555103");
		  getDriver().findElement(By.id("city")).click();
		  getDriver().findElement(By.id("city")).clear();
		  getDriver().findElement(By.id("city")).sendKeys("New York");
		  getDriver().findElement(By.xpath("//button[@type='submit']")).click();
		}

//	private void logOut() {
//		 driver.findElement(By.linkText("OWNER1")).click();
//		 driver.findElement(By.linkText("Logout")).click();
//		 driver.findElement(By.xpath("//button[@type='submit']")).click();
//		
//	}
	
	
	  @Then("My profile changes")
	  private void profileChanges() {
		  assertEquals("New York", getDriver().findElement(By.xpath("//tr[3]/td")).getText());
		  stopDriver();
		
	}
	  
	  @Then("My profile doesnt changes")
	  private void profileDoesntChanges() {
		  List<String> spans = getDriver().findElements(By.tagName("span")).stream().map(s -> s.getText()).collect(Collectors.toList());
		  Assert.assertTrue(spans.contains("the password must contain at least one digit, one punctuation symbol, one letter and a minimum length of 10 characters"));
		 stopDriver();
	}
	 
	
}

