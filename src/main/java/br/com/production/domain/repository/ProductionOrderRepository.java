package br.com.production.domain.repository;

import br.com.production.domain.model.ProductionOrder;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductionOrderRepository implements PanacheRepository<ProductionOrder> {
}