
package utils.api;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import utils.ConfigReader;

public class BaseApiTest {

    @BeforeClass
    public void setupApi() {

        RestAssured.baseURI =
                ConfigReader.get("api.base.url");
        RestAssured.basePath = "/ws/rest/v1";

    }
}