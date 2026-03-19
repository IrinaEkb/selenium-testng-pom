package base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import utils.ConfigReader;
import utils.DriverManager;
import utils.TestListener;

@Listeners(TestListener.class)
public class BaseTest {
    protected WebDriver driver;

    @Parameters({"browser"})
    @BeforeMethod
    public void setupDriver(@Optional("chrome") String browser) {
        DriverManager.setDriver(browser);
        driver = DriverManager.getDriver();
    }

    @AfterMethod
    public void teardownDriver() {

        DriverManager.quitDriver();
    }
}