package utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ScreenshotUtil {

    public static String captureScreenshot(String testName) {
        try {
            File srcFile = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.FILE);
            File destFile = new File("screenshots/" + testName + ".png");
            FileUtils.copyFile(srcFile, destFile);
            return destFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] getScreenshotBytes(String filePath) {
        try {
            return Files.readAllBytes(new File(filePath).toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    public static ByteArrayInputStream getScreenshotAsStream(String filePath) {
        return new ByteArrayInputStream(getScreenshotBytes(filePath));
    }
}