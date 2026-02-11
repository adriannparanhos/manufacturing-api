package br.com.production.rest.dto;

import java.math.BigDecimal;

public record ProductionPlanDTO(
        String productName,
        Integer quantityToProduce,
        BigDecimal unitValue,
        BigDecimal totalValue
) {
}
