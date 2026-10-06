package com.caseStudy.E_commerce.Repository;


import com.caseStudy.E_commerce.Entity.Favourite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavouriteRepository
        extends JpaRepository<Favourite, Long> {

    boolean existsByUserIdAndProductId(
            Long userId,
            Long productId
    );

    Optional<Favourite> findByUserIdAndProductId(
            Long userId,
            Long productId
    );

    @EntityGraph(attributePaths = "product")
    Page<Favourite> findByUserId(
            Long userId,
            Pageable pageable
    );

}
