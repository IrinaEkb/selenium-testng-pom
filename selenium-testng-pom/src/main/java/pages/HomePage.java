package pages;

import base.BasePage;
import org.openqa.selenium.By;

public class HomePage extends BasePage {

    private final By exploreO3Button = By.xpath("");
    private final By exploreO2Button = By.xpath("");

    private final By demoTitle = By.xpath("");

    public boolean isDemoPageDisplayed() {
        return isDisplayed(demoTitle);
    }

    public void clickExplore3() {
        click(exploreO3Button);
    }

    public void clickExplore2() {
        click(exploreO2Button);
    }

    public boolean isExplore3Displayed() {
        return isDisplayed(exploreO3Button);
    }

    public boolean isExplore2Displayed() {
        return isDisplayed(exploreO2Button);
    }
}