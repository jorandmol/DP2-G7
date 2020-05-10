package org.springframework.samples.petclinic.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddVisitUITest {
  private WebDriver driver;
  private StringBuffer verificationErrors = new StringBuffer();

  @LocalServerPort
  private int port;
  
  @BeforeEach
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testAddVisitUISuccess() throws Exception {
    driver.get("http://localhost:" + port);
    driver.findElement(By.xpath("//div[@id='main-navbar']/ul[3]/li/a")).click();
    driver.findElement(By.id("username")).click();
    driver.findElement(By.id("username")).clear();
    driver.findElement(By.id("username")).sendKeys("vet1");
    driver.findElement(By.id("password")).click();
    driver.findElement(By.id("password")).clear();
    driver.findElement(By.id("password")).sendKeys("v3terinarian_1");
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
    driver.findElement(By.xpath("(//a[contains(text(),'Add visit')])[2]")).click();
    driver.findElement(By.id("description")).click();
    driver.findElement(By.id("description")).clear();
    driver.findElement(By.id("description")).sendKeys("Clinical examination of the pet");
    new Select(driver.findElement(By.name("medicalTests"))).selectByVisibleText("Complete Blood Count (CBC)");
    new Select(driver.findElement(By.name("medicalTests"))).selectByVisibleText("Fluid Analysis");
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    assertEquals("Already registered", driver.findElement(By.xpath("//table[@id='AppointmentsTodayTable']/tbody/tr[2]/td[5]")).getText());
    driver.findElement(By.xpath("//div[@id='main-navbar']/ul[3]/li/a")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).click();
    driver.findElement(By.xpath("//button[@type='submit']")).click();
  }

  @Test
  public void testAddVisitUITestWithErrors() throws Exception {
    driver.get("http://localhost:" + port);
    driver.findElement(By.xpath("//div[@id='main-navbar']/ul[3]/li/a")).click();
    driver.findElement(By.id("username")).click();
    driver.findElement(By.id("username")).clear();
    driver.findElement(By.id("username")).sendKeys("vet1");
    driver.findElement(By.id("password")).click();
    driver.findElement(By.id("password")).clear();
    driver.findElement(By.id("password")).sendKeys("v3terinarian_1");
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Add visit')]")).click();
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    assertEquals("no puede estar vacío", driver.findElement(By.xpath("//form[@id='visit']/div/div[2]/div/span[2]")).getText());
    driver.findElement(By.id("description")).click();
    driver.findElement(By.id("description")).clear();
    driver.findElement(By.id("description")).sendKeys("Neutered pet properly");
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    assertEquals("Already registered", driver.findElement(By.xpath("//table[@id='AppointmentsTodayTable']/tbody/tr/td[5]")).getText());
    driver.findElement(By.xpath("//div[@id='main-navbar']/ul[3]/li/a/strong")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).click();
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
