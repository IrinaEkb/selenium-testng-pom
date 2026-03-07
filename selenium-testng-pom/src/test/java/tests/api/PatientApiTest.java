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

import java.util.Map;
import java.util.UUID;

public class PatientApiTest extends BaseApiTest {

    private static final String PATIENT_ENDPOINT = "/patient";

    @Test(description = "[PATIENT-001] Create Patient", groups = {"api", "smoke"})
    public void createPatient() {

        LogUtil.info("=== PATIENT-001: Create Patient ===");
        Map<String, Object> payload = PatientPayloadFactory.createValidPatient();
        LogUtil.info("=== PATIENT-001: Create Patient ===");

        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .body(payload)
                .post(PATIENT_ENDPOINT);

        LogUtil.info("Response status: " + response.getStatusCode());
        LogUtil.info("Response body: " + response.getBody().asString());

        response.then().spec(ResponseSpecs.success201());
        Assert.assertNotNull(response.jsonPath().getString("uuid"));
        response.then().log().all();
        LogUtil.info("Patient created successfully with UUID: " + response.jsonPath().getString("uuid"));
    }

    @Test(description = "[PATIENT-002] Get Existing Patient", groups = {"api"})
    public void getExistingPatient() {
        LogUtil.info("=== PATIENT-002: Get Existing Patient ===");
        String patientUuid = createPatientAndGetUUID();

        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .get(PATIENT_ENDPOINT + "/" + patientUuid);

        LogUtil.info("Response status: " + response.getStatusCode());
        LogUtil.info("Response body: " + response.getBody().asString());

        response.then().spec(ResponseSpecs.success200());
        Assert.assertEquals(response.jsonPath().getString("uuid"), patientUuid);
    }

    @Test(description = "[PATIENT-003] Update Patient", groups = {"api"})
    public void updatePatient() {
        LogUtil.info("=== PATIENT-003: Update Patient ===");
        String patientUuid = createPatientAndGetUUID();
        LogUtil.info("Updating patient with UUID: " + patientUuid);

        Map<String, Object> updatedPayload = PatientPayloadFactory.createValidPatient();
        LogUtil.info("Updating patient with UUID: " + patientUuid);

        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .body(updatedPayload)
                .post(PATIENT_ENDPOINT + "/" + patientUuid);

        LogUtil.info("Response status: " + response.getStatusCode());
        LogUtil.info("Response body: " + response.getBody().asString());

        response.then().spec(ResponseSpecs.success200());
        LogUtil.info("Updated Payload: " + updatedPayload);
    }

    @Test(description = "[PATIENT-004] Missing Gender", groups = {"api"})
    public void missingGender() {
        Map<String, Object> payload = PatientPayloadFactory.withoutGender();
        LogUtil.info("=== PATIENT-004: Missing Gender ===");

        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .body(payload)
                .post(PATIENT_ENDPOINT);

        LogUtil.info("Response status: " + response.getStatusCode());
        LogUtil.info("Response body: " + response.getBody().asString());

        response.then().spec(ResponseSpecs.badRequest400());
        LogUtil.info("Correctly received 400 Bad Request for missing gender.");
    }

    @Test(description = "[PATIENT-005] Non-existing UUID", groups = {"api"})
    public void nonExistingUUID() {
        LogUtil.info("=== PATIENT-005: Non-existing UUID ===");
        String randomUuid = UUID.randomUUID().toString();
        LogUtil.info("Fetching patient with random UUID: " + randomUuid);

        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .get(PATIENT_ENDPOINT + "/" + randomUuid);

        LogUtil.info("Response status: " + response.getStatusCode());
        LogUtil.info("Response body: " + response.getBody().asString());

        response.then().spec(ResponseSpecs.notFound404());
        LogUtil.info("Correctly received 404 Not Found for non-existing UUID.");
    }

    @Test(description = "[PATIENT-006] Birthdate In Future", groups = {"api"})
    public void birthdateInFuture() {
        LogUtil.info("=== PATIENT-006: Birthdate In Future ===");
        Map<String, Object> payload = PatientPayloadFactory.birthdateInFuture();
        LogUtil.info("Payload: " + payload);

        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .body(payload)
                .post(PATIENT_ENDPOINT);

        LogUtil.info("Response status: " + response.getStatusCode());
        LogUtil.info("Response body: " + response.getBody().asString());

        response.then().spec(ResponseSpecs.badRequest400());
        LogUtil.info("Correctly received 400 Bad Request for future birthdate.");
    }

    @Test(description = "[PATIENT-007] Missing identifiers", groups = {"api"})
    public void missingIdentifiers() {

        LogUtil.info("=== PATIENT-007: Missing identifiers ===");

        Map<String, Object> payload = PatientPayloadFactory.missingIdentifiers();

        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .body(payload)
                .post(PATIENT_ENDPOINT);

        LogUtil.info("Response status: " + response.getStatusCode());
        LogUtil.info("Response body: " + response.getBody().asString());

        response.then().spec(ResponseSpecs.badRequest400());

        LogUtil.info("Correctly received 400 Bad Request for missing identifiers.");
    }

    @Test(description = "[PATIENT-008] Identifier Too Long", groups = {"api"})
    public void identifierTooLong() {
        LogUtil.info("=== PATIENT-008: Identifier Too Long ===");
        Map<String, Object> payload = PatientPayloadFactory.identifierTooLong();
        LogUtil.info("Payload: " + payload);

        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .body(payload)
                .post(PATIENT_ENDPOINT);

        LogUtil.info("Response status: " + response.getStatusCode());
        LogUtil.info("Response body: " + response.getBody().asString());

        response.then().spec(ResponseSpecs.badRequest400());
        LogUtil.info("Correctly received 400 Bad Request for too long identifier.");
    }

    @Test(description = "[PATIENT-009] Invalid IdentifierType UUID", groups = {"api"})
    public void invalidIdentifierType() {
        LogUtil.info("=== PATIENT-009: Invalid IdentifierType UUID ===");
        Map<String, Object> payload = PatientPayloadFactory.invalidIdentifierType();
        LogUtil.info("Payload: " + payload);

        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .body(payload)
                .post(PATIENT_ENDPOINT);

        LogUtil.info("Response status: " + response.getStatusCode());
        LogUtil.info("Response body: " + response.getBody().asString());
        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body:\n" + response.getBody().asString());

        response.then().spec(ResponseSpecs.badRequest400());
        LogUtil.info("Correctly received 400 Bad Request for invalid identifier type.");
    }


    @Test(description = "[PATIENT-010] Missing identifierType", groups = {"api"})
    public void missingIdentifierType() {

        LogUtil.info("=== PATIENT-011: Missing identifierType ===");

        Map<String, Object> payload = PatientPayloadFactory.missingIdentifierType();

        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .body(payload)
                .post(PATIENT_ENDPOINT);

        LogUtil.info("Response status: " + response.getStatusCode());
        LogUtil.info("Response body: " + response.getBody().asString());

        response.then().spec(ResponseSpecs.badRequest400());

        LogUtil.info("Correctly received 400 Bad Request for missing identifierType.");
    }

    @Test(description = "[PATIENT-011] Empty identifiers array", groups = {"api"})
    public void emptyIdentifiers() {

        LogUtil.info("=== PATIENT-012: Empty identifiers array ===");

        Map<String, Object> payload = PatientPayloadFactory.emptyIdentifiers();

        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .body(payload)
                .post(PATIENT_ENDPOINT);

        LogUtil.info("Response status: " + response.getStatusCode());
        LogUtil.info("Response body: " + response.getBody().asString());

        response.then().spec(ResponseSpecs.badRequest400());

        LogUtil.info("Correctly received 400 Bad Request for empty identifiers array.");
    }



    // Helper method to create a patient and return UUID
    private String createPatientAndGetUUID() {
        Map<String, Object> payload = PatientPayloadFactory.createValidPatient();
        LogUtil.info("Creating patient with payload: " + payload);
        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .body(payload)
                .post(PATIENT_ENDPOINT);
        String uuid = response.jsonPath().getString("uuid");
        LogUtil.info("Created patient with UUID: " + uuid);
        return uuid;
    }
}