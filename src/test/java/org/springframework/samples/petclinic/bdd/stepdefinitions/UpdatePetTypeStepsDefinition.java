package org.springframework.samples.petclinic.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class UpdatePetTypeStepsDefinition extends AbstractStep{
	
	String shark = "shark";
	
	  @And("Update a pet type correctly")
	  public void updatePetType() {
		  getDriver().findElement(By.linkText("MANAGEMENT")).click();
		  getDriver().findElement(By.linkText("Pet types")).click();
		  getDriver().findElement(By.linkText("cat")).click();
		  getDriver().findElement(By.xpath("(//input[@id='name'])[2]")).click();
		  getDriver().findElement(By.xpath("(//input[@id='name'])[2]")).clear();
		  getDriver().findElement(By.xpath("(//input[@id='name'])[2]")).sendKeys("shark");
		  getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	  }
	    @Then("Pet type name changes")
	    public void petTypeChanges() {
	    	assertEquals("shark", getDriver().findElement(By.linkText("shark")).getText());
	    }
	    
	    
	    @And("Update a pet type with the same name")
	    public void updatePetTypeWithSameName() {
	    	 getDriver().findElement(By.linkText("MANAGEMENT")).click();
	    	 getDriver().findElement(By.linkText("Pet types")).click();
	    	 getDriver().findElement(By.linkText("dog")).click();
	    	 getDriver().findElement(By.xpath("(//input[@id='name'])[2]")).click();
	    	 getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	    }
	    @Then("Pet type name doesnt changes")
	    public void petTypeNameDoesntChange() {
	    	   assertFalse(getDriver().findElement(By.xpath("(//input[@id='name'])[2]")).getAttribute("value")==shark);
	    }

}
