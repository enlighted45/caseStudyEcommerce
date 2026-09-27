package com.caseStudy.E_commerce.Repository;


import com.caseStudy.E_commerce.Entity.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    @Query(
            """
            Select o
            FROM Order o
            JOIN o.user  u
            WHERE u.id = ?1        
            """

    )
    Page<Order> findByUserId(Long userId, Pageable pageable);


}
