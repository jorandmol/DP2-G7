package org.springframework.samples.petclinic.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class UpdateOwnerStepsDefinition extends AbstractStep{
	
	 @And("I enter owner data correctly")
	 public void ownerEnterData() {
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
	 @Then("My profile changes")
	 public void ownerProfileChange() {
		  assertEquals("New York", getDriver().findElement(By.xpath("//tr[3]/td")).getText());
	 }
	 
	 @And("I enter a bad password")
	 public void ownerEnterDataWrong() {
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
	 @Then("My profile doesnt changes")
	 public void ownerProfileDoesntChange() {
		 List<String> spans =  getDriver().findElements(By.tagName("span")).stream().map(s -> s.getText()).collect(Collectors.toList());
		  Assert.assertTrue(spans.contains("the password must contain at least one digit, one punctuation symbol, one letter and a minimum length of 10 characters"));
	 }


}
