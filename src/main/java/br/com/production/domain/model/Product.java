package br.com.production.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "PRODUCTS")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(name = "sales_value", nullable = false)
    private BigDecimal salesValue;

    public Product() {}

    public Product(String name, BigDecimal salesValue) {
        this.name = Objects.requireNonNull(name, "Nome é obrigatório");
        this.salesValue = Objects.requireNonNull(salesValue, "Valor de venda é obrigatório");
        this.code = "PROD-" + System.currentTimeMillis();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getSalesValue() { return salesValue; }
    public void setSalesValue(BigDecimal salesValue) { this.salesValue = salesValue; }
}