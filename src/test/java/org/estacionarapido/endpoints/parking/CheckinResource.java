package org.estacionarapido.endpoints.parking;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest

public class CheckinResource {
    // tamanho da placa, validade da placa, 
    @ParameterizedTest
    @CsvSource({
        "MER2E25, 1, 201", // success - mercosul
        "BRA2025, 1, 201", // success - traditional
        "M3R2025, 1, 400", // error - wrong plate format 
        //TODO: "BRA2E25, 1, 400", // error - already in there
        "``, 1, 400", // error - no plate
        "BRA2026, 42, 404", // error - no price model id avaliable
    })
    void testRegisterVehicle(final String plate, final long priceModelId, final int httpCode)
    {
        given()
          .when()
          .body(String.format("{\"plate\":\"%s\",\"priceModelId\":%d}", plate, priceModelId))
          .contentType("application/json")
          .post("/parking/register")
          .then()
             .statusCode(httpCode);
    }
}
