package org.springframework.samples.petclinic.ui;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateStayUITest {
	
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
	public void testCreateNewStay() throws Exception {
		logIn();
		petsAndNewStay();
		driver.findElement(By.id("registerDate")).click();
		driver.findElement(By.id("registerDate")).clear();
		driver.findElement(By.id("registerDate")).sendKeys("2022/10/20");
		driver.findElement(By.id("releaseDate")).click();
		driver.findElement(By.id("releaseDate")).clear();
		driver.findElement(By.id("releaseDate")).sendKeys("2022/10/22");
		driver.findElement(By.xpath("//form[@id='stay']/div[2]/div")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		logOut();
	}
	
	@Test
	@Order(2)
	public void testCreateNewStayError() throws Exception {
		logIn();
		petsAndNewStay();
		driver.findElement(By.id("registerDate")).click();
		driver.findElement(By.id("registerDate")).clear();
		driver.findElement(By.id("registerDate")).sendKeys("2022/10/20");
		driver.findElement(By.id("releaseDate")).click();
		driver.findElement(By.id("releaseDate")).clear();
		driver.findElement(By.id("releaseDate")).sendKeys("2022/10/21");
		driver.findElement(By.xpath("//form[@id='stay']/div[2]/div")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		driver.findElement(By.id("registerDate")).click();
		driver.findElement(By.id("registerDate")).clear();
		driver.findElement(By.id("registerDate")).sendKeys("2022/11/20");
		driver.findElement(By.xpath("//form[@id='stay']/div")).click();
		driver.findElement(By.id("releaseDate")).click();
		driver.findElement(By.id("releaseDate")).clear();
		driver.findElement(By.id("releaseDate")).sendKeys("2022/11/21");
		driver.findElement(By.xpath("//form[@id='stay']/div[2]/div")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		logOut();
	}
	
	private void petsAndNewStay() {
		driver.findElement(By.linkText("MY PETS")).click();
		driver.findElement(By.linkText("Stays")).click();
		driver.findElement(By.linkText("New stay")).click();
	}
	
	private void logIn() {
		driver.get("http://localhost:" + port + "/");
		driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();
		driver.findElement(By.id("username")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("owner1");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("0wn3333r_1");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	private void logOut() {
		 driver.findElement(By.linkText("OWNER1")).click();
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
