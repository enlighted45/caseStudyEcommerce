package com.caseStudy.E_commerce.Repository;


import com.caseStudy.E_commerce.Entity.OrderItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemRepository {


    @PersistenceContext
    private EntityManager entityManager;

    // persist
    public void saveOrderItem(OrderItem orderItem){
        entityManager.persist(orderItem);
    }
    // findById
    public OrderItem findById(long id){
        return entityManager.find(OrderItem.class,id);
    }
    // remove
    public void deleteOrderItem(long id){
        OrderItem orderItem = findById(id);
        if(orderItem!=null){
            entityManager.remove(orderItem);
        }
        else{
            // resource not found
        }
    }

}
