package tests.ui;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import utils.LogUtil;

public class HomePageTest extends BaseTest {

    private final String demoUrl = "https://openmrs.org/demo"; // Public OpenMRS demo URL

    @Test(description = "[UI-001][Smoke] Verify home page loads and Explore buttons are visible", groups = {"smoke", "ui"})
    public void testHomePage() {
        LogUtil.info("=== UI-001: Verify home page and buttons ===");

        driver.get(demoUrl);
        HomePage homePage = new HomePage(driver);

        LogUtil.info("Current URL: " + driver.getCurrentUrl());

        Assert.assertTrue(homePage.isDemoPageDisplayed(), "Demo page should be displayed");
        Assert.assertTrue(homePage.isExplore3Displayed(), "Explore OpenMRS 3 button should be visible");
        Assert.assertTrue(homePage.isExplore2Displayed(), "Explore OpenMRS 2 button should be visible");

        LogUtil.info("Home page elements verified successfully");
    }

    @Test(description = "[UI-002][Smoke] Click Explore OpenMRS 3 button", groups = {"smoke", "ui"})
    public void testExploreOpenMRS3() {
        LogUtil.info("=== UI-002: Click Explore OpenMRS 3 ===");

        driver.get(demoUrl);
        HomePage homePage = new HomePage(driver);

        LogUtil.info("Clicking Explore OpenMRS 3 button");
        homePage.clickExplore3();

        String currentUrl = driver.getCurrentUrl();
        LogUtil.info("Current URL after click: " + currentUrl);
        Assert.assertTrue(currentUrl.contains("openmrs/spa/login"), "Should navigate to OpenMRS 3 login page");
    }

    @Test(description = "[UI-003][Smoke] Click Explore OpenMRS 2 button", groups = {"smoke", "ui"})
    public void testExploreOpenMRS2() {
        LogUtil.info("=== UI-003: Click Explore OpenMRS 2 ===");

        driver.get(demoUrl);
        HomePage homePage = new HomePage(driver);

        LogUtil.info("Clicking Explore OpenMRS 2 button");
        homePage.clickExplore2();

        String currentUrl = driver.getCurrentUrl();
        LogUtil.info("Current URL after click: " + currentUrl);
        Assert.assertTrue(currentUrl.contains("openmrs/login.htm"), "Should navigate to OpenMRS 2 login page");
    }
}