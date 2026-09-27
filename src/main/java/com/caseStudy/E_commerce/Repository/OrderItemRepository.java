package com.caseStudy.E_commerce.Repository;


import com.caseStudy.E_commerce.Entity.Order;
import com.caseStudy.E_commerce.Entity.OrderItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {

    // it's saying I have to write a method where
    // it find order-item by foreign key orderId

    @Query(
            """
            Select o
            From OrderItem o
            Join o.order ord
            Where ord.id = ?1     
            """
    )
    //findByOrderId(Long orderId)
    List<OrderItem> findByOrderId(Long id);




}
