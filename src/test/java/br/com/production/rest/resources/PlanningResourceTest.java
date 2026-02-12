package br.com.production.rest.resources;

import br.com.production.rest.dto.ProductionPlanDTO;
import br.com.production.service.ProductionPlanningService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class PlanningResourceTest {

    @InjectMock
    ProductionPlanningService service;

    @Test
    public void deveRetornarListaDePlanejamentoComStatus200() {
        ProductionPlanDTO planoFalso = new ProductionPlanDTO(
                "Teste Produto",
                10,
                new BigDecimal("50.00"),
                new BigDecimal("500.00")
        );

        Mockito.when(service.calculateBestProductionPlan())
                .thenReturn(List.of(planoFalso));

        given()
                .when().get("/api/planning")
                .then()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].productName", is("Teste Produto"));
    }

    @Test
    public void deveRetornarErro400SeFaltarDadosNoPost() {
        given()
                .contentType("application/json")
                .body("{}")
                .when()
                .post("/api/planning")
                .then()
                .statusCode(400);
    }
}