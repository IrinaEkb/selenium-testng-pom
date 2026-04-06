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

        String className = result.getTestClass().getName();

        // ===== UI TESTS =====
        if (className.contains(".ui.")) {
            System.out.println("UI test → taking screenshot");

            String screenshotPath = ScreenshotUtil.captureScreenshot(result.getName());

            if (screenshotPath != null) {
                Allure.addAttachment(
                        "Screenshot - " + result.getName(),
                        ScreenshotUtil.getScreenshotAsStream(screenshotPath)
                );
            }

            String pageSourcePath = PageSourceUtil.savePageSource(result.getName());

            if (pageSourcePath != null) {
                Allure.addAttachment(
                        "Page Source - " + result.getName(),
                        PageSourceUtil.getPageSourceAsStream(pageSourcePath)
                );
            }

            // ===== API TESTS =====
        } else if (className.contains(".api.")) {
            System.out.println("API test → attaching response");

            Object response = result.getAttribute("apiResponse");

            if (response != null) {
                Allure.addAttachment(
                        "API Response",
                        response.toString()
                );
            } else {
                System.out.println("No API response found to attach");
            }
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