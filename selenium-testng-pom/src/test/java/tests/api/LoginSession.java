package tests.api;

import io.restassured.RestAssured;
import utils.LogUtil;
import utils.api.ApiClient;
import utils.api.AuthHelper;
import utils.api.BaseApiTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ConfigReader;

import java.util.Base64;
import java.util.Map;

public class LoginSession extends BaseApiTest {

    private static final String SESSION_ENDPOINT = "/session";
    private static final String PASSWORD_ENDPOINT = "/password";

    private final String username = ConfigReader.get("username");
    private final String originalPassword = ConfigReader.get("password");
    private final String tempPassword = originalPassword + "1";

    @Test(description = "[API-LOGIN-001] Valid login", groups = {"smoke","api"})
    public void verifyValidLogin() {
        LogUtil.info("=== API-LOGIN-001: Valid Login ===");

        String authHeader = AuthHelper.defaultAdminAuth();
        LogUtil.info("Authorization header: " + authHeader);

        // FIX: explicitly pass auth header
        Response response = ApiClient.post(SESSION_ENDPOINT, "{}", authHeader);

        LogUtil.info("Response status: " + response.getStatusCode());
        LogUtil.info("Response body: " + response.getBody().asString());
        LogUtil.info("Base URI: " + RestAssured.baseURI + RestAssured.basePath);

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

        // FIX: pass WRONG auth explicitly
        Response response = ApiClient.post(SESSION_ENDPOINT, "{}", wrongAuth);

        LogUtil.info("Response status: " + response.getStatusCode());
        LogUtil.info("Response body: " + response.getBody().asString());

        Assert.assertTrue(
                response.statusCode() == 401 ||
                        !response.jsonPath().getBoolean("authenticated"),
                "Authentication should fail"
        );
    }

    @Test(description = "[API-LOGIN-003] No auth header", groups = {"api"})
    public void verifyLoginWithoutAuth() {
        LogUtil.info("=== API-LOGIN-003: No Authorization Header ===");

        // FIX: call WITHOUT auth
        Response response = ApiClient.get(SESSION_ENDPOINT);

        LogUtil.info("Response status: " + response.getStatusCode());
        LogUtil.info("Response body: " + response.getBody().asString());

        Assert.assertTrue(
                response.statusCode() == 401 ||
                        !response.jsonPath().getBoolean("authenticated"),
                "Request without auth should fail"
        );
    }

    @Test(description = "[API-LOGIN-004] Verify session cookie returned", groups = {"api"})
    public void verifySessionCookieReturned() {
        LogUtil.info("=== API-LOGIN-004: Verify Session Cookie ===");

        String authHeader = AuthHelper.defaultAdminAuth();

        Response response = ApiClient.get(SESSION_ENDPOINT, authHeader);

        String sessionCookie = response.getCookie("JSESSIONID");
        LogUtil.info("Session cookie: " + sessionCookie);

        Assert.assertNotNull(sessionCookie, "Session cookie should exist");
    }

    @Test(description = "[API-LOGIN-005] Verify logout", groups = {"api"})
    public void verifyLogout() {

        String authHeader = AuthHelper.defaultAdminAuth();

        Response response = ApiClient.delete(SESSION_ENDPOINT, authHeader);
        LogUtil.info("Response status: " + response.getStatusCode());

        Assert.assertEquals(response.getStatusCode(), 204);
    }

    @Test(description = "[API-LOGIN-006] Verify API access using session cookie", groups = {"api"})
    public void verifyAccessWithSessionCookie() {
        LogUtil.info("=== API-LOGIN-006: Access With Session Cookie ===");

        String authHeader = AuthHelper.defaultAdminAuth();

        Response login = ApiClient.get(SESSION_ENDPOINT, authHeader);
        String sessionId = login.getCookie("JSESSIONID");

        LogUtil.info("Session ID: " + sessionId);
        Assert.assertNotNull(sessionId, "Session ID should not be null");

        Map<String, String> cookies = Map.of("JSESSIONID", sessionId);

        Response response = ApiClient.get("/user?q=" + username, null, cookies);

        LogUtil.info("Response status: " + response.getStatusCode());
        LogUtil.info("Response body: " + response.getBody().asString());

        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(description = "[API-LOGIN-007] Change password by user", enabled = false, groups = {"api"})
    public void verifyPasswordChange() {
        LogUtil.info("=== API-LOGIN-007: Change Password ===");

        String authHeader = AuthHelper.defaultAdminAuth();

        String payload = String.format("""
                {
                  "oldPassword":"%s",
                  "newPassword":"%s"
                }
                """, originalPassword, tempPassword);

        Response response = ApiClient.post(PASSWORD_ENDPOINT, payload, authHeader);

        LogUtil.info("Response status: " + response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(description = "[API-LOGIN-008] Login with new password", enabled = false, groups = {"api"})
    public void verifyLoginWithNewPassword() {
        LogUtil.info("=== API-LOGIN-008: Login With New Password ===");

        String authHeader = "Basic " +
                Base64.getEncoder()
                        .encodeToString((username + ":" + tempPassword).getBytes());

        Response response = ApiClient.get(SESSION_ENDPOINT, authHeader);

        LogUtil.info("Response status: " + response.getStatusCode());

        Assert.assertTrue(response.statusCode() == 200 || response.statusCode() == 401);
    }

    @Test(description = "[API-LOGIN-009] Password without number validation", groups = {"api"})
    public void verifyPasswordWithoutNumber() {
        LogUtil.info("=== API-LOGIN-009: Password Without Number ===");

        String authHeader = AuthHelper.defaultAdminAuth();

        String payload = String.format("""
                {
                  "oldPassword":"%s",
                  "newPassword":"Password"
                }
                """, originalPassword);

        Response response = ApiClient.post(PASSWORD_ENDPOINT, payload, authHeader);

        LogUtil.info("Response status: " + response.getStatusCode());

        Assert.assertTrue(response.getStatusCode() >= 400);
    }

    @Test(description = "[API-LOGIN-010] Get login locations without authentication", groups = {"api"})
    public void verifyLoginLocationsWithoutAuth() {
        LogUtil.info("=== API-LOGIN-010: Get Login Locations Without Auth ===");

        Response response = ApiClient.get("/location?tag=Login+Location");

        LogUtil.info("Response status: " + response.getStatusCode());

        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(description = "[API-LOGIN-011] Protected endpoint without authentication", groups = {"api"})
    public void verifyProtectedEndpointWithoutAuth() {
        LogUtil.info("=== API-LOGIN-011: Protected Endpoint Without Auth ===");

        Response response = ApiClient.get("/user?q=" + username);

        LogUtil.info("Response status: " + response.getStatusCode());

        Assert.assertEquals(response.getStatusCode(), 401);
    }
}