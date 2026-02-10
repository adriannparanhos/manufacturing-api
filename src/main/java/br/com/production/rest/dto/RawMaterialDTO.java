package br.com.production.rest.dto;

import java.math.BigDecimal;

public record RawMaterialDTO(
        Long id,
        String name,
        BigDecimal stockQuantity
) {
}
