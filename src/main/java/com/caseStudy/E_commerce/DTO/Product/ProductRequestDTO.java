package com.caseStudy.E_commerce.DTO.Product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {

    //name	String
    @NotBlank(message = "Product Name shouldn't be blank or null or empty")
    private String name;
    //    price	BigDecimal
    @NotNull(message = "price cannot be null")
    @Min(value = 1, message = "Price should be greater than 1 or equal to zero")
    private BigDecimal price;
    //    quantity	Integer
    @Min(value = 1, message = "quantity should be greater than 1 or equal to zero")
    private Integer quantity;
    //    category	String
    @NotBlank(message = "Category Name shouldn't be blank or null or empty")
    private String category;
    //    description	String
    @NotBlank(message = "Category Name shouldn't be blank or null or empty")
    private String description;

}
