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
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;



@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OwnerManagementStepsDefinition extends AbstractStep {
	
	@LocalServerPort
	private int port;
	
	private WebDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();
	
	  @BeforeEach
	  public void setUp() throws Exception {
	    driver = new FirefoxDriver();
	    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	  }
	  
	  @Test
	  public void testUpdateOwnerProfile() throws Exception {
		  logIn();
		  
		  changeProfilePositive();
		  
		  profileChanges();
		 
		  logOut();
	  }
	  


	@Test
	  public void testUpdateOwnerProfileInvalidPassword() throws Exception {
		  logIn();
		  driver.findElement(By.linkText("OWNER1")).click();
		  driver.findElement(By.linkText("My Profile")).click();
		  driver.findElement(By.linkText("Edit Owner")).click();
		  driver.findElement(By.id("telephone")).click();
		  driver.findElement(By.id("telephone")).clear();
		  driver.findElement(By.id("telephone")).sendKeys("708535103");
		  driver.findElement(By.id("city")).click();
		  driver.findElement(By.id("city")).clear();
		  driver.findElement(By.id("city")).sendKeys("New Jersey");
		  driver.findElement(By.id("user.password")).click();
		  driver.findElement(By.id("user.password")).clear();
		  driver.findElement(By.id("user.password")).sendKeys("badpass");
		  driver.findElement(By.xpath("//button[@type='submit']")).click();
		  
		  List<String> spans = driver.findElements(By.tagName("span")).stream().map(s -> s.getText()).collect(Collectors.toList());
		  Assert.assertTrue(spans.contains("the password must contain at least one digit, one punctuation symbol, one letter and a minimum length of 10 characters"));
			
		  logOut();
	  }
@Given("Im logged in the system as {string}")
  	private void logIn() {
		driver.get("http://localhost:" + port + "/");
		driver.findElement(By.xpath("//a[contains(@href, '/login')]")).click();
		driver.findElement(By.id("username")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("owner1");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("0wn3333r_1");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	private void logOut() {
		 driver.findElement(By.linkText("OWNER1")).click();
		 driver.findElement(By.linkText("Logout")).click();
		 driver.findElement(By.xpath("//button[@type='submit']")).click();
		
	}
	@When("I update my profile")
	private void changeProfilePositive() {
		  driver.findElement(By.linkText("OWNER1")).click();
		  driver.findElement(By.linkText("My Profile")).click();
		  driver.findElement(By.linkText("Edit Owner")).click();
		  driver.findElement(By.id("telephone")).click();
		  driver.findElement(By.id("telephone")).clear();
		  driver.findElement(By.id("telephone")).sendKeys("608555103");
		  driver.findElement(By.id("city")).click();
		  driver.findElement(By.id("city")).clear();
		  driver.findElement(By.id("city")).sendKeys("New York");
		  driver.findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	  @Then("My profile changes")
	  private void profileChanges() {
		  assertEquals("New York", driver.findElement(By.xpath("//tr[3]/td")).getText());
		
	}
	
	  @AfterEach
	  public void tearDown() throws Exception {
	    driver.quit();
	    String verificationErrorString = verificationErrors.toString();
	    if (!"".equals(verificationErrorString)) {
	      fail(verificationErrorString);
	    }
	  }
}

