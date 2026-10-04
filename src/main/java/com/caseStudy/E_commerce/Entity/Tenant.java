package com.caseStudy.E_commerce.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(
            length = 150,
            unique = true,
            nullable = false
    )
    String name;
    @OneToMany(
            mappedBy = "tenant",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    List<User> users = new ArrayList<>();

    @OneToMany(
            mappedBy = "tenant" ,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
    )
    List<Product> products = new ArrayList<>();
    //createdAt
    LocalDateTime createdAt;
    //updatedAt
    LocalDateTime updatedAt;

    public void addUser(User user){
        user.setTenant(this);
        this.users.add(user);
    }
    public void addProduct(Product product){
        product.setTenant(this);
        this.products.add(product);
    }
}
