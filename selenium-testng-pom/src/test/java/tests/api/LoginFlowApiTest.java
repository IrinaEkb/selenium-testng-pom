package tests.api;

import io.restassured.RestAssured;
import utils.BaseApiTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ConfigReader;

import java.util.Base64;


public class LoginFlowApiTest extends BaseApiTest {

    private String basicAuth() {

        String user = ConfigReader.get("api.username");
        String pass = ConfigReader.get("api.password");

        String auth = user + ":" + pass;

        return "Basic " +
                Base64.getEncoder()
                        .encodeToString(auth.getBytes());
    }

    @Test(
            description = "[API-LOGIN-001] Valid login",
            groups = {"smoke","api"}
    )
    public void verifyValidLogin() {

        Response response =
                RestAssured
                        .given()
                        .header("Authorization", basicAuth())
                        .when()
                        .get("/ws/rest/v1/session");

        response.then().statusCode(200);

        Assert.assertTrue(
                response.jsonPath()
                        .getBoolean("authenticated"));

        Assert.assertNotNull(
                response.jsonPath()
                        .getString("sessionId"));
    }

    @Test(
            description = "[API-LOGIN-002] Invalid password",
            groups = {"api",}
    )
    public void verifyInvalidLogin() {

        String wrongAuth =
                "Basic " +
                        Base64.getEncoder()
                                .encodeToString("admin:WrongPass".getBytes());

        Response response =
                RestAssured
                        .given()
                        .header("Authorization", wrongAuth)
                        .when()
                        .get("/ws/rest/v1/session");

        Assert.assertTrue(
                response.statusCode() == 401 ||
                        !response.jsonPath().getBoolean("authenticated")
        );
    }

    @Test(
            description = "[API-LOGIN-003] No auth header",
            groups = {"api"}
    )
    public void verifyLoginWithoutAuth() {

        Response response =
                RestAssured
                        .given()
                        .when()
                        .get("/ws/rest/v1/session");

        Assert.assertTrue(
                response.statusCode() == 401 ||
                        !response.jsonPath().getBoolean("authenticated")
        );
    }
}