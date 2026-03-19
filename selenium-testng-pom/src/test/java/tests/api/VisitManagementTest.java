package tests.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.LogUtil;
import utils.api.BaseApiTest;
import utils.api.RequestSpecs;
import utils.api.ResponseSpecs;
import utils.api.VisitPayloadFactory;
import utils.api.PatientPayloadFactory;

import java.util.Map;
import java.util.UUID;

public class VisitManagementTest extends BaseApiTest {

    private static final String VISIT_ENDPOINT = "/visit";

    @Test(description = "[VISIT-001] Create Visit", groups = {"api", "smoke"})
    public void createVisit() {

        LogUtil.info("=== VISIT-001: Create Visit ===");

        String patientUuid = createPatient();
        Map<String, Object> payload = VisitPayloadFactory.valid(patientUuid);

        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .body(payload)
                .post(VISIT_ENDPOINT);

        LogUtil.info("Response: " + response.asString());

        response.then().spec(ResponseSpecs.success201());
        Assert.assertNotNull(response.jsonPath().getString("uuid"));
    }

    @Test(description = "[VISIT-002] Get Visit by UUID", groups = {"api"})
    public void getVisit() {

        LogUtil.info("=== VISIT-002: Get Visit ===");

        String visitUuid = createVisitAndGetUUID();

        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .get(VISIT_ENDPOINT + "/" + visitUuid);

        LogUtil.info("Response: " + response.asString());

        response.then().spec(ResponseSpecs.success200());
        Assert.assertEquals(response.jsonPath().getString("uuid"), visitUuid);
    }

    @Test(description = "[VISIT-003] Get Visits by Patient", groups = {"api"})
    public void getVisitsByPatient() {

        LogUtil.info("=== VISIT-003: Get Visits by Patient ===");

        String patientUuid = createPatient();
        createVisit(patientUuid);

        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .queryParam("patient", patientUuid)
                .get(VISIT_ENDPOINT);

        LogUtil.info("Response: " + response.asString());

        response.then().spec(ResponseSpecs.success200());
    }

    @Test(description = "[VISIT-004] Update Visit", groups = {"api"})
    public void updateVisit() {

        LogUtil.info("=== VISIT-004: Update Visit ===");

        String visitUuid = createVisitAndGetUUID();

        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .body(VisitPayloadFactory.updateStartDate())
                .post(VISIT_ENDPOINT + "/" + visitUuid);

        LogUtil.info("Response: " + response.asString());

        response.then().spec(ResponseSpecs.success200());
        Assert.assertTrue(response.jsonPath().getString("startDatetime").contains("2020-01-01"));
    }

    @Test(description = "[VISIT-005] Delete Visit", groups = {"api"})
    public void deleteVisit() {

        LogUtil.info("=== VISIT-005: Delete Visit ===");

        String visitUuid = createVisitAndGetUUID();

        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .delete(VISIT_ENDPOINT + "/" + visitUuid);

        LogUtil.info("Response status: " + response.getStatusCode());

        response.then().spec(ResponseSpecs.success204());
    }

    @Test(description = "[VISIT-006] Get Non-existing Visit", groups = {"api"})
    public void getNonExistingVisit() {

        LogUtil.info("=== VISIT-006: Get Non-existing Visit ===");

        String randomUuid = UUID.randomUUID().toString();

        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .get(VISIT_ENDPOINT + "/" + randomUuid);

        LogUtil.info("Response: " + response.asString());

        response.then().spec(ResponseSpecs.notFound404());
    }

    @Test(description = "[VISIT-007] Create Visit Without Patient", groups = {"api"})
    public void createVisitWithoutPatient() {

        LogUtil.info("=== VISIT-007 ===");

        String patientUuid = createPatient();

        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .body(VisitPayloadFactory.withoutPatient(patientUuid))
                .post(VISIT_ENDPOINT);

        response.then().spec(ResponseSpecs.badRequest400());
    }

    @Test(description = "[VISIT-008] Invalid VisitType", groups = {"api"})
    public void invalidVisitType() {

        LogUtil.info("=== VISIT-008 ===");

        String patientUuid = createPatient();

        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .body(VisitPayloadFactory.invalidVisitType(patientUuid))
                .post(VISIT_ENDPOINT);

        response.then().spec(ResponseSpecs.badRequest400());
    }

    @Test(description = "[VISIT-009] Invalid Date", groups = {"api"})
    public void invalidDate() {

        LogUtil.info("=== VISIT-009 ===");

        String patientUuid = createPatient();

        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .body(VisitPayloadFactory.invalidDate(patientUuid))
                .post(VISIT_ENDPOINT);

        response.then().spec(ResponseSpecs.badRequest400());
    }

    @Test(description = "[VISIT-010] Filter by Date", groups = {"api"})
    public void filterByDate() {

        LogUtil.info("=== VISIT-010 ===");

        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .queryParam("fromStartDate", "2020-01-01")
                .get(VISIT_ENDPOINT);

        response.then().spec(ResponseSpecs.success200());
    }

    // ===== HELPERS =====

    private String createPatient() {
        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .body(PatientPayloadFactory.createValidPatient())
                .post("/patient");

        return response.jsonPath().getString("uuid");
    }

    private String createVisitAndGetUUID() {
        String patientUuid = createPatient();
        return createVisit(patientUuid);
    }

    private String createVisit(String patientUuid) {
        Response response = RestAssured
                .given(RequestSpecs.adminRequest())
                .body(VisitPayloadFactory.valid(patientUuid))
                .post(VISIT_ENDPOINT);

        return response.jsonPath().getString("uuid");
    }
}