package org.springframework.samples.petclinic.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MedicalTestUITest {
	
	private WebDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();
	
    @LocalServerPort
    private int port;

	@BeforeEach
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(300, TimeUnit.SECONDS);
	}

	@Test
	public void testAddMedicalTest() throws Exception {
		as("admin1").
		iClickManagementMedicalTestAndThereAreMedicalsTests();
		iClickNewMedicalTest();
		addNewMedicalTest().
		logOut();
	}


	@Test
	public void testAddMedicalTestWithErrors() throws Exception {
		as("admin1").
		iClickManagementMedicalTestAndThereAreMedicalsTests();
		addNewIncorrectMedicalTest().
		logOut();
	}
	
	
	private MedicalTestUITest addNewIncorrectMedicalTest() {
		driver.findElement(By.xpath("//a[contains(@href, 'medical-tests/new')]")).click();
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("Blood test");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("", driver.findElement(By.id("description")).getAttribute("value"));
		driver.findElement(By.id("description")).click();
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("blood test");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		return this;
	}

	private MedicalTestUITest addNewMedicalTest() {
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("Blood test");
		driver.findElement(By.id("description")).click();
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("blood test");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		
		WebElement medTable = driver.findElement(By.id("medicalTestsTable"));
		List<WebElement> medicalTestList = medTable.findElements(By.id("medicalTest"));
		int medicalTest = medicalTestList.size();
		
		assertTrue(driver.findElement(By.xpath("//table[@id='medicalTestsTable']/tbody/tr["+medicalTest+"]/td[2]")).getText()
				.matches("blood test"));
		assertTrue(driver.findElement(By.xpath("//table[@id='medicalTestsTable']/tbody/tr["+medicalTest+"]/td")).getText()
				.matches("Blood test"));
		
		return this;
	}
	
	private void iClickNewMedicalTest() {
	    driver.findElement(By.xpath("//a[contains(@href, 'medical-tests/new')]")).click();
	}

	private void iClickManagementMedicalTestAndThereAreMedicalsTests() {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
	    driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/ul/li/div/div/p[2]/strong/a")).click();		
	}

	private void logOut() {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul[3]/li/a")).click();
	    driver.findElement(By.xpath("//div[@id='main-navbar']/ul[3]/li/ul/li/div/div/div[2]/p[2]/a")).click();
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	private MedicalTestUITest as(String username) {
		driver.get("http://localhost:" + port);
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul[3]/li/a")).click();
		driver.findElement(By.id("username")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys(username);
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(passwordOf(username));
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
}
