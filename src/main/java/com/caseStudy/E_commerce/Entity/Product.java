package com.caseStudy.E_commerce.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    //name        → search by product name
    @Column(
            length = 150,
            nullable = false

    )
    String name;
    //price       → order total amount
    @Column(
            nullable = false,
            precision = 15,
            scale=2
    )
    BigDecimal price;
    //quantity    → available quantity / ordering restriction
    @Column(
            nullable = false
    )
    long quantity;
    //category    → category filtering
    @Column(
            nullable = false
    )
    String category;
    // description
    @Lob
    @Column(
         nullable = false
    )
    String description;
    //tenant      → product belongs to a brand
    @ManyToOne
    @JoinColumn(
            name = "TenantId",
            nullable = false
    )
    Tenant tenant;
}
