package br.com.production.rest.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductDTO(
        Long id,
        String name,
        BigDecimal salesValue,
        List<ProductCompositionDTO> compositions
) {
}
