package com.caseStudy.E_commerce.Repository;


import com.caseStudy.E_commerce.Entity.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    @PersistenceContext
    private EntityManager entityManager;
    // persist
    public void saveOrder(Order order){
        entityManager.persist(order);
    }
    // findByID
    public Order findOrderId(long id){
        return entityManager.find(Order.class,id);
    }
    // REMOVAL
    public void deleteOrderById(long id){
        Order order = findOrderId(id);
        if(order==null){
            // throw resource not found
        }
        entityManager.remove(order);
    }
}
