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
            name = "Password",
            length = 150,
            insertable = true,
            updatable = true
    )
    String password;

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
            length = 10,
            insertable = true,
            updatable = true
    )
    Role role;

    @ManyToOne
    @JoinColumn(
            name = "TenantId",
            nullable = true
    )
    Tenant tenant;

    @OneToMany(mappedBy = "user")
    List<Order> orderList = new ArrayList<>();

    LocalDateTime createdAt;

    LocalDateTime updatedAt;


}
