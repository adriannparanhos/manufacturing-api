package br.com.production.rest.dto;

import java.math.BigDecimal;

public record ProductCompositionDTO(
        Long materialId,
        String materialName,
        BigDecimal quantity
) {
}
