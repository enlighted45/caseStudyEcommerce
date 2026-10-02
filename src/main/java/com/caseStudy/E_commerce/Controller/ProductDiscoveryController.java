package com.caseStudy.E_commerce.Controller;

import com.caseStudy.E_commerce.DTO.Product.ProductResponseDTO;
import com.caseStudy.E_commerce.Service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductDiscoveryController {

    private ProductService productService;

    public ProductDiscoveryController(ProductService productService){
        this.productService=productService;
    }
    // now what to do
    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getProducts(Pageable pageable){
        Page<ProductResponseDTO> pageResponse =
                productService.getProducts(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(pageResponse);
    }


    @GetMapping("/category/{category}")
    public ResponseEntity<Page<ProductResponseDTO>> getProductsByCategory(
            @PathVariable("category") String category, Pageable pageable){
        Page<ProductResponseDTO> pageResponse = productService.
                getProductsByCategory(category, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(pageResponse);
    }
    // now

    @GetMapping("/name/{name}")
    public ResponseEntity<Page<ProductResponseDTO>> searchProductsByName(
            @PathVariable("name") String name,
            Pageable pageable){
        Page<ProductResponseDTO> pageResponse = productService.
                searchProductsByName(name, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(pageResponse);
    }


    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponseDTO>> searchProductsByCategoryAndName(
            @RequestParam String name, @RequestParam String category,
            Pageable pageable){
        Page<ProductResponseDTO> pageResponse = productService.
                searchProductsByCategoryAndName(category, name, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(pageResponse);
    }


}
