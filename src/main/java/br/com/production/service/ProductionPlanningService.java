package br.com.production.service;

import br.com.production.domain.model.Product;
import br.com.production.domain.model.ProductComposition;
import br.com.production.domain.model.RawMaterial;
import br.com.production.domain.repository.ProductCompositionRepository;
import br.com.production.domain.repository.ProductRepository;
import br.com.production.domain.repository.RawMaterialRepository;
import br.com.production.rest.dto.ProductionPlanDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ProductionPlanningService {

    @Inject
    ProductRepository productRepository;

    @Inject
    RawMaterialRepository materialRepository;

    @Inject
    ProductCompositionRepository compositionRepository;

    public List<ProductionPlanDTO> calculateBestProductionPlan() {
        List<RawMaterial> materials = materialRepository.listAll();
        Map<Long, BigDecimal> tempStock = new HashMap<>();

        for (RawMaterial rm : materials) {
            tempStock.put(rm.id, rm.stockQuantity);
        }

        List<Product> products = productRepository.listAll();
        products.sort((p1, p2) -> p2.salesValue.compareTo(p1.salesValue));

        List<ProductionPlanDTO> plan = new ArrayList<>();

        for (Product product : products) {
            List<ProductComposition> recipe = compositionRepository.findByProduct(product.id);

            if (recipe.isEmpty()) continue;

            int possibleQuantity = 0;

            while (true) {
                boolean canProduceOne = true;

                for (ProductComposition item : recipe) {
                    BigDecimal currentStock = tempStock.getOrDefault(item.rawMaterial.id, BigDecimal.ZERO);
                    if (currentStock.compareTo(item.quantityRequired) < 0) {
                        canProduceOne = false;
                        break;
                    }
                }

                if (canProduceOne) {
                    for (ProductComposition item : recipe) {
                        BigDecimal currentStock = tempStock.get(item.rawMaterial.id);
                        tempStock.put(item.rawMaterial.id, currentStock.subtract(item.quantityRequired));
                    }
                    possibleQuantity++;
                } else {
                    break;
                }
            }

            if (possibleQuantity > 0) {
                BigDecimal totalValue = product.salesValue.multiply(new BigDecimal(possibleQuantity));
                plan.add(new ProductionPlanDTO(
                        product.name,
                        possibleQuantity,
                        product.salesValue,
                        totalValue
                ));
            }
        }

        return plan;
    }
}
