package utils;

import io.qameta.allure.Allure;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        LogUtil.info("Test started: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LogUtil.info("Test passed: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LogUtil.error("Test failed: " + result.getName());

        // Screenshot
        String screenshotPath = ScreenshotUtil.captureScreenshot(result.getName());
        if (screenshotPath != null) {
            Allure.addAttachment("Screenshot - " + result.getName(),
                    ScreenshotUtil.getScreenshotAsStream(screenshotPath));
        }

        // Page source
        String pageSourcePath = PageSourceUtil.savePageSource(result.getName());
        if (pageSourcePath != null) {
            Allure.addAttachment("Page Source - " + result.getName(),
                    PageSourceUtil.getPageSourceAsStream(pageSourcePath));
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LogUtil.warn("Test skipped: " + result.getName());
    }

    @Override
    public void onStart(ITestContext context) {
        LogUtil.info("Test suite started: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        LogUtil.info("Test suite finished: " + context.getName());
    }
}