package com.caseStudy.E_commerce.DTO.Product;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class ProductResponseDTO {


    //ProductResponseDTO
    //Field	Type
    //id	Long
    private long id;
    //name	String
    private String name;
    //price	BigDecimal
    private BigDecimal price;
    //quantity	Integer
    private Integer quantity;
    //category	String
    private String category;
    //description	String
    private String description;
    //tenantId	Long
    private Long tenantId;
}
