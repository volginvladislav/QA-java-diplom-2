package steps;

import config.Config;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RestSteps {
    public RequestSpecification getDefaultRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(Config.getBaseUri())
                .setContentType(ContentType.JSON)
                .build();
    }
}
