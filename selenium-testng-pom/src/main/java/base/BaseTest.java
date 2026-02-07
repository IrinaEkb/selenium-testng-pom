package base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import utils.DriverManager;
import utils.TestListener;

@Listeners(TestListener.class)
public class BaseTest {

    @BeforeMethod
    public void setupDriver() {
        DriverManager.setDriver();
        WebDriver driver = DriverManager.getDriver();
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void teardownDriver() {
        DriverManager.quitDriver();
    }
}