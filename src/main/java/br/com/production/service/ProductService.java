package br.com.production.service;

import br.com.production.domain.model.Product;
import br.com.production.domain.model.ProductComposition;
import br.com.production.domain.model.RawMaterial;
import br.com.production.domain.repository.ProductCompositionRepository;
import br.com.production.domain.repository.ProductRepository;
import br.com.production.domain.repository.RawMaterialRepository;
import br.com.production.rest.dto.ProductCompositionDTO;
import br.com.production.rest.dto.ProductDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepository productRepository;

    @Inject
    RawMaterialRepository materialRepository;

    @Inject
    ProductCompositionRepository compositionRepository;

    public List<ProductDTO> listAll() {
        return productRepository.listAll().stream().map(product -> {
            List<ProductComposition> compositions = compositionRepository.findByProduct(product.id);

            List<ProductCompositionDTO> compositionDTOS = compositions.stream()
                    .map(c -> new ProductCompositionDTO(c.rawMaterial.id, c.rawMaterial.name, c.quantityRequired))
                    .collect(Collectors.toList());

            return new ProductDTO(product.id, product.name, product.salesValue, compositionDTOS);
        }).collect(Collectors.toList());
    }

    @Transactional
    public ProductDTO create(ProductDTO dto) {
        Product product = new Product();
        product.name = dto.name();
        product.salesValue = dto.salesValue();
        product.code = "PROD-" + System.currentTimeMillis();
        productRepository.persist(product);

        if (dto.compositions() != null) {
            for (ProductCompositionDTO compDto : dto.compositions()) {
                RawMaterial material = materialRepository.findById(compDto.materialId());
                if (material == null) {
                    throw new IllegalArgumentException("Materia prima nao encontrada: " + compDto.materialId());
                }

                ProductComposition composition = new ProductComposition();
                composition.product = product;
                composition.rawMaterial = material;
                composition.quantityRequired = compDto.quantity();

                compositionRepository.persist(composition);
            }
        }

        return dto;
    }

}
