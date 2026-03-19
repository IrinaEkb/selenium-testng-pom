package tests.ui;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import utils.ConfigReader;
import utils.LogUtil;

public class HomePageTest extends BaseTest {

    @Test(description = "[UI-001][Smoke] Verify home page loads and Explore buttons are visible", groups = {"smoke", "ui"})
    public void testHomePage() {
        String url = ConfigReader.get("ui.base.url") + "/demo";
        LogUtil.info("Opening URL: " + url);
        driver.get(url);
        HomePage homePage = new HomePage(driver);
        LogUtil.info("Current URL: " + driver.getCurrentUrl());

        Assert.assertTrue(driver.getCurrentUrl().contains("/demo"));
        Assert.assertTrue(homePage.isDemoPageDisplayed());
        Assert.assertTrue(homePage.isExplore3Displayed());
        Assert.assertTrue(homePage.isExplore2Displayed());
        System.out.println(driver.getCurrentUrl());
        LogUtil.info("Home page elements verified");
    }
    @Test(description = "[UI-002][Smoke] Click Explore OpenMRS 3 button", groups = {"smoke", "ui"})
    public void testExploreOpenMRS3() {

        String url = ConfigReader.get("ui.base.url") + "/demo";
        LogUtil.info("Opening URL: " + url);

        driver.get(url);
        HomePage homePage = new HomePage(driver);
        LogUtil.info("Click Explore OpenMRS 3");
        homePage.clickExplore3();
        LogUtil.info("Current URL after click: " + driver.getCurrentUrl());
        Assert.assertTrue(driver.getCurrentUrl().contains("openmrs/spa/login"));
    }

    @Test(description = "[UI-003][Smoke] Click Explore OpenMRS 2 button", groups = {"smoke", "ui"})
    public void testExploreOpenMRS2() {

        String url = ConfigReader.get("ui.base.url") + "/demo";
        LogUtil.info("Opening URL: " + url);

        driver.get(url);
        HomePage homePage = new HomePage(driver);
        LogUtil.info("Click Explore OpenMRS 2");
        homePage.clickExplore2();
        LogUtil.info("Current URL after click: " + driver.getCurrentUrl());
        Assert.assertTrue(driver.getCurrentUrl().contains("openmrs/login.htm"));
    }
}