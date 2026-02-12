package br.com.production.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "RAW_MATERIALS")
public class RawMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(name = "stock_quantity", nullable = false)
    private BigDecimal stockQuantity;

    public RawMaterial() {}

    public RawMaterial(String name, BigDecimal stockQuantity) {
        this.name = Objects.requireNonNull(name);
        this.stockQuantity = stockQuantity != null ? stockQuantity : BigDecimal.ZERO;
        this.code = "RM-" + System.currentTimeMillis();
    }

    public boolean hasSufficientStock(BigDecimal amountRequired) {
        if (amountRequired == null || amountRequired.compareTo(BigDecimal.ZERO) <= 0) {
            return true;
        }
        return this.stockQuantity != null && this.stockQuantity.compareTo(amountRequired) >= 0;
    }

    public void updateDetails(String name, BigDecimal stockQuantity) {
        this.name = name;
        this.stockQuantity = stockQuantity;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(BigDecimal stockQuantity) { this.stockQuantity = stockQuantity; }
}