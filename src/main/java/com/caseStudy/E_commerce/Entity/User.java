package com.caseStudy.E_commerce.Entity;


import com.caseStudy.E_commerce.Enum.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(
            unique = true,
            nullable = false,
            name = "User Name",
            length = 50,
            insertable = true,
            updatable = true
    )
    String userName;

    @Column(
            unique = true,
            nullable = false,
            name = "Email",
            length = 150,
            insertable = true,
            updatable = true
    )
    String email;


    @Column(
            nullable = false,
            name = "First Name",
            length = 150,
            insertable = true,
            updatable = true
    )
    String firstName;
    @Column(
            nullable = false,
            name = "Last Name",
            length = 150,
            insertable = true,
            updatable = true
    )
    String lastName;


    // create enum here
    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            name = "Role",
            length = 10

    )
    Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "TenantId",
            nullable = true
    )
    Tenant tenant;


    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    List<Order> orders = new ArrayList<>();



    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Favourite> favourites = new ArrayList<>();



    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    String keycloakId;


    public void addOrder(Order order) {
        this.orders.add(order);
        order.setUser(this);
    }

    public void addFavourite(Favourite favourite) {
        favourites.add(favourite);
        favourite.setUser(this);
    }


    public void removeFavourite(Favourite favourite) {
        favourites.remove(favourite);
        favourite.setUser(null);
    }
}
