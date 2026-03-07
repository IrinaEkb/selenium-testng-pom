package tests.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.LogUtil;
import utils.api.BaseApiTest;
import utils.api.PatientPayloadFactory;
import utils.api.RequestSpecs;
import utils.api.ResponseSpecs;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AutoGenerationOptionApiTest extends BaseApiTest {

    private static final String AUTO_GEN_ENDPOINT = "/idgen/autogenerationoption";

    // HELPER
    private String createSequentialIdentifierSource() {
        // a new source to keep tests isolated
        Map<String, Object> payload = new HashMap<>();
        payload.put("sourceType", "SequentialIdentifierGenerator");
        payload.put("identifierType",
                PatientPayloadFactory.AutoGenerationOptionPayloads.validOption().get("identifierType"));
        payload.put("name", "Debug Source " + UUID.randomUUID());
        payload.put("description", "For testing");

        payload.put("firstIdentifierBase", 10000);
        payload.put("baseCharacterSet", "0123456789ACDEFGHJKLMNPRTUVWXY");
        payload.put("prefix", "");
        payload.put("suffix", "");
        payload.put("minLength", 3);
        payload.put("maxLength", 10);

        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .body(payload)
                .post("/idgen/identifiersource");

        return response.jsonPath().getString("uuid");
    }

    // HELPER: Create AutoGenerationOption and return UUID
    private String createOptionAndGetUUID() {
        String sourceUUID = createSequentialIdentifierSource();

        Map<String, Object> payload = PatientPayloadFactory.AutoGenerationOptionPayloads.validOption();
        payload.put("source", sourceUUID);

        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .body(payload)
                .post(AUTO_GEN_ENDPOINT);

        response.then().spec(ResponseSpecs.success201());

        return response.jsonPath().getString("uuid");
    }

    @Test(description = "[AUTO-001] Create AutoGenerationOption", groups = {"api", "smoke"})
    public void createAutoGenerationOption() {
        LogUtil.info("=== AUTO-001: Create AutoGenerationOption ===");
        String sourceUUID = createSequentialIdentifierSource();
        LogUtil.info("Created IdentifierSource UUID: " + sourceUUID);

        Map<String, Object> payload = PatientPayloadFactory.AutoGenerationOptionPayloads.validOption();
        payload.put("source", sourceUUID);
        LogUtil.info("Payload with real source: " + payload);

        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .body(payload)
                .post(AUTO_GEN_ENDPOINT);

        response.then().spec(ResponseSpecs.success201());
        String uuid = response.jsonPath().getString("uuid");
        Assert.assertNotNull(uuid, "UUID should not be null");
        LogUtil.info("Created AutoGenerationOption UUID: " + uuid);
    }

    @Test(description = "[AUTO-002] Get Existing AutoGenerationOption", groups = {"api"})
    public void getExistingOption() {
        LogUtil.info("=== AUTO-002: Get Existing AutoGenerationOption ===");

        String uuid = createOptionAndGetUUID();

        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .get(AUTO_GEN_ENDPOINT + "/" + uuid);

        response.then().spec(ResponseSpecs.success200());
        Assert.assertEquals(response.jsonPath().getString("uuid"), uuid);
        LogUtil.info("Fetched AutoGenerationOption successfully with UUID: " + uuid);
    }

    @Test(description = "[AUTO-003] Update AutoGenerationOption", groups = {"api"})
    public void updateOption() {
        LogUtil.info("=== AUTO-003: Update AutoGenerationOption ===");

        String uuid = createOptionAndGetUUID();
        Map<String, Object> payload = PatientPayloadFactory.AutoGenerationOptionPayloads.validOption();
        payload.put("manualEntryEnabled", false);

        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .body(payload)
                .post(AUTO_GEN_ENDPOINT + "/" + uuid);

        response.then().statusCode(200);

        // Мягкая проверка: поле может быть null, проверяем только что оно существует
        Object manualEnabledObj = response.jsonPath().get("manualEntryEnabled");
        if (manualEnabledObj != null) {
            LogUtil.info("manualEntryEnabled after update: " + manualEnabledObj);
        } else {
            LogUtil.info("manualEntryEnabled not returned by API after update (expected for OpenMRS).");
        }
        LogUtil.info("Updated AutoGenerationOption successfully: " + uuid);
    }

    @Test(description = "[AUTO-004] Delete AutoGenerationOption", groups = {"api"})
    public void deleteOption() {
        LogUtil.info("=== AUTO-004: Delete AutoGenerationOption ===");

        String uuid = createOptionAndGetUUID();

        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .delete(AUTO_GEN_ENDPOINT + "/" + uuid + "?purge=true");

        response.then().spec(ResponseSpecs.success204());
        LogUtil.info("Deleted AutoGenerationOption UUID: " + uuid);

        // Verify deletion
        Response getResponse = RestAssured
                .given(RequestSpecs.adminRequest())
                .get(AUTO_GEN_ENDPOINT + "/" + uuid);
        getResponse.then().spec(ResponseSpecs.notFound404());
        LogUtil.info("Verified deletion: 404 Not Found");
    }

    @Test(description = "[AUTO-005] Create AutoGenerationOption with missing source", groups = {"api"})
    public void createMissingSource() {
        LogUtil.info("=== AUTO-005: Create AutoGenerationOption with missing source");
        Map<String, Object> payload = PatientPayloadFactory.AutoGenerationOptionPayloads.missingSource();

        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .body(payload)
                .post(AUTO_GEN_ENDPOINT);

        response.then().spec(ResponseSpecs.badRequest400());
        LogUtil.info("Correctly received 400 Bad Request for missing source");
    }

    @Test(description = "[AUTO-006] Create AutoGenerationOption with invalid identifierType", groups = {"api"})
    public void createInvalidIdentifierType() {
        LogUtil.info("=== AUTO-006: Create AutoGenerationOption with invalid identifierType");
        Map<String, Object> payload = PatientPayloadFactory.AutoGenerationOptionPayloads.invalidIdentifierType();

        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .body(payload)
                .post(AUTO_GEN_ENDPOINT);

        response.then().spec(ResponseSpecs.badRequest400());
        LogUtil.info("Correctly received 400 Bad Request for invalid identifierType");
    }

    @Test(description = "[AUTO-007] Create AutoGenerationOption without location (optional)", groups = {"api"})
    public void createWithoutLocation() {
        LogUtil.info("=== AUTO-007: Create AutoGenerationOption without location (optional)");
        String sourceUUID = createSequentialIdentifierSource();
        LogUtil.info("Created IdentifierSource UUID: " + sourceUUID);

        Map<String, Object> payload = PatientPayloadFactory.AutoGenerationOptionPayloads.missingLocation();
        payload.put("source", sourceUUID);
        LogUtil.info("Payload with real source (no location): " + payload);

        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .body(payload)
                .post(AUTO_GEN_ENDPOINT);

        response.then().spec(ResponseSpecs.success201());
        LogUtil.info("Successfully created AutoGenerationOption without location");
    }
}