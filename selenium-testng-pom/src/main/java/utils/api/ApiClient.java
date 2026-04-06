package utils.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.util.Map;

public class ApiClient {

    // =========================
    // POST
    // =========================

    public static Response post(String path, Object body, String authHeader) {
        return post(path, body, authHeader, null);
    }

    public static Response post(String path, Object body, String authHeader, Map<String, String> cookies) {

        RequestSpecification request = RestAssured.given()
                .contentType("application/json")
                .log().all();

        if (authHeader != null) {
            request.header("Authorization", authHeader);
        }

        if (cookies != null) {
            request.cookies(cookies);
        }

        Response response = request
                .body(body)
                .redirects().follow(false)
                .post(path);

        response.then().log().all();
        attachResponseToListener(response);

        return response;
    }

    // =========================
    // GET
    // =========================

    public static Response get(String path, String authHeader) {
        return get(path, authHeader, null);
    }

    public static Response get(String path, String authHeader, Map<String, String> cookies) {

        RequestSpecification request = RestAssured.given()
                .log().all();

        if (authHeader != null) {
            request.header("Authorization", authHeader);
        }

        if (cookies != null) {
            request.cookies(cookies);
        }

        Response response = request
                .redirects().follow(false)
                .get(path);

        response.then().log().all();
        attachResponseToListener(response);

        return response;
    }

    // =========================
    // DELETE
    // =========================

    public static Response delete(String path, String authHeader) {
        return delete(path, authHeader, null);
    }

    public static Response delete(String path, String authHeader, Map<String, String> cookies) {

        RequestSpecification request = RestAssured.given()
                .log().all();

        if (authHeader != null) {
            request.header("Authorization", authHeader);
        }

        if (cookies != null) {
            request.cookies(cookies);
        }

        Response response = request
                .redirects().follow(false)
                .delete(path);

        response.then().log().all();
        attachResponseToListener(response);

        return response;
    }

    // =========================
    // WITHOUT AUTH
    // =========================

    public static Response post(String path, Object body) {
        return post(path, body, null, null);
    }

    public static Response get(String path) {
        return get(path, null, null);
    }

    public static Response delete(String path) {
        return delete(path, null, null);
    }

    // =========================
    // HELPER
    // =========================

    private static void attachResponseToListener(Response response) {
        ITestResult result = Reporter.getCurrentTestResult();
        if (result != null && response != null) {
            result.setAttribute("apiResponse", response.asPrettyString());
        }
    }
}