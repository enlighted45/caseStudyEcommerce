package com.caseStudy.E_commerce.Entity;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Order {

    //id	Long	Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    //totalQuantity	Integer	Total quantity of all products in the order
    @Column(
            nullable = false
    )
    long totalQuantity;
    //totalAmount	BigDecimal	Total price of the order
    @Column(
            precision=15,
            scale = 2,
            nullable = false
    )
    BigDecimal totalAmount;
    //user	User	User who placed the order
    @ManyToOne
    @JoinColumn(
            name = "UserId",
            nullable = false
    )
    User user;
    //orderItems	List<OrderItem>	Products included in the order
    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    List<OrderItem> orderItems=new ArrayList<>();

}
