
package utils.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import utils.ConfigReader;

public class BaseApiTest {
    protected Response response;

    @BeforeClass
    public void setupApi() {

        RestAssured.baseURI =
                ConfigReader.get("api.base.url");
        RestAssured.basePath = "/ws/rest/v1";

    }
    @AfterMethod
    public void attachResponseToAllure(ITestResult result) {

        if (response != null) {
            System.out.println("Attaching API response from BaseApiTest");

            result.setAttribute("apiResponse", response.asPrettyString());
        }
    }
}