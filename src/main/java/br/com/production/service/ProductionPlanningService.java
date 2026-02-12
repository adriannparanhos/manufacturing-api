package br.com.production.service;

import br.com.production.domain.model.*;
import br.com.production.domain.repository.*;
import br.com.production.rest.dto.ProductionPlanDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.math.BigDecimal;
import java.util.*;

@ApplicationScoped
public class ProductionPlanningService {

    private final ProductRepository productRepository;
    private final RawMaterialRepository materialRepository;
    private final ProductCompositionRepository compositionRepository;
    private final ProductionOrderRepository orderRepository;

    public ProductionPlanningService(ProductRepository productRepo,
                                     RawMaterialRepository materialRepo,
                                     ProductCompositionRepository compRepo,
                                     ProductionOrderRepository orderRepo) {
        this.productRepository = productRepo;
        this.materialRepository = materialRepo;
        this.compositionRepository = compRepo;
        this.orderRepository = orderRepo;
    }

    @Transactional
    public List<ProductionPlanDTO> calculateBestProductionPlan() {
        List<RawMaterial> materials = materialRepository.listAll();
        Map<Long, BigDecimal> simulationStock = new HashMap<>();
        materials.forEach(m -> simulationStock.put(m.getId(), m.getStockQuantity()));

        List<Product> products = productRepository.listAll();
        products.sort(Comparator.comparing(Product::getSalesValue).reversed());

        List<ProductionPlanDTO> plan = new ArrayList<>();

        for (Product product : products) {
            List<ProductComposition> recipe = compositionRepository.findByProduct(product.getId());
            if (recipe.isEmpty()) continue;

            int possibleQuantity = calculateMaxPossible(recipe, simulationStock);

            if (possibleQuantity > 0) {
                deductFromSimulation(recipe, simulationStock, possibleQuantity);

                BigDecimal totalValue = product.getSalesValue().multiply(new BigDecimal(possibleQuantity));
                plan.add(new ProductionPlanDTO(
                        product.getName(),
                        possibleQuantity,
                        product.getSalesValue(),
                        totalValue
                ));
            }
        }
        return plan;
    }

    @Transactional
    public ProductionPlanDTO calculateSpecificPlan(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId);
        if (product == null) throw new NotFoundException("Produto não encontrado");

        List<ProductComposition> recipe = compositionRepository.findByProduct(productId);
        if (recipe.isEmpty()) {
            throw new IllegalArgumentException("Este produto não possui receita cadastrada.");
        }

        for (ProductComposition item : recipe) {
            BigDecimal requiredPerUnit = item.getQuantityRequired();
            BigDecimal totalRequired = requiredPerUnit.multiply(new BigDecimal(quantity));

            if (!item.getRawMaterial().hasSufficientStock(totalRequired)) {
                BigDecimal stock = item.getRawMaterial().getStockQuantity();
                throw new IllegalArgumentException(String.format(
                        "Estoque insuficiente de %s. Necessário: %s, Disponível: %s",
                        item.getRawMaterial().getName(),
                        totalRequired,
                        stock
                ));
            }
        }

        BigDecimal totalValue = product.getSalesValue().multiply(new BigDecimal(quantity));

        ProductionOrder order = new ProductionOrder(product, quantity, totalValue);
        orderRepository.persist(order);

        return new ProductionPlanDTO(
                product.getName(),
                quantity,
                product.getSalesValue(),
                totalValue
        );
    }

    private int calculateMaxPossible(List<ProductComposition> recipe, Map<Long, BigDecimal> stock) {
        int maxPossible = Integer.MAX_VALUE;

        for (ProductComposition item : recipe) {
            BigDecimal available = stock.getOrDefault(item.getRawMaterial().getId(), BigDecimal.ZERO);
            if (available.compareTo(BigDecimal.ZERO) <= 0) return 0;

            int possibleForThisItem = available.divideToIntegralValue(item.getQuantityRequired()).intValue();
            maxPossible = Math.min(maxPossible, possibleForThisItem);
        }
        return maxPossible == Integer.MAX_VALUE ? 0 : maxPossible;
    }

    private void deductFromSimulation(List<ProductComposition> recipe, Map<Long, BigDecimal> stock, int quantity) {
        for (ProductComposition item : recipe) {
            BigDecimal totalRequired = item.getQuantityRequired().multiply(new BigDecimal(quantity));
            Long matId = item.getRawMaterial().getId();

            if (stock.containsKey(matId)) {
                stock.put(matId, stock.get(matId).subtract(totalRequired));
            }
        }
    }
}