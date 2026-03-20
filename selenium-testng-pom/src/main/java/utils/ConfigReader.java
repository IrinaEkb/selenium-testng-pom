package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    public static Properties properties = new Properties();

    static {
        String env = System.getProperty("env", "demo"); // default to demo
        String fileName = "src/test/resources/" + env + ".properties";

        try (FileInputStream fis = new FileInputStream(fileName)) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load " + fileName);
        }
    }
    public static String get(String key) {

        return properties.getProperty(key);
    }
}