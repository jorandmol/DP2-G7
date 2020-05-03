package org.springframework.samples.petclinic.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptRejectStayUITest {
	
	@LocalServerPort
	private int port;
	
	private String username;
	private WebDriver driver;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	
  @BeforeEach
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }
  
	 @Test
	 @Order(2)
	  public void testRejectStay() throws Exception {
		as("admin1").
		goStaysAndChangeStatus();
		
		// Rechaza la solicitud de estancia
	    new Select(driver.findElement(By.id("status"))).selectByVisibleText("REJECTED");
	    driver.findElement(By.xpath("//option[@value='REJECTED']")).click();
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    
	    assertEquals("REJECTED", driver.findElement(By.xpath("//table[@id='stayTables']/tbody/tr[3]/td[3]")).getText());
	    logOut();
	  }
	 
	 @Test
	 @Order(1)
	  public void testAcceptStay() throws Exception {
		as("admin1").
		goStaysAndChangeStatus();
		
		// Acepta la solicitud de estancia
	    new Select(driver.findElement(By.id("status"))).selectByVisibleText("ACCEPTED");
	    driver.findElement(By.xpath("//option[@value='ACCEPTED']")).click();
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    
	    assertEquals("ACCEPTED", driver.findElement(By.xpath("//table[@id='stayTables']/tbody/tr[2]/td[3]")).getText());
	    logOut();
	  }
	 
	 @Test
	 @Order(3)
	 public void testRejectAnAcceptedStay() {
		as("admin1").
		driver.findElement(By.linkText("STAYS")).click();
		 
		driver.get("http://localhost:" + port + "/admin/stays/1");
		new Select(driver.findElement(By.id("status"))).selectByVisibleText("REJECTED");
	    driver.findElement(By.xpath("//option[@value='REJECTED']")).click();
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    
	    assertEquals("Stay already confirmed or rejected by admin", driver.findElement(By.xpath("//form[@id='stay']/div/div[3]/div/span[2]")).getText());
	    logOut();
	 }

	 private void goStaysAndChangeStatus() {
		driver.findElement(By.linkText("STAYS")).click();
		driver.findElement(By.linkText("Change Status")).click();
	}
	 
	 private void logOut() {
		 driver.findElement(By.linkText("ADMIN1")).click();
		 driver.findElement(By.linkText("Logout")).click();
		 driver.findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	private AcceptRejectStayUITest as(String username) {
		this.username=username;
	    driver.get("http://localhost:" + port);
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

