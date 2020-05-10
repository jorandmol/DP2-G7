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
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UpdateThreatmentsUITest {

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
	@Order(1)
	public void testUpdateThreatments() throws Exception {
		logIn();
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span")).click();
		driver.findElement(By.linkText("Treatments")).click();
		driver.findElement(By.linkText("Show Treatment")).click();
		driver.findElement(By.id("editBtn")).click();
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("Treatment 2");
		driver.findElement(By.id("description")).click();
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("Description 2");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		logOut();
		
	}
	@Test
	@Order(2)
	public void testUpdateThreatmentsWithNoChanges() throws Exception {
		logIn();
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span")).click();
		driver.findElement(By.linkText("Treatments")).click();
		driver.findElement(By.linkText("Show Treatment")).click();
		driver.findElement(By.id("editBtn")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		logOut();
	}
	
	

	private void logOut() {
		 driver.findElement(By.linkText("VET1")).click();
		 driver.findElement(By.linkText("Logout")).click();
		 driver.findElement(By.xpath("//button[@type='submit']")).click();
		
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

	@AfterEach
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
}
