package com.caseStudy.E_commerce.Repository;

import com.caseStudy.E_commerce.Entity.Product;
import com.caseStudy.E_commerce.Entity.Tenant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {


    @Query(
            """
            SELECT p
            FROM Product p
            JOIN p.tenant t
            WHERE t.id = ?1
            """
    )
    Page<Product> findByTenantId(Long tenantId, Pageable pageable);

    @Query(
            """
           SELECT p
           FROM Product p
           JOIN p.tenant t
           WHERE t.id = ?1
           AND p.category = ?2   
           """
    )
    Page<Product> findByTenantIdAndCategory(Long tenantId, String category, Pageable pageable);



    @Query(
            """
            SELECT p
            FROM Product p
            JOIN p.tenant t
            WHERE t.id = ?1
            AND p.name LIKE CONCAT('%', ?2, '%') 
           """
    )
    Page<Product> findByTenantIdAndName(Long tenantId, String name, Pageable pageable);


    @Query(
            """
            SELECT p
            FROM Product p
            JOIN p.tenant t
            WHERE t.id = ?1
            AND p.category = ?2
            AND p.name LIKE CONCAT('%', ?3, '%')
           """
    )
    Page <Product> findByTenantIdAndCategoryAndName(
            Long tenantId,
            String category,
            String name,
            Pageable pageable
    );

    @Query(
            """
            SELECT p
            FROM Product p
            JOIN p.tenant t
            WHERE p.id = ?1
            AND t.id = ?2        
           """
    )
    Optional<Product> findByIdAndTenantId(Long productId, Long tenantId);




}
