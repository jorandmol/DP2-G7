package org.springframework.samples.petclinic.ui;

import java.util.regex.Pattern;
import java.time.LocalDate;
import java.util.List;
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
import org.springframework.samples.petclinic.util.PetclinicDates;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class DeleteAppointmentUITest {
  private WebDriver driver;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  private String appointmentDateToDelete = PetclinicDates.getFormattedFutureDate(LocalDate.now(), 5, "yyyy/MM/dd");
  private String appointmentDateToNotDelete = PetclinicDates.getFormattedFutureDate(LocalDate.now(), 1, "yyyy/MM/dd");
  
  @LocalServerPort
  private int port;

  @BeforeEach
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  @Order(1)
  public void testDeleteAppointment() throws Exception {
    driver.get("http://localhost:" + port);
    driver.findElement(By.xpath("//a[contains(@href, '/login')]")).click();
    driver.findElement(By.id("username")).clear();
    driver.findElement(By.id("username")).sendKeys("owner1");
    driver.findElement(By.id("password")).clear();
    driver.findElement(By.id("password")).sendKeys("0wn3333r_1");
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).click();
    driver.findElement(By.linkText("My Profile")).click();
    driver.findElement(By.linkText("Add Appointment")).click();
    driver.findElement(By.xpath("//input[@id='appointmentDate']")).click();
    driver.findElement(By.id("appointmentDate")).clear();
    driver.findElement(By.id("appointmentDate")).sendKeys(appointmentDateToDelete);
    driver.findElement(By.id("description")).clear();
    driver.findElement(By.id("description")).sendKeys("Description");
    driver.findElement(By.name("vet")).click();
    driver.findElement(By.xpath("//option[@value='1']")).click();
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    int nAppointments = getNumberOfAppointments();
    driver.findElement(By.xpath("//td[3]/table/tbody/tr["+ (nAppointments) +"]/td[4]/a")).click();
    assertEquals(getNumberOfAppointments(), nAppointments - 1);
  }
  
  @Test
  @Order(2)
  public void testNotDeleteAppointment() throws Exception {
    driver.get("http://localhost:" + port);
    driver.findElement(By.xpath("//a[contains(@href, '/login')]")).click();
    driver.findElement(By.id("username")).clear();
    driver.findElement(By.id("username")).sendKeys("owner1");
    driver.findElement(By.id("password")).clear();
    driver.findElement(By.id("password")).sendKeys("0wn3333r_1");
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    driver.findElement(By.xpath("//a[contains(@href, '#')]")).click();
    driver.findElement(By.xpath("//a[contains(@href, '/users/profile')]")).click();
    driver.findElement(By.linkText("Add Appointment")).click();
    driver.findElement(By.id("appointmentDate")).click();
    driver.findElement(By.id("appointmentDate")).clear();
    driver.findElement(By.id("appointmentDate")).sendKeys(appointmentDateToNotDelete);
    driver.findElement(By.id("description")).clear();
    driver.findElement(By.id("description")).sendKeys("Description");
    driver.findElement(By.name("vet")).click();
    driver.findElement(By.xpath("//option[@value='1']")).click();
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    int nAppointments = getNumberOfAppointments();
    driver.findElement(By.xpath("//td[3]/table/tbody/tr["+ (nAppointments) +"]/td[4]/a")).click();
    assertEquals("No se puede cancelar una cita con dos o menos días de antelación", driver.findElement(By.className("error-text")).getText().trim());
  }

  @AfterEach
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }
  
  private int getNumberOfAppointments( ) {
	  return driver.findElements(By.xpath("//td[3]/table/tbody/tr")).size()-1;
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