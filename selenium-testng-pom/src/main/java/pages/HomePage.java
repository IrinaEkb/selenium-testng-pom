package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {

    private final By exploreO3Button = By.xpath("//a[@class='wp-block-button__link wp-element-button' and contains(text(), 'Explore OpenMRS 3')]");
    private final By exploreO2Button = By.xpath("//a[@class='wp-block-button__link wp-element-button' and contains(text(), 'Explore OpenMRS 2')]");
    private final By demoTitle = By.xpath("//p[@class='wp-block-paragraph' and text()='Explore OpenMRS EMR']");

    public HomePage(WebDriver driver) {
        super(driver);
    }

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