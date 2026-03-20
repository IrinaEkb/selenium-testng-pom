package utils.api;

import utils.ConfigReader;

import java.util.Base64;

public class AuthHelper {
    public static String basicAuth(String username, String password) {

        String auth = username + ":" + password;

        return "Basic " +
                Base64.getEncoder()
                        .encodeToString(auth.getBytes());
    }

    public static String defaultAdminAuth() {
        return basicAuth(
                ConfigReader.get("username"),
                ConfigReader.get("password")
        );
    }
}

