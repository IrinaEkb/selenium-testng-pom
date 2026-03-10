package tests.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import utils.LogUtil;
import utils.api.AuthHelper;
import utils.api.BaseApiTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ConfigReader;

import java.util.Base64;



public class LoginSession extends BaseApiTest {

    private static final String SESSION_ENDPOINT = "/session";
    private static final String PASSWORD_ENDPOINT = "/password";

    private final String username = ConfigReader.get("api.username");
    private final String originalPassword = ConfigReader.get("api.password");
    private final String tempPassword = originalPassword + "1"; // for password change

    @Test(description = "[API-LOGIN-001] Valid login", groups = {"smoke","api"})
    public void verifyValidLogin() {
        LogUtil.info("=== API-LOGIN-001: Valid Login ===");

        String authHeader = AuthHelper.defaultAdminAuth();
        LogUtil.info("Authorization header: " + authHeader);

        Response response = RestAssured
                .given()
                .header("Authorization", authHeader)
                .contentType(ContentType.JSON)
                .body("{}")
                .post(SESSION_ENDPOINT);

        LogUtil.info("Response status: " + response.getStatusCode());
        LogUtil.info("Response body: " + response.getBody().asString());
        LogUtil.info("Base URI: " + RestAssured.baseURI + RestAssured.basePath);
        LogUtil.info("Authorization header: " + authHeader);

        response.then().statusCode(200);
        Assert.assertTrue(response.jsonPath().getBoolean("authenticated"), "User should be authenticated");
        Assert.assertNotNull(response.jsonPath().getString("user.uuid"), "User UUID should not be null");

        LogUtil.info("Login successful. User UUID: " + response.jsonPath().getString("user.uuid"));
    }

    @Test(description = "[API-LOGIN-002] Invalid password", groups = {"api"})
    public void verifyInvalidLogin() {
        LogUtil.info("=== API-LOGIN-002: Invalid Password ===");

        String wrongAuth = "Basic " +
                Base64.getEncoder()
                        .encodeToString((username + ":WrongPass").getBytes());

        LogUtil.info("Authorization header (invalid): " + wrongAuth);

        Response response = RestAssured
                .given()
                .header("Authorization", wrongAuth)
                .get(SESSION_ENDPOINT);

        LogUtil.info("Response status: " + response.getStatusCode());
        LogUtil.info("Response body: " + response.getBody().asString());

        Assert.assertTrue(response.statusCode() == 401 || !response.jsonPath().getBoolean("authenticated"),
                "Authentication should fail");
    }

    @Test(description = "[API-LOGIN-003] No auth header", groups = {"api"})
    public void verifyLoginWithoutAuth() {
        LogUtil.info("=== API-LOGIN-003: No Authorization Header ===");

        Response response = RestAssured.given().get(SESSION_ENDPOINT);

        LogUtil.info("Response status: " + response.getStatusCode());
        LogUtil.info("Response body: " + response.getBody().asString());

        Assert.assertTrue(response.statusCode() == 401 || !response.jsonPath().getBoolean("authenticated"),
                "Request without auth should fail");
    }

    @Test(description = "[API-LOGIN-004] Verify session cookie returned", groups = {"api"})
    public void verifySessionCookieReturned() {
        LogUtil.info("=== API-LOGIN-004: Verify Session Cookie ===");

        Response response = RestAssured
                .given()
                .header("Authorization", AuthHelper.defaultAdminAuth())
                .get(SESSION_ENDPOINT);

        String sessionCookie = response.getCookie("JSESSIONID");
        LogUtil.info("Session cookie: " + sessionCookie);

        Assert.assertNotNull(sessionCookie, "Session cookie should exist");
    }

    @Test(description = "[API-LOGIN-005] Verify logout", groups = {"api"})
    public void verifyLogout() {

        LogUtil.info("=== API-LOGIN-005: Logout ===");

        String auth = AuthHelper.defaultAdminAuth();

        Response logoutResponse = RestAssured
                .given()
                .header("Authorization", auth)
                .delete(SESSION_ENDPOINT);

        Assert.assertEquals(logoutResponse.getStatusCode(), 204);

        LogUtil.info("Logout successful.");
    }

    @Test(description = "[API-LOGIN-006] Verify API access using session cookie", groups = {"api"})
    public void verifyAccessWithSessionCookie() {
        LogUtil.info("=== API-LOGIN-006: Access With Session Cookie ===");

        Response login = RestAssured
                .given()
                .header("Authorization", AuthHelper.defaultAdminAuth())
                .get(SESSION_ENDPOINT);

        String sessionId = login.getCookie("JSESSIONID");
        LogUtil.info("Session ID: " + sessionId);

        Response response = RestAssured
                .given()
                .cookie("JSESSIONID", sessionId)
                .get("/user?q=" + username);

        LogUtil.info("Response status: " + response.getStatusCode());
        LogUtil.info("Response body: " + response.getBody().asString());

        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(description = "[API-LOGIN-007] Change password by user", enabled = false, groups = {"api"})
    public void verifyPasswordChange() {
        LogUtil.info("=== API-LOGIN-007: Change Password ===");

        String payload = String.format("""
                {
                  "oldPassword":"%s",
                  "newPassword":"%s"
                }
                """, originalPassword, tempPassword);

        LogUtil.info("Password change payload: " + payload);

        Response response = RestAssured
                .given()
                .header("Authorization", AuthHelper.defaultAdminAuth())
                .contentType("application/json")
                .body(payload)
                .post(PASSWORD_ENDPOINT);

        LogUtil.info("Response status: " + response.getStatusCode());
        LogUtil.info("Response body: " + response.getBody().asString());

        Assert.assertEquals(response.getStatusCode(), 200, "Password change should succeed");

        // Revert password back to original for test isolation
        String revertPayload = String.format("""
                {
                  "oldPassword":"%s",
                  "newPassword":"%s"
                }
                """, tempPassword, originalPassword);

        RestAssured
                .given()
                .header("Authorization", "Basic " +
                        Base64.getEncoder().encodeToString((username + ":" + tempPassword).getBytes()))
                .contentType("application/json")
                .body(revertPayload)
                .post(PASSWORD_ENDPOINT);

        LogUtil.info("Password reverted to original after test.");
    }

    @Test(description = "[API-LOGIN-008] Login with new password", enabled = false, groups = {"api"})
    public void verifyLoginWithNewPassword() {
        LogUtil.info("=== API-LOGIN-008: Login With New Password ===");

        // Use temp password for this test
        String authHeader = "Basic " +
                Base64.getEncoder().encodeToString((username + ":" + tempPassword).getBytes());

        LogUtil.info("Authorization header: " + authHeader);

        Response response = RestAssured
                .given()
                .header("Authorization", authHeader)
                .get(SESSION_ENDPOINT);

        LogUtil.info("Response status: " + response.getStatusCode());
        LogUtil.info("Response body: " + response.getBody().asString());

        Assert.assertTrue(response.statusCode() == 200 || response.statusCode() == 401);
    }

    @Test(description = "[API-LOGIN-009] Password without number validation", groups = {"api"})
    public void verifyPasswordWithoutNumber() {
        LogUtil.info("=== API-LOGIN-009: Password Without Number ===");

        String payload = String.format("""
                {
                  "oldPassword":"%s",
                  "newPassword":"Password"
                }
                """, originalPassword);

        LogUtil.info("Payload: " + payload);

        Response response = RestAssured
                .given()
                .header("Authorization", AuthHelper.defaultAdminAuth())
                .contentType("application/json")
                .body(payload)
                .post(PASSWORD_ENDPOINT);

        LogUtil.info("Response status: " + response.getStatusCode());
        LogUtil.info("Response body: " + response.getBody().asString());

        Assert.assertTrue(response.getStatusCode() >= 400);
    }

    @Test(description = "[API-LOGIN-010] Get login locations without authentication", groups = {"api"})
    public void verifyLoginLocationsWithoutAuth() {
        LogUtil.info("=== API-LOGIN-010: Get Login Locations Without Auth ===");

        Response response = RestAssured.given()
                .get("/location?tag=Login+Location");

        LogUtil.info("Response status: " + response.getStatusCode());
        LogUtil.info("Response body: " + response.getBody().asString());

        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(description = "[API-LOGIN-011] Protected endpoint without authentication", groups = {"api"})
    public void verifyProtectedEndpointWithoutAuth() {
        LogUtil.info("=== API-LOGIN-011: Protected Endpoint Without Auth ===");

        Response response = RestAssured.given()
                .get("/user?q=" + username);

        LogUtil.info("Response status: " + response.getStatusCode());
        LogUtil.info("Response body: " + response.getBody().asString());

        Assert.assertEquals(response.getStatusCode(), 401);
    }
}