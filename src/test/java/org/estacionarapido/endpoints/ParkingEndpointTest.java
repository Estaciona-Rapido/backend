package org.estacionarapido.endpoints;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class ParkingEndpointTest {
    @Test
    void getHistory() {
        // given()
        //     .when()
        //     //.body("{\"name\":\"Ano Novo 2026\",\"capacity\": 1, \"periodString\": \"[2025-12-31 00:00, 2026-01-02 00:00)\"}")
        //     .contentType("application/json")
        //     .post("/exception?name=feriado")
        //     .then()
        //     .statusCode(204);
        
        // GET method:
        given()
          .when().get("/parking/history")
          .then()
             .statusCode(200);
        }

}