package org.springframework.samples.petclinic.ui;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class NotCreateAppointmentByPetUITest {
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
    public void testNotCreateAppointmentByPetUI() throws Exception {
        driver.get("http://localhost:8080/");
        driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("owner1");
        driver.findElement(By.id("password")).click();
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("0wn3333r_1");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).click();
        driver.findElement(By.linkText("My Profile")).click();
        driver.findElement(By.linkText("Add Appointment")).click();
        driver.findElement(By.id("appointmentDate")).click();
        driver.findElement(By.linkText("29")).click();
        driver.findElement(By.id("description")).click();
        driver.findElement(By.id("description")).clear();
        driver.findElement(By.id("description")).sendKeys("Malestar general");
        driver.findElement(By.name("vet")).click();
        new Select(driver.findElement(By.name("vet"))).selectByVisibleText("Rafael Ortega");
        driver.findElement(By.xpath("//option[@value='4']")).click();
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        driver.findElement(By.linkText("Add Appointment")).click();
        driver.findElement(By.id("appointmentDate")).click();
        driver.findElement(By.linkText("29")).click();
        driver.findElement(By.id("description")).click();
        driver.findElement(By.id("description")).clear();
        driver.findElement(By.id("description")).sendKeys("Malestar general");
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
