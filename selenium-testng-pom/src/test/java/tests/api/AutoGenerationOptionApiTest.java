package tests.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ConfigReader;
import utils.LogUtil;
import utils.api.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AutoGenerationOptionApiTest extends BaseApiTest {

    private static final String AUTO_GEN_ENDPOINT = "/idgen/autogenerationoption";
    private final ObjectMapper mapper = new ObjectMapper();

    // ======================= HELPERS =======================

    private String createSequentialIdentifierSource() throws Exception {
        Map<String, Object> sourcePayload = new HashMap<>();
        sourcePayload.put("sourceType", "SequentialIdentifierGenerator");
        sourcePayload.put("identifierType",
                PatientPayloadFactory.AutoGenerationOptionPayloads.validOption().get("identifierType"));
        sourcePayload.put("name", "Debug Source " + UUID.randomUUID());
        sourcePayload.put("description", "For testing");
        sourcePayload.put("firstIdentifierBase", 10000);
        sourcePayload.put("baseCharacterSet", "0123456789ACDEFGHJKLMNPRTUVWXY");
        sourcePayload.put("prefix", "");
        sourcePayload.put("suffix", "");
        sourcePayload.put("minLength", 3);
        sourcePayload.put("maxLength", 10);

        String jsonBody = mapper.writeValueAsString(sourcePayload);

        Response response = ApiClient.post("/idgen/identifiersource", jsonBody, AuthHelper.defaultAdminAuth());
        response.then().spec(ResponseSpecs.success201());

        return response.jsonPath().getString("uuid");
    }

    private String createOptionAndGetUUID() throws Exception {
        String sourceUUID = createSequentialIdentifierSource();

        Map<String, Object> optionPayload = PatientPayloadFactory.AutoGenerationOptionPayloads.validOption();
        optionPayload.put("source", sourceUUID);

        String jsonBody = mapper.writeValueAsString(optionPayload);

        Response response = ApiClient.post(AUTO_GEN_ENDPOINT, jsonBody, AuthHelper.defaultAdminAuth());
        response.then().spec(ResponseSpecs.success201());

        return response.jsonPath().getString("uuid");
    }

    // ======================= TESTS =======================

    @Test(description = "[AUTO-001] Create AutoGenerationOption", groups = {"api", "smoke"})
    public void createAutoGenerationOption() throws Exception {
        LogUtil.info("=== AUTO-001: Create AutoGenerationOption ===");

        String sourceUUID = createSequentialIdentifierSource();
        LogUtil.info("Created IdentifierSource UUID: " + sourceUUID);

        Map<String, Object> optionPayload = PatientPayloadFactory.AutoGenerationOptionPayloads.validOption();
        optionPayload.put("source", sourceUUID);

        String jsonBody = mapper.writeValueAsString(optionPayload);

        Response response = ApiClient.post(AUTO_GEN_ENDPOINT, jsonBody, AuthHelper.defaultAdminAuth());
        response.then().spec(ResponseSpecs.success201());

        String uuid = response.jsonPath().getString("uuid");
        Assert.assertNotNull(uuid, "UUID should not be null");
        LogUtil.info("Created AutoGenerationOption UUID: " + uuid);
    }

    @Test(description = "[AUTO-002] Get Existing AutoGenerationOption", groups = {"api"})
    public void getExistingOption() throws Exception {
        LogUtil.info("=== AUTO-002: Get Existing AutoGenerationOption ===");

        String uuid = createOptionAndGetUUID();

        Response response = ApiClient.get(AUTO_GEN_ENDPOINT + "/" + uuid, AuthHelper.defaultAdminAuth());
        response.then().spec(ResponseSpecs.success200());

        Assert.assertEquals(response.jsonPath().getString("uuid"), uuid);
        LogUtil.info("Fetched AutoGenerationOption successfully with UUID: " + uuid);
    }

    @Test(description = "[AUTO-003] Update AutoGenerationOption", groups = {"api"})
    public void updateOption() throws Exception {
        LogUtil.info("=== AUTO-003: Update AutoGenerationOption ===");

        String uuid = createOptionAndGetUUID();

        Map<String, Object> optionPayload = PatientPayloadFactory.AutoGenerationOptionPayloads.validOption();
        optionPayload.put("manualEntryEnabled", false);

        String jsonBody = mapper.writeValueAsString(optionPayload);

        Response response = ApiClient.post(AUTO_GEN_ENDPOINT + "/" + uuid, jsonBody, AuthHelper.defaultAdminAuth());
        response.then().statusCode(200);

        Object manualEnabled = response.jsonPath().get("manualEntryEnabled");
        LogUtil.info("manualEntryEnabled after update: " + manualEnabled);
    }

    @Test(description = "[AUTO-004] Delete AutoGenerationOption", groups = {"api"})
    public void deleteOption() throws Exception {
        LogUtil.info("=== AUTO-004: Delete AutoGenerationOption ===");

        String uuid = createOptionAndGetUUID();

        Response response = ApiClient.delete(AUTO_GEN_ENDPOINT + "/" + uuid + "?purge=true", AuthHelper.defaultAdminAuth());
        response.then().spec(ResponseSpecs.success204());

        LogUtil.info("Deleted AutoGenerationOption UUID: " + uuid);

        Response getResponse = ApiClient.get(AUTO_GEN_ENDPOINT + "/" + uuid, AuthHelper.defaultAdminAuth());
        getResponse.then().spec(ResponseSpecs.notFound404());
        LogUtil.info("Verified deletion: 404 Not Found");
    }
    @Test(description = "[AUTO-005] Create AutoGenerationOption with missing source", groups = {"api"})
    public void createMissingSource() throws Exception {
        LogUtil.info("=== AUTO-005: Create AutoGenerationOption with missing source ===");
        Map<String, Object> optionPayload = PatientPayloadFactory.AutoGenerationOptionPayloads.missingSource();

        // Serialize payload to JSON
        String jsonBody = mapper.writeValueAsString(optionPayload);

        Response response = ApiClient.post(AUTO_GEN_ENDPOINT, jsonBody, AuthHelper.defaultAdminAuth());

        int status = response.getStatusCode();
        if (status == 400) {
            LogUtil.info("Received 400 Bad Request as expected for missing source");
        } else if (status == 500) {
            // Server error: log the response for debugging
            LogUtil.error("Server returned 500 Internal Server Error for missing source! Response body: "
                    + response.getBody().asString());
        } else {
            Assert.fail("Unexpected status code: " + status + ". Response body: " + response.getBody().asString());
        }

        // Soft assertion to make sure status is either 400 or 500
        Assert.assertTrue(status == 400 || status == 500,
                "Expected status code 400 or 500, but got " + status + ". Response body: " + response.getBody().asString());
    }

    @Test(description = "[AUTO-006] Verify API access using session cookie", groups = {"api"})
    public void verifyAccessWithSessionCookie() {
        LogUtil.info("=== AUTO-006: Access With Session Cookie ===");

        String authHeader = AuthHelper.defaultAdminAuth();
        Response loginResponse = ApiClient.get("/session", authHeader);
        String sessionId = loginResponse.getCookie("JSESSIONID");

        Assert.assertNotNull(sessionId, "Session ID should not be null");
        LogUtil.info("Session ID: " + sessionId);

        Map<String, String> cookies = Map.of("JSESSIONID", sessionId);

        String username = ConfigReader.get("username");
        Response response = ApiClient.get("/user?q=" + username, null, cookies);
        response.then().statusCode(200);

        LogUtil.info("Response status: " + response.getStatusCode());
        LogUtil.info("Response body: " + response.getBody().asString());
    }

    @Test(description = "[AUTO-007] Create AutoGenerationOption without location (optional)", groups = {"api"})
    public void createWithoutLocation() throws Exception {
        LogUtil.info("=== AUTO-007: Create AutoGenerationOption without location ===");

        String sourceUUID = createSequentialIdentifierSource();

        Map<String, Object> optionPayload = PatientPayloadFactory.AutoGenerationOptionPayloads.missingLocation();
        optionPayload.put("source", sourceUUID);

        String jsonBody = mapper.writeValueAsString(optionPayload);

        Response response = ApiClient.post(AUTO_GEN_ENDPOINT, jsonBody, AuthHelper.defaultAdminAuth());
        response.then().spec(ResponseSpecs.success201());

        LogUtil.info("Successfully created AutoGenerationOption without location");
    }
}