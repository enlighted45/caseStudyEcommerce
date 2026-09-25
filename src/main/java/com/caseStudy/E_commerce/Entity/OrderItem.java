package com.caseStudy.E_commerce.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    //Field	Type	Purpose
    //id	Long	Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    //Order	The order this item belongs to
    @ManyToOne
    @JoinColumn(
        name = "OrderId",
        nullable = false
    )
    Order order;
    //product	Product	Product being purchased
    // what relation should it follow
    @ManyToOne
    @JoinColumn(
            name = "ProductId",
            nullable = false
    )
    Product product;
    //quantity	Integer	Number of units purchased
    @Column(
      name = "Quantity",
      nullable = false
    )
    long quantity;
}
