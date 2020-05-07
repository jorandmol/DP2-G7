package org.springframework.samples.petclinic.ui;

import static org.junit.jupiter.api.Assertions.fail;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateThreatmentsUITest {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@BeforeEach
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		baseUrl = "https://www.google.com/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	@Order(1)
	public void testCreateNewThreatment() throws Exception {
		logIn();
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span")).click();
		driver.findElement(By.linkText("Treatments")).click();
		driver.findElement(By.linkText("Add New Treatment")).click();
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("Codein injection");
		driver.findElement(By.id("description")).click();
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("Pet should be injected with codein one time per month");
		driver.findElement(By.id("timeLimit")).click();
		driver.findElement(By.id("timeLimit")).clear();
		driver.findElement(By.id("timeLimit")).sendKeys("2020/12/01");
		driver.findElement(By.xpath("//option[@value='5']")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		logOut();
	
		
	}

	@Test
	@Order(2)
	public void testCreateNewThreatmentWithoutMedicine() throws Exception {
		logIn();
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span")).click();
		driver.findElement(By.linkText("Treatments")).click();
		driver.findElement(By.linkText("Add New Treatment")).click();
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("Frenadol ingestion");
		driver.findElement(By.id("description")).click();
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("The pet must take frenadol 3 times per day during 1 month");
		driver.findElement(By.id("timeLimit")).click();
		driver.findElement(By.id("timeLimit")).clear();
		driver.findElement(By.id("timeLimit")).sendKeys("2020/10/20");
		driver.findElement(By.xpath("//body/div")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		logOut();
		
	}
	
	@Test
	@Order(3)
	public void testCreateNewThreatmentWithErrors() throws Exception {
	
		logIn();
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span")).click();
		driver.findElement(By.linkText("Treatments")).click();
		driver.findElement(By.linkText("Add New Treatment")).click();
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("Bad treatment");
		driver.findElement(By.id("timeLimit")).click();
		driver.findElement(By.id("timeLimit")).clear();
		driver.findElement(By.id("timeLimit")).sendKeys("2020/11/11");
		driver.findElement(By.xpath("//form[@id='treatment']/div[2]/div")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		logOut();
	}

	private void logIn() {
		driver.get("http://localhost:" + port + "/");
		driver.findElement(By.xpath("//a[contains(@href, '/login')]")).click();
		driver.findElement(By.id("username")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("vet1");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("v3terinarian_1");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		
	}
	
	
	private void logOut() {
		 driver.findElement(By.linkText("VET1")).click();
		 driver.findElement(By.linkText("Logout")).click();
		 driver.findElement(By.xpath("//button[@type='submit']")).click();
		
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
