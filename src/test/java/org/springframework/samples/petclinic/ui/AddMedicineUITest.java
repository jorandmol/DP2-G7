package org.springframework.samples.petclinic.ui;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
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
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	private int medicines;

  @BeforeEach
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "https://www.google.com/";
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
  }

	private AddMedicineUITest whenIamLoggedIntheSystem() {	
		return this;
	}
	
	private void iSeeMedicineTextAndClickGo() {
		assertEquals("Medicines", driver.findElement(By.xpath("(//h3)[2]")).getText());
		driver.findElement(By.xpath("(//a[contains(text(),'Go')])[2]")).click();
	}
	
	private void thereAreMedicines() {
		WebElement medTable = driver.findElement(By.id("medicinesTable"));
		List<WebElement> medicinesList = medTable.findElements(By.id("med"));
		medicines = medicinesList.size();
		assertEquals(medicines,3);
		driver.findElement(By.linkText("New medicine")).click();
	}
	
	private AddMedicineUITest introduceMedicineData(){
	    driver.findElement(By.id("name")).click();
	    driver.findElement(By.id("name")).clear();
	    driver.findElement(By.id("name")).sendKeys("Parodentix Dogs");
	    driver.findElement(By.id("code")).click();
	    driver.findElement(By.id("code")).clear();
	    driver.findElement(By.id("code")).sendKeys("DXD-123");
	    driver.findElement(By.id("expirationDate")).click();
	    driver.findElement(By.id("expirationDate")).clear();
	    driver.findElement(By.id("expirationDate")).sendKeys("2026/04/30");
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
	
	private AddMedicineUITest as(String username) {
		this.username=username;
	    driver.get("http://localhost:8080");
	    driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
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

