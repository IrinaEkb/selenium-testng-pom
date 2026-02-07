package utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiUtil {
    public static Response getRequest(String endpoint) {
        return RestAssured.get(endpoint);
    }
}