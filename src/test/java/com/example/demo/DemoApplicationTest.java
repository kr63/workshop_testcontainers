package com.example.demo;

import io.restassured.filter.log.LogDetail;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class DemoApplicationTest extends AbstractIntegrationTest {

    @Test
    public void contextLoads() {

    }

    @Test
    public void healthy() {
        given(requestSpecification)
                .when()
                .get("/actuator/health")
                .then()
                .statusCode(200)
                .log().ifValidationFails(LogDetail.ALL);
    }
}