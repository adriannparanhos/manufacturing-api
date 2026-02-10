package br.com.production.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "RAW_MATERIALS")
public class RawMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, unique = true)
    public String code;

    @Column(nullable = false)
    public String name;

    @Column(name = "stock_quantity", nullable = false)
    public BigDecimal stockQuantity;

    public RawMaterial() {}
}