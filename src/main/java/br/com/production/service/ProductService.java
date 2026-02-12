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
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductService {

    private final ProductRepository productRepository;
    private final RawMaterialRepository materialRepository;
    private final ProductCompositionRepository compositionRepository;

    public ProductService(ProductRepository productRepository,
                          RawMaterialRepository materialRepository,
                          ProductCompositionRepository compositionRepository) {
        this.productRepository = productRepository;
        this.materialRepository = materialRepository;
        this.compositionRepository = compositionRepository;
    }

    @Transactional
    public List<ProductDTO> listAll() {
        return productRepository.listAll().stream().map(product -> {
            List<ProductComposition> compositions = compositionRepository.findByProduct(product.getId());

            List<ProductCompositionDTO> compositionDTOS = compositions.stream()
                    .map(c -> new ProductCompositionDTO(
                            c.getRawMaterial().getId(),
                            c.getRawMaterial().getName(),
                            c.getQuantityRequired()
                    ))
                    .collect(Collectors.toList());

            return new ProductDTO(
                    product.getId(),
                    product.getName(),
                    product.getSalesValue(),
                    compositionDTOS
            );
        }).collect(Collectors.toList());
    }

    @Transactional
    public ProductDTO create(ProductDTO dto) {
        Product product = new Product(dto.name(), dto.salesValue());

        productRepository.persist(product);

        if (dto.compositions() != null) {
            for (ProductCompositionDTO compDto : dto.compositions()) {
                RawMaterial material = materialRepository.findById(compDto.materialId());
                if (material == null) {
                    throw new IllegalArgumentException("Materia prima nao encontrada: " + compDto.materialId());
                }

                ProductComposition composition = new ProductComposition(
                        product,
                        material,
                        compDto.quantity()
                );

                compositionRepository.persist(composition);
            }
        }

        return new ProductDTO(product.getId(), product.getName(), product.getSalesValue(), dto.compositions());
    }

    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id);
        if (product == null) {
            throw new NotFoundException("Produto não encontrado");
        }

        compositionRepository.delete("product.id", id);

        productRepository.delete(product);
    }
}