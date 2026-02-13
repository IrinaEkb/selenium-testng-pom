package tests.ui;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import utils.ConfigReader;

public class HomePageTest extends BaseTest {

    @Test(description = "[UI-001][Smoke] Verify home page loads and Explore buttons are visible", groups = {"smoke", "ui"})
    public void testHomePage() {
        driver.get(ConfigReader.get("ui.base.url") + "/demo");

        HomePage homePage = new HomePage();

        Assert.assertTrue(driver.getCurrentUrl().contains("/demo"));
        Assert.assertTrue(homePage.isDemoPageDisplayed());
        Assert.assertTrue(homePage.isExplore3Displayed());
        Assert.assertTrue(homePage.isExplore2Displayed());
    }

    @Test(description = "[UI-002][Smoke] Click Explore OpenMRS 3 button", groups = {"smoke", "ui"})
    public void testExploreOpenMRS3() {

        driver.get(ConfigReader.get("ui.base.url") + "/demo");

        driver.get(ConfigReader.get("base.url") + "/demo");

<<<<<<< HEAD

=======
>>>>>>> 66d646b (Added locators, fixed links, added waits in BasePage)
        HomePage homePage = new HomePage();

        homePage.clickExplore3();

        Assert.assertTrue(driver.getCurrentUrl().contains("openmrs/spa/login"));
    }

    @Test(description = "[UI-003][Smoke] Click Explore OpenMRS 2 button", groups = {"smoke", "ui"})
    public void testExploreOpenMRS2() {

        driver.get(ConfigReader.get("ui.base.url") + "/demo");

        driver.get(ConfigReader.get("base.url") + "/demo");

<<<<<<< HEAD

=======
>>>>>>> 66d646b (Added locators, fixed links, added waits in BasePage)
        HomePage homePage = new HomePage();

        homePage.clickExplore2();

        Assert.assertTrue(driver.getCurrentUrl().contains("openmrs/login.htm"));
    }
}