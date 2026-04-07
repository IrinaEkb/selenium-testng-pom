package utils.api;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiClient {

    // Base API URL
    private static final String BASE_URL = utils.ConfigReader.get("api.base.url");

    // ----------------------
    // Admin requests
    // ----------------------
    public static Response getAsAdmin(String endpoint) {
        return getAsAdmin(endpoint, null);
    }

    public static Response getAsAdmin(String endpoint, Map<String, String> cookies) {
        RequestSpecification spec = RequestSpecs.adminRequest().baseUri(BASE_URL);
        if (cookies != null) spec.cookies(cookies);
        return spec.when().get(endpoint).andReturn();
    }

    public static Response postAsAdmin(String endpoint, String jsonBody) {
        RequestSpecification spec = RequestSpecs.adminRequest().baseUri(BASE_URL).body(jsonBody);
        return spec.when().post(endpoint).andReturn();
    }

    public static Response deleteAsAdmin(String endpoint) {
        RequestSpecification spec = RequestSpecs.adminRequest().baseUri(BASE_URL);
        return spec.when().delete(endpoint).andReturn();
    }

    // ----------------------
    // GET requests
    // ----------------------
    public static Response get(String endpoint) {
        return get(endpoint, null, null);
    }

    public static Response get(String endpoint, String authHeader) {
        return get(endpoint, authHeader, null);
    }

    public static Response get(String endpoint, Map<String, String> cookies) {
        return get(endpoint, null, cookies);
    }

    public static Response get(String endpoint, String authHeader, Map<String, String> cookies) {
        RequestSpecification spec = given().baseUri(BASE_URL).contentType("application/json");
        if (authHeader != null) spec.header("Authorization", authHeader);
        if (cookies != null) spec.cookies(cookies);
        return spec.when().get(endpoint).andReturn();
    }

    // ----------------------
    // POST requests
    // ----------------------
    public static Response post(String endpoint, String jsonBody) {
        return post(endpoint, jsonBody, null);
    }

    public static Response post(String endpoint, String jsonBody, String authHeader) {
        RequestSpecification spec = given().baseUri(BASE_URL).contentType("application/json").body(jsonBody);
        if (authHeader != null) spec.header("Authorization", authHeader);
        return spec.when().post(endpoint).andReturn();
    }

    // ----------------------
    // DELETE requests
    // ----------------------
    public static Response delete(String endpoint) {
        return delete(endpoint, null);
    }

    public static Response delete(String endpoint, String authHeader) {
        RequestSpecification spec = given().baseUri(BASE_URL).contentType("application/json");
        if (authHeader != null) spec.header("Authorization", authHeader);
        return spec.when().delete(endpoint).andReturn();
    }
}