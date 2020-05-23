package org.springframework.samples.petclinic.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptRejectStayStepDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;
	
	@Given("I am not logged in the system")
	public void IamNotLogged() throws Exception{		
		getDriver().get("http://localhost:"+port);
		WebElement element=getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul[3]/li/a"));
		if(element==null || !element.getText().equalsIgnoreCase("login")) {
			getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul[3]/li/a")).click();
			getDriver().findElement(By.linkText("Logout")).click();
			getDriver().findElement(By.xpath("//button[@type='submit']")).click();
		}
	}
	@When("Im logged in the system as {string}")
	public void IdoLoginAs(String username) throws Exception {		
		loginAs(username,passwordOf(username),port, getDriver());		
	}
	
	public static void loginAs(String username,int port,WebDriver driver) {
		loginAs(username,passwordOf(username),port, driver);
	}
	
	public static void loginAs(String username,String password,int port,WebDriver driver) {				
		driver.get("http://localhost:"+port);
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul[3]/li/a")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys(username);
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	private static String passwordOf(String username) {
		String result="4dm1n";
		if("owner1".equals(username))
			result="0wn3333r_1";
		if("vet1".equals(username))
			result="v3terinarian_1";
		return result;
	}
    
    @And("I accept a pending stay request")
    public void goStaysAndAccept() {
		getDriver().findElement(By.linkText("STAYS")).click();
		getDriver().findElement(By.linkText("Change Status")).click();
		new Select(getDriver().findElement(By.id("status"))).selectByVisibleText("ACCEPTED");
	    getDriver().findElement(By.xpath("//option[@value='ACCEPTED']")).click();
	    getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}
    
    @And("I reject a pending stay request")
    public void goStaysAndReject() {
		getDriver().findElement(By.linkText("STAYS")).click();
		getDriver().findElement(By.linkText("Change Status")).click();
		new Select(getDriver().findElement(By.id("status"))).selectByVisibleText("REJECTED");
	    getDriver().findElement(By.xpath("//option[@value='REJECTED']")).click();
	    getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}
    
    @And("I try to reject an accepted stay")
    public void acceptRejectedStay() {
    	getDriver().get("http://localhost:" + port + "/admin/stays/1");
		new Select(getDriver().findElement(By.id("status"))).selectByVisibleText("REJECTED");
		getDriver().findElement(By.xpath("//option[@value='REJECTED']")).click();
		getDriver().findElement(By.xpath("//button[@type='submit']")).click();
    }
    
    @Then("The stay is accepted")
    public void checkStayIsAccepted() {
	    assertEquals("ACCEPTED", getDriver().findElement(By.xpath("//table[@id='stayTables']/tbody/tr[2]/td[3]")).getText());
	     
    }
    
    @Then("The stay is rejected")
    public void checkStayIsRejected() {
	    assertEquals("REJECTED", getDriver().findElement(By.xpath("//table[@id='stayTables']/tbody/tr[3]/td[3]")).getText());
	     
    }
    
    @Then("I get an error")
    public void getError() {
	    assertEquals("Stay already confirmed or rejected by admin", getDriver().findElement(By.xpath("//form[@id='stay']/div/div[3]/div/span[2]")).getText());
	     
    }
}