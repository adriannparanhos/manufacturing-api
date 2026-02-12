package br.com.production.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "PRODUCTION_ORDERS")
public class ProductionOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "total_cost", nullable = false)
    private BigDecimal totalCost;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public ProductionOrder() {}

    public ProductionOrder(Product product, Integer quantity, BigDecimal totalCost) {
        this.product = product;
        this.quantity = quantity;
        this.totalCost = totalCost;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Product getProduct() { return product; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getTotalCost() { return totalCost; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}