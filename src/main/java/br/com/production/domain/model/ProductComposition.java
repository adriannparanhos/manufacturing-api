package br.com.production.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "PRODUCT_COMPOSITIONS")
public class ProductComposition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    public Product product;

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    public RawMaterial rawMaterial;

    @Column(name = "quantity_required", nullable = false)
    public BigDecimal quantityRequired;

    public ProductComposition() {}
}