package org.springframework.samples.petclinic.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddMedicineUITest {
	
	@LocalServerPort
	private int port;
	
	private String username;
	private WebDriver driver;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	private int medicines;
	private String name = "Parodentix Dogs";
	private String date = LocalDate.now().plusYears(3).format(DateTimeFormatter.ofPattern("yyyy/MM/dd")).toString();

  @BeforeEach
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }
  
  @Test
  public void testAddNewMedicine() throws Exception {
	  as("admin1").
	   whenIamLoggedIntheSystem().
	   iSeeMedicineTextAndClickGo();
	   thereAreMedicines();
	   introduceMedicineData();
	   thereAreMoreMedicines();
	   logOut();
  }
  
  @Test
  public void testAddNewMedicineError() throws Exception {
	  as("admin1").
	   whenIamLoggedIntheSystem().
	   iSeeMedicineTextAndClickGo();
	  driver.findElement(By.linkText("New medicine")).click();
	   introduceMedicineData();
	   medicineGotErrors();
	   logOut();
  }

	private void medicineGotErrors() {
		List<String> spans = driver.findElements(By.tagName("span")).stream().map(s -> s.getText()).collect(Collectors.toList());
		Assert.assertTrue(spans.contains("is already in use"));
		driver.findElement(By.linkText("Return")).click();
		
		WebElement medTable = driver.findElement(By.id("medicinesTable"));
		List<WebElement> medicinesList = medTable.findElements(By.id("med"));
		medicines = medicinesList.size();
		assertEquals(medicines,7);
  }

	private AddMedicineUITest whenIamLoggedIntheSystem() {	
		return this;
	}
	
	private void iSeeMedicineTextAndClickGo() {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).click();
	    driver.findElement(By.linkText("Medicines")).click();
	}
	
	private void thereAreMedicines() {
		WebElement medTable = driver.findElement(By.id("medicinesTable"));
		List<WebElement> medicinesList = medTable.findElements(By.id("med"));
		medicines = medicinesList.size();
		assertEquals(medicines,6);
		driver.findElement(By.linkText("New medicine")).click();
	}
	
	private AddMedicineUITest introduceMedicineData(){
	    driver.findElement(By.id("name")).click();
	    driver.findElement(By.id("name")).clear();
	    driver.findElement(By.id("name")).sendKeys(name);
	    driver.findElement(By.id("code")).click();
	    driver.findElement(By.id("code")).clear();
	    driver.findElement(By.id("code")).sendKeys("DXD-123");
	    driver.findElement(By.id("expirationDate")).click();
	    driver.findElement(By.id("expirationDate")).clear();
	    driver.findElement(By.id("expirationDate")).sendKeys(date);
	    driver.findElement(By.id("description")).click();
	    driver.findElement(By.id("description")).clear();
	    driver.findElement(By.id("description")).sendKeys("Ideal para mantener fuerte la dentadura de la mascota");
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    return this;
	  }
	
	private void thereAreMoreMedicines() {
		WebElement medTable = driver.findElement(By.id("medicinesTable"));
		List<WebElement> medicinesList = medTable.findElements(By.id("med"));
		assertEquals(medicinesList.size(), medicines + 1);
	}
	
	private void logOut() {
		 driver.findElement(By.linkText("ADMIN1")).click();
		 driver.findElement(By.linkText("Logout")).click();
		 driver.findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	private AddMedicineUITest as(String username) {
		this.username=username;
	    driver.get("http://localhost:" + port);
	    driver.findElement(By.xpath("//a[contains(@href, '/login')]")).click();
	    driver.findElement(By.id("password")).clear();
	    driver.findElement(By.id("password")).sendKeys(passwordOf(username));
	    driver.findElement(By.id("username")).clear();
	    driver.findElement(By.id("username")).sendKeys(username);
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
		return this;
	}
	
	private CharSequence passwordOf(String username) {
		return "4dm1n";
	}
	
	  @AfterEach
	  public void tearDown() throws Exception {
	    driver.quit();
	    String verificationErrorString = verificationErrors.toString();
	    if (!"".equals(verificationErrorString)) {
	      fail(verificationErrorString);
	    }
	  }
	
	  private boolean isElementPresent(By by) {
	    try {
	      driver.findElement(by);
	      return true;
	    } catch (NoSuchElementException e) {
	      return false;
	    }
	  }
	
	  private boolean isAlertPresent() {
	    try {
	      driver.switchTo().alert();
	      return true;
	    } catch (NoAlertPresentException e) {
	      return false;
	    }
	  }
	
	  private String closeAlertAndGetItsText() {
	    try {
	      Alert alert = driver.switchTo().alert();
	      String alertText = alert.getText();
	      if (acceptNextAlert) {
	        alert.accept();
	      } else {
	        alert.dismiss();
	      }
	      return alertText;
	    } finally {
	      acceptNextAlert = true;
	    }
	  }
	}

