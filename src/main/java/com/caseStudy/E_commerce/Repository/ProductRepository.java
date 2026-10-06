package com.caseStudy.E_commerce.Repository;

import com.caseStudy.E_commerce.Entity.Product;
import com.caseStudy.E_commerce.Entity.Tenant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    @EntityGraph(attributePaths = "tenant")
    @Query(
            """
            SELECT p
            FROM Product p
            JOIN p.tenant t
            WHERE t.name = ?1
            AND p.isDeleted = false
            """
    )
    Page<Product> findByTenantName(String tenantName, Pageable pageable);


    @Query(
            """
           SELECT p
           FROM Product p
           WHERE p.category = ?1 
           AND p.isDeleted = false  
           """
    )
    Page<Product> findByCategory(String category, Pageable pageable);


    @Query(
            """
            SELECT p
            FROM Product p
            WHERE p.name LIKE CONCAT('%', ?1, '%') 
            AND p.isDeleted = false
           """
    )
    Page<Product> findByName(String name, Pageable pageable);

    @Query(
            """
            SELECT p
            FROM Product p
            WHERE p.category = ?1
            AND p.name LIKE CONCAT('%', ?2, '%')
            AND p.isDeleted = false
           """
    )
    Page <Product> findByCategoryAndName(
            String category,
            String name,
            Pageable pageable
    );

    @EntityGraph(attributePaths = "tenant")
    @Query(

            """
            SELECT p
            FROM Product p
            JOIN p.tenant t
            WHERE p.id = ?1
            AND t.name = ?2
            AND p.isDeleted = false       
           """
    )
    Optional<Product> findByIdAndTenantName(Long productId, String tenantName);

    @Query("""
    SELECT p
    FROM Product p
    WHERE p.isDeleted = false
    """)
    Page<Product> findAllActiveProducts(Pageable pageable);
}
