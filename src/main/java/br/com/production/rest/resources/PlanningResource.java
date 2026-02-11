package br.com.production.rest.resources;

import br.com.production.rest.dto.ProductionPlanDTO;
import br.com.production.service.ProductionPlanningService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/api/planning")
public class PlanningResource {

    @Inject
    ProductionPlanningService planningService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductionPlanDTO> getPlan() {
        return planningService.calculateBestProductionPlan();
    }
}