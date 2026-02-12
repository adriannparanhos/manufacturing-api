package br.com.production.service.mapper;

import br.com.production.domain.model.*;
import br.com.production.rest.dto.*;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductionMapper {

    public ProductDTO toProductDTO(Product product, List<ProductComposition> compositions) {
        List<ProductCompositionDTO> compDtos = compositions.stream()
                .map(c -> new ProductCompositionDTO(
                        c.getRawMaterial().getId(),
                        c.getRawMaterial().getName(),
                        c.getQuantityRequired()))
                .collect(Collectors.toList());

        return new ProductDTO(product.getId(), product.getName(), product.getSalesValue(), compDtos);
    }

    public RawMaterialDTO toRawMaterialDTO(RawMaterial rm) {
        return new RawMaterialDTO(rm.getId(), rm.getName(), rm.getStockQuantity());
    }
}