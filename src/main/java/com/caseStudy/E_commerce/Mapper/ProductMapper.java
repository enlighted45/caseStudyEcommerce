package com.caseStudy.E_commerce.Mapper;

import com.caseStudy.E_commerce.DTO.Product.ProductRequestDTO;
import com.caseStudy.E_commerce.DTO.Product.ProductResponseDTO;
import com.caseStudy.E_commerce.Entity.Product;
import com.caseStudy.E_commerce.Entity.Tenant;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product mapToEntity(ProductRequestDTO productRequestDTO, Tenant tenant){

        Product productEntity = new Product();
        productEntity.setName(productRequestDTO.getName());
        productEntity.setCategory(productRequestDTO.getCategory());
        productEntity.setDescription(productRequestDTO.getDescription());
        productEntity.setPrice(productRequestDTO.getPrice());
        productEntity.setQuantity(productRequestDTO.getQuantity());
        productEntity.setTenant(tenant);
        productEntity.setIsDeleted(false);
        return productEntity;

    }
    public ProductResponseDTO mapToResponseDTO(Product product){
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setCategory(product.getCategory());
        productResponseDTO.setId(product.getId());
        productResponseDTO.setDescription(product.getDescription());
        productResponseDTO.setQuantity(product.getQuantity());
        productResponseDTO.setName(product.getName());
        productResponseDTO.setPrice(product.getPrice());
        // think of lazy transactional
        productResponseDTO.setTenantId(product.getTenant().getId());
        return productResponseDTO;
    }

}
