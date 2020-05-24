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
public class AddMedicineStepDefinitions extends AbstractStep {
	
	@LocalServerPort
	private int port;
	
	private int medicines = 0;
	private String name = "Parodentix Dogs";
	private String date = LocalDate.now().plusYears(3).format(DateTimeFormatter.ofPattern("yyyy/MM/dd")).toString();
    
    @Then("There are the same number of medicines")
    public void medicineGotErrors() {
		List<String> spans = getDriver().findElements(By.tagName("span")).stream().map(s -> s.getText()).collect(Collectors.toList());
		Assert.assertTrue(spans.contains("is already in use"));
		getDriver().findElement(By.linkText("Return")).click();
		
		WebElement medTable = getDriver().findElement(By.id("medicinesTable"));
		List<WebElement> medicinesList = medTable.findElements(By.id("med"));
		assertEquals(medicinesList.size(),7);
		
    }
	
    @And("I see medicines and I want to add more")
	public void iSeeMedicineTextAndClickGo() {
		getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).click();
	    getDriver().findElement(By.linkText("Medicines")).click();
		getDriver().findElement(By.linkText("New medicine")).click();
	}
	
    @And("I introduce correctly its data")
	public void introduceMedicineData(){
	    getDriver().findElement(By.id("name")).click();
	    getDriver().findElement(By.id("name")).clear();
	    getDriver().findElement(By.id("name")).sendKeys(name);
	    getDriver().findElement(By.id("code")).click();
	    getDriver().findElement(By.id("code")).clear();
	    getDriver().findElement(By.id("code")).sendKeys("DXD-123");
	    getDriver().findElement(By.id("expirationDate")).click();
	    getDriver().findElement(By.id("expirationDate")).clear();
	    getDriver().findElement(By.id("expirationDate")).sendKeys(date);
	    getDriver().findElement(By.id("description")).click();
	    getDriver().findElement(By.id("description")).clear();
	    getDriver().findElement(By.id("description")).sendKeys("Ideal para mantener fuerte la dentadura de la mascota");
	    getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	  }
    
    @And("I introduce correctly its data but its code is repeated")
	public void introduceMedicineDataRepeatedCode(){
	    getDriver().findElement(By.id("name")).click();
	    getDriver().findElement(By.id("name")).clear();
	    getDriver().findElement(By.id("name")).sendKeys(name);
	    getDriver().findElement(By.id("code")).click();
	    getDriver().findElement(By.id("code")).clear();
	    getDriver().findElement(By.id("code")).sendKeys("PEN-2356");
	    getDriver().findElement(By.id("expirationDate")).click();
	    getDriver().findElement(By.id("expirationDate")).clear();
	    getDriver().findElement(By.id("expirationDate")).sendKeys(date);
	    getDriver().findElement(By.id("description")).click();
	    getDriver().findElement(By.id("description")).clear();
	    getDriver().findElement(By.id("description")).sendKeys("Ideal para mantener fuerte la dentadura de la mascota");
	    getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	  }
	
    @Then("There are more medicines")
	public void thereAreMoreMedicines() {
		WebElement medTable = getDriver().findElement(By.id("medicinesTable"));
		List<WebElement> medicinesList = medTable.findElements(By.id("med"));
		assertEquals(medicinesList.size(), 7);
		
	}
}
