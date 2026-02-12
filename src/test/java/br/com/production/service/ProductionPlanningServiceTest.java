package br.com.production.service;

import br.com.production.domain.model.Product;
import br.com.production.domain.model.ProductComposition;
import br.com.production.domain.model.RawMaterial;
import br.com.production.domain.repository.ProductCompositionRepository;
import br.com.production.domain.repository.ProductRepository;
import br.com.production.domain.repository.ProductionOrderRepository;
import br.com.production.domain.repository.RawMaterialRepository;
import br.com.production.rest.dto.ProductionPlanDTO;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;

@QuarkusTest
public class ProductionPlanningServiceTest {

    @Inject
    ProductionPlanningService service;

    @InjectMock
    ProductRepository productRepository;

    @InjectMock
    ProductCompositionRepository compositionRepository;

    @InjectMock
    ProductionOrderRepository orderRepository;

    @InjectMock
    RawMaterialRepository materialRepository;

    @Test
    public void deveCalcularPlanejamentoComSucesso() {
        Long productId = 1L;
        Integer qtd = 5;

        Product product = new Product("Bolo", new BigDecimal("20.00"));
        RawMaterial farinha = new RawMaterial("Farinha", new BigDecimal("1000"));
        ProductComposition composicao = new ProductComposition(product, farinha, new BigDecimal("100"));

        Mockito.when(productRepository.findById(productId)).thenReturn(product);
        Mockito.when(compositionRepository.findByProduct(productId)).thenReturn(List.of(composicao));

        ProductionPlanDTO resultado = service.calculateSpecificPlan(productId, qtd);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("Bolo", resultado.productName());
        Assertions.assertEquals(new BigDecimal("100.00"), resultado.totalValue());
    }

    @Test
    public void deveBloquearQuandoEstoqueInsuficiente() {
        Long productId = 1L;
        Integer qtd = 20;

        Product product = new Product("Bolo", new BigDecimal("20.00"));
        RawMaterial farinha = new RawMaterial("Farinha", new BigDecimal("1000"));
        ProductComposition composicao = new ProductComposition(product, farinha, new BigDecimal("100"));

        Mockito.when(productRepository.findById(productId)).thenReturn(product);
        Mockito.when(compositionRepository.findByProduct(productId)).thenReturn(List.of(composicao));

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            service.calculateSpecificPlan(productId, qtd);
        });
    }
}