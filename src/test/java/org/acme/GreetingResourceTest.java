package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class GreetingResourceTest {

    @Test
    public void testHelloEndpoint() {
        // Clock clock = Clock.fixed(Instant.parse("2022-11-17T00:00:00.00Z"), ZoneId.of("UTC"));

        given()
          .when().get("/api/KL2222/60")
          .then()
             .statusCode(200)
             .body(is("Hello Schiphol: KL1111, distance=60"));
    }

}
