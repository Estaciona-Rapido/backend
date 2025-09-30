package org.estaciona.rapido.Scenario;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class ExceptionResourceTest {
    @Test
    void testExceptionEndpoint() {
        //POST method:
        given()
            .when()
            .body("{\"name\":\"Ano Novo 2026\",\"capacity\": 1, \"periodString\": \"[2025-12-31 00:00, 2026-01-02 00:00)\"}")
            .contentType("application/json")
            .post("/exception")
            .then()
            
            .statusCode(204);
        
        // GET method:
        given()
          .when().get("/exception")
          .then()
             .statusCode(200);
        }

}