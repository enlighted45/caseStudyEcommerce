package com.caseStudy.E_commerce.Repository;

import com.caseStudy.E_commerce.Entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    @PersistenceContext
    private EntityManager entityManager;
    //persist
    public void saveProduct(Product product){
        entityManager.persist(product);
    }
    //findById
    public Product findProductById(Long id) {
        return entityManager.find(Product.class, id);
    }
    //delete
    public void deleteProduct(Long id) {
        Product product = findProductById(id);
        if (product != null) {
            entityManager.remove(product);
        }
    }
    // search by name
    // find By category


}
