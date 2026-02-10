package br.com.production.domain.repository;

import br.com.production.domain.model.ProductComposition;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductCompositionRepository implements PanacheRepository<ProductComposition> {
}
