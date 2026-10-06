package com.caseStudy.E_commerce.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Entity
@Table(
        name = "favourites",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"user_id", "product_id"}
                )
        }
)
@Data
public class Favourite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;


}
