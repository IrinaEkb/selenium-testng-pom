package utils;

import org.apache.commons.io.FileUtils;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class PageSourceUtil {

    public static String savePageSource(String testName) {
        try {
            String pageSource = DriverManager.getDriver().getPageSource();
            File file = new File("pagesource/" + testName + ".html");
            FileUtils.writeStringToFile(file, pageSource, "UTF-8");
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] getPageSourceBytes(String filePath) {
        try {
            return Files.readAllBytes(new File(filePath).toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    public static ByteArrayInputStream getPageSourceAsStream(String filePath) {
        return new ByteArrayInputStream(getPageSourceBytes(filePath));
    }
}