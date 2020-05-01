package org.springframework.samples.petclinic.ui;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.fail;

import java.util.List;
import java.util.concurrent.TimeUnit;

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
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PetRequestUITest {

	@LocalServerPort
	private int port;

	private String username;
	private WebDriver driver;
	private int pets;
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
	public void testPetRequestAccepted() throws Exception {
		as("owner3").whenIamLoggedIntheSystem()
		.goToMyProfile().iClickRequestPetAndIntroduceOne()
		.iShowNinaInMyRequestPending();
		logOut();
		
		as("admin1").whenIamLoggedIntheSystem()
		.iClickRequestsAndThereArePetsRequetsIncludingNina()
		.iClickNinaAndAccepted();
		thereAreLessPet();
		logOut();
		
		as("owner3")
		.whenIamLoggedIntheSystem()
		.iShowNinaInMyPets();
		logOut();
	}
	
	@Test
	public void testPetRequestRejected() throws Exception {
		as("owner1").whenIamLoggedIntheSystem()
		.goToMyProfile().iClickRequestPetAndIntroduceOne()
		.iShowNinaInMyRequestPending();
		logOut();
		
		as("admin1").whenIamLoggedIntheSystem()
		.iClickRequestsAndThereArePetsRequetsIncludingNina()
		.iClickNinaAndRejected();
		thereAreLessPet();
		logOut();
		
		as("owner1")
		.whenIamLoggedIntheSystem()
		.iShowNinaInMyRequestRejected();
		iClickNinaAndShowWhy();
		logOut();
	}
	
	@Test
	public void testPetRequestRejectedWithErrors() throws Exception {
		as("owner6").whenIamLoggedIntheSystem()
		.goToMyProfile().iClickRequestPetAndIntroduceOne()
		.iShowNinaInMyRequestPending();
		logOut();
		
		as("admin1").whenIamLoggedIntheSystem()
		.iClickRequestsAndThereArePetsRequetsIncludingNina()
		.iClickNinaAndRejectedWithErrors();
		logOut();
		
		as("owner6").whenIamLoggedIntheSystem()
		.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a")).click();
		iShowNinaInMyRequestPending();
		logOut();
	}

	private void iClickNinaAndRejectedWithErrors() {
		driver.findElement(By.xpath("//table[@id='petsTable']/tbody/tr[5]/td[2]/a/strong")).click();
		driver.findElement(By.name("status")).click();
		driver.findElement(By.xpath("//option[@value='REJECTED']")).click();	
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	    assertEquals("justification is mandatory if the application is rejected", driver.findElement(By.xpath("//form[@id='pet']/div/div[2]/div/span[2]")).getText());
	}

	private void iClickNinaAndShowWhy() {
	    driver.findElement(By.xpath("//table[@id='petsTable']/tbody/tr[3]/td[2]/a/strong")).click();
		assertEquals("Justification:", driver.findElement(By.xpath("//h3")).getText());
		//falta show why
	}

	private void iClickNinaAndRejected() {
		driver.findElement(By.xpath("//table[@id='petsTable']/tbody/tr[5]/td[2]/a/strong")).click();
		driver.findElement(By.name("status")).click();
		driver.findElement(By.xpath("//option[@value='REJECTED']")).click();
	    driver.findElement(By.id("justification")).click();
	    driver.findElement(By.id("justification")).sendKeys("We cannot accept any pet for up to 2 months as we are collapsed");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	private void iShowNinaInMyRequestRejected() {
	    driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();
		assertEquals("Nina",
				driver.findElement(By.xpath("//table[@id='petsTable']/tbody/tr[3]/td[2]/a/strong")).getText());
		assertEquals("REJECTED",
				driver.findElement(By.xpath("//table[@id='petsTable']/tbody/tr[3]/td[5]")).getText());			
	}

	private void iShowNinaInMyPets() {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		assertEquals("Nina", driver.findElement(By.xpath("//tr[2]/td/dl/dd")).getText());		
	}

	private void thereAreLessPet() {
		WebElement petsTable = driver.findElement(By.id("petsTable"));
		List<WebElement> petsList = petsTable.findElements(By.id("pet"));
		assertEquals(petsList.size(), pets - 1);		
	}

	private void iClickNinaAndAccepted() {
		driver.findElement(By.xpath("//table[@id='petsTable']/tbody/tr[5]/td[2]/a/strong")).click();
		driver.findElement(By.name("status")).click();
		new Select(driver.findElement(By.name("status"))).selectByVisibleText("ACCEPTED");
		driver.findElement(By.xpath("//option[@value='ACCEPTED']")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		
	}

	private PetRequestUITest iClickRequestsAndThereArePetsRequetsIncludingNina() {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[5]/a")).click();
		
		WebElement petsTable = driver.findElement(By.id("petsTable"));
		List<WebElement> petsList = petsTable.findElements(By.id("pet"));
		pets = petsList.size();
		assertEquals(pets,5);
		assertEquals("Nina",
				driver.findElement(By.xpath("//table[@id='petsTable']/tbody/tr[5]/td[2]/a/strong")).getText());
		assertEquals("dog", driver.findElement(By.xpath("//table[@id='petsTable']/tbody/tr[5]/td[4]")).getText());	
		return this;
	}

	private void logOut() {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/ul/li/div/div/div[2]/p[2]/a")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	private void iShowNinaInMyRequestPending() {
		assertEquals("Nina",
				driver.findElement(By.xpath("//table[@id='petsTable']/tbody/tr[3]/td[2]/a/strong")).getText());
		assertEquals("PENDING",
				driver.findElement(By.xpath("//table[@id='petsTable']/tbody/tr[3]/td[5]")).getText());		
	}

	private PetRequestUITest iClickRequestPetAndIntroduceOne() {
		driver.findElement(By.linkText("Request pet")).click();
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("Nina");
		driver.findElement(By.id("birthDate")).click();
		driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/div/a/span")).click();
		driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/div/a/span")).click();
		driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/div/a/span")).click();
		driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/table/tbody/tr/td[4]/a")).click();
		new Select(driver.findElement(By.id("type"))).selectByVisibleText("dog");
		driver.findElement(By.xpath("//option[@value='dog']")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		return this;
	}

	private PetRequestUITest goToMyProfile() {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
		assertEquals("My Profile",
				driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/ul/li[3]/div/div/div/p/a")).getText());
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/ul/li[3]/div/div/div/p/a")).click();
		return this;
	}

	private PetRequestUITest as(String username) {
		this.username = username;
		driver.get("http://localhost:" + port);
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
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
		if (username.equals("admin1")) {
			return "4dm1n";
		} else if (username.equals("owner1")) {
			return "0wn3333r_1";
		}else if (username.equals("owner3")) {
			return "0wn3333r_3";
		}{
			return "0wn3333r_6";
		}
	}

	private PetRequestUITest whenIamLoggedIntheSystem() {
		return this;
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
