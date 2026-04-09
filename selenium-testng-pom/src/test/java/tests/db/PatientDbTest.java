package tests.db;

import org.testng.Assert;
import org.testng.annotations.Test;
import utils.DBUtil;
import utils.LogUtil;
import utils.api.PatientPayloadFactory;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

public class PatientDbTest {

    private static final String PATIENT_ENDPOINT = "/patient";

    private String createPatientAndGetUuid(Map<String, Object> payload) {
        Response response = RestAssured
                .given(utils.api.RequestSpecs.adminRequest())
                .body(payload)
                .post(PATIENT_ENDPOINT);
        return response.jsonPath().getString("uuid");
    }

    @Test(description = "[PATIENT-001] Create Patient - DB verification", groups = {"e2e"})
    public void createPatientDb() throws Exception {
        Map<String, Object> payload = PatientPayloadFactory.createValidPatient();
        String uuid = createPatientAndGetUuid(payload);

        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT * FROM patient WHERE uuid = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            Assert.assertTrue(rs.next());
            LogUtil.info("Patient verified in DB: ID=" + rs.getInt("patient_id"));
        }
    }

    @Test(description = "[PATIENT-004] Missing optional fields - DB validation", groups = {"e2e"})
    public void missingOptionalFieldsDb() throws Exception {
        Map<String, Object> payload = PatientPayloadFactory.withoutGender();
        Response response = RestAssured
                .given(utils.api.RequestSpecs.adminRequest())
                .body(payload)
                .post(PATIENT_ENDPOINT);
        Assert.assertEquals(response.getStatusCode(), 400);
    }

    @Test(description = "[PATIENT-006] Birthdate In Future - DB validation", groups = {"e2e"})
    public void birthdateInFutureDb() throws Exception {
        Map<String, Object> payload = PatientPayloadFactory.birthdateInFuture();
        Response response = RestAssured
                .given(utils.api.RequestSpecs.adminRequest())
                .body(payload)
                .post(PATIENT_ENDPOINT);
        Assert.assertEquals(response.getStatusCode(), 400);
    }

    @Test(description = "[PATIENT-005] Non-existing patient_id - DB check", groups = {"e2e"})
    public void nonExistingPatientIdDb() throws Exception {
        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT * FROM patient WHERE patient_id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, 999999);
            ResultSet rs = ps.executeQuery();
            Assert.assertFalse(rs.next());
            LogUtil.info("No patient found for patient_id: 999999");
        }
    }
}