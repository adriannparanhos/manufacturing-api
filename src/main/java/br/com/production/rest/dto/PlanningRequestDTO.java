package br.com.production.rest.dto;

public record PlanningRequestDTO(
        Long productId,
        Integer quantity
) {
}
