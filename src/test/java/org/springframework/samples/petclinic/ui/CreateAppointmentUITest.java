package org.springframework.samples.petclinic.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
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
import org.springframework.samples.petclinic.util.PetclinicDates;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateAppointmentUITest {
    private WebDriver driver;
    private StringBuffer verificationErrors = new StringBuffer();
    private String appointmentDate = PetclinicDates.getFormattedFutureDate(LocalDate.now(), 5, "yyyy/MM/dd");
    
    @LocalServerPort
    private int port;
    
    @BeforeEach
    public void setUp() throws Exception {
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void testCreateAppointmentUI() throws Exception {
        driver.get("http://localhost:" + port);
        driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("owner1");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("0wn3333r_1");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
        int appointmentsAfter = getNumberOfAppointments();
        driver.findElement(By.linkText("Add Appointment")).click();
        driver.findElement(By.xpath("//input[@id='appointmentDate']")).click();
        driver.findElement(By.id("appointmentDate")).clear();
        driver.findElement(By.id("appointmentDate")).sendKeys(appointmentDate);
        driver.findElement(By.id("description")).click();
        driver.findElement(By.id("description")).clear();
        driver.findElement(By.id("description")).sendKeys("Malestar general");
        new Select(driver.findElement(By.name("vet"))).selectByVisibleText("Rafael Ortega");
        driver.findElement(By.xpath("//option[@value='4']")).click();
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        int appointmentsBefore = getNumberOfAppointments();
        assertEquals(appointmentsBefore, appointmentsAfter + 1);
    }
    
    @Test
    public void testNotCreateAppointmentByPetUI() throws Exception {
    	driver.get("http://localhost:" + port);
        driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("owner1");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("0wn3333r_1");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
        driver.findElement(By.linkText("Add Appointment")).click();
        driver.findElement(By.xpath("//input[@id='appointmentDate']")).click();
        driver.findElement(By.id("appointmentDate")).clear();
        driver.findElement(By.id("appointmentDate")).sendKeys(appointmentDate);
        driver.findElement(By.id("description")).click();
        driver.findElement(By.id("description")).clear();
        driver.findElement(By.id("description")).sendKeys("Malestar general");
        driver.findElement(By.name("vet")).click();
        new Select(driver.findElement(By.name("vet"))).selectByVisibleText("Rafael Ortega");
        driver.findElement(By.xpath("//option[@value='4']")).click();
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        assertEquals("Impossible to register an appointment with this fields", driver.findElement(By.id("vetError")).getText());
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
    
}
