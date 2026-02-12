package br.com.production.rest.resources;

import br.com.production.rest.dto.PlanningRequestDTO;
import br.com.production.rest.dto.ProductionPlanDTO;
import br.com.production.service.ProductionPlanningService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/planning")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PlanningResource {

    private final ProductionPlanningService planningService;

    public PlanningResource(ProductionPlanningService planningService) {
        this.planningService = planningService;
    }

    @GET
    public List<ProductionPlanDTO> getGeneralPlan() {
        return planningService.calculateBestProductionPlan();
    }

    @POST
    public Response calculateSpecific(PlanningRequestDTO request) {
        if (request.productId() == null || request.quantity() == null || request.quantity() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Produto e Quantidade válida são obrigatórios").build();
        }

        try {
            ProductionPlanDTO plan = planningService.calculateSpecificPlan(request.productId(), request.quantity());
            return Response.ok(plan).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    public record ErrorResponse(String message) {}
}