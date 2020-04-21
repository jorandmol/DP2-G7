package org.springframework.samples.petclinic.ui;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.*;
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
public class CreateAppointmentUITest {
    private WebDriver driver;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    private String appointmentDate = PetclinicDates.getFormattedFutureDate(LocalDate.now(), 5, "yyyy/MM/dd");
    private String expectedAppointmentDate = PetclinicDates.getFormattedFutureDate(LocalDate.now(), 5, "yyyy-MM-dd");
    
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
        driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
        driver.findElement(By.linkText("My Profile")).click();
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
        assertEquals(expectedAppointmentDate, driver.findElement(By.xpath("//td[3]/table/tbody/tr[3]/td")).getText());
        assertEquals("Edit", driver.findElement(By.linkText("Edit")).getText());
        assertEquals("Delete", driver.findElement(By.linkText("Delete")).getText());
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
        driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
        driver.findElement(By.linkText("My Profile")).click();
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
        assertEquals("Imposible realizar una cita con esos datos", driver.findElement(By.id("vetError")).getText());
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
