package br.com.production.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "PRODUCTS")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, unique = true)
    public String code;

    @Column(nullable = false)
    public String name;

    @Column(name = "sales_value", nullable = false)
    public BigDecimal salesValue;

    public Product() {}

    public Product(String code, String name, BigDecimal salesValue) {
        this.code = code;
        this.name = name;
        this.salesValue = salesValue;
    }
}