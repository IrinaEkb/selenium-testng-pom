package utils.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import utils.api.AuthHelper;

public class RequestSpecs {

    public static RequestSpecification adminRequest() {

        return new RequestSpecBuilder()
                .addHeader("Authorization", AuthHelper.defaultAdminAuth())
                .setContentType(ContentType.JSON)
                .build();
    }
}
