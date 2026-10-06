package com.caseStudy.E_commerce.Entity;


import com.caseStudy.E_commerce.Enum.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "orders")
public class Order {

    //id	Long	Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    //totalQuantity	Integer	Total quantity of all products in the order
    @Column(
            nullable = false
    )
    Integer totalQuantity;
    //totalAmount	BigDecimal	Total price of the order
    @Column(
            precision=15,
            scale = 2,
            nullable = false
    )
    BigDecimal totalAmount;
    //user	User	User who placed the order
    @ManyToOne(fetch = FetchType.LAZY)
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

    @Enumerated(EnumType.STRING)
    OrderStatus status;
    LocalDateTime createdAt;

}
