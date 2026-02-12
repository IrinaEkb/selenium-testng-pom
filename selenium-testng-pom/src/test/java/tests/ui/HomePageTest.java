package tests.ui;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import utils.ConfigReader;




public class HomePageTest extends BaseTest {

    @Test(description = "[UI-001][Smoke] Verify home page loads and Explore buttons are visible", groups = {"smoke", "ui"})
    public void testHomePage() {
        driver.get(ConfigReader.get("base.url") + "/demo");

        HomePage homePage = new HomePage();

        Assert.assertTrue(homePage.isDemoPageDisplayed(), "Demo page title is not displayed");
        Assert.assertTrue(homePage.isExplore3Displayed(), "Explore OpenMRS 3 button not visible");
        Assert.assertTrue(homePage.isExplore2Displayed(), "Explore OpenMRS 2 button not visible");
    }

    @Test(description = "[UI-002][Smoke] Click Explore OpenMRS 3 button", groups = {"smoke", "ui"})
    public void testExploreOpenMRS3() {
        driver.get(ConfigReader.get("base.url") + "/demo");
        HomePage homePage = new HomePage();

        homePage.clickExplore3();

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("openmrs-3"), "URL does not contain 'openmrs-3'");
    }

    @Test(description = "[UI-003][Smoke] Click Explore OpenMRS 2 button", groups = {"smoke", "ui"})
    public void testExploreOpenMRS2() {
        driver.get(ConfigReader.get("base.url") + "/demo");
        HomePage homePage = new HomePage();

        homePage.clickExplore2();

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("openmrs-2"), "URL does not contain 'openmrs-2'");
    }
}