package tests.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utils.api.BaseApiTest;
import utils.api.PatientPayloadFactory;

import java.util.Map;

public class DebugApi extends BaseApiTest {

    @Test
    public void printIdentifierTypesAndLocations (){

        System.out.println("=== IDENTIFIER TYPES ===");

        Response response = RestAssured
                .given()
                .auth().preemptive().basic("admin", "Admin123")
                .when()
                .get("/patientidentifiertype");

        response.prettyPrint();

        System.out.println("\n=== LOCATIONS ===");

        Response locations = RestAssured
                .given()
                .auth().preemptive().basic("admin", "Admin123")
                .when()
                .get("/location");

        locations.prettyPrint();

    }

    @Test
    public void debugCreateAutoGenerationOption() {
        System.out.println("\n=== DEBUG CREATE AUTOGENERATIONOPTION ===");

        Map<String, Object> payload = PatientPayloadFactory.AutoGenerationOptionPayloads.validOption();
        System.out.println("Payload being sent: " + payload);

        Response response = RestAssured
                .given()
                .auth().preemptive().basic("admin", "Admin123")
                .contentType("application/json")
                .body(payload)
                .post("/idgen/autogenerationoption");

        System.out.println("Response status code: " + response.getStatusCode());
        System.out.println("Response body:");
        response.prettyPrint();
    }
}