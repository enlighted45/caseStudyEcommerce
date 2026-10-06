package com.caseStudy.E_commerce.Controller;


import com.caseStudy.E_commerce.Authentication.AuthenticationService.TenantAuthorizationService;
import com.caseStudy.E_commerce.DTO.Product.ProductRequestDTO;
import com.caseStudy.E_commerce.DTO.Product.ProductResponseDTO;
import com.caseStudy.E_commerce.Service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{tenantName}/products")

@PreAuthorize(
        "@tenantAuthorizationService.isCurrentUserTenant(#tenantName)"
)
@SecurityRequirement(name = "bearerAuth")

public class TenantProductManagementController {

    private ProductService productService;
    private final TenantAuthorizationService tenantAuthorizationService;

    public TenantProductManagementController(ProductService productService,
                                             TenantAuthorizationService tenantAuthorizationService){
        this.productService=productService;
        this.tenantAuthorizationService=tenantAuthorizationService;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(
            @PathVariable("tenantName") String tenantName,
            @RequestBody @Valid ProductRequestDTO productRequestDTO
            )
    {

        ProductResponseDTO productResponseDTO = productService.
                createProduct(tenantName,productRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponseDTO);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDTO> getProductById(
            @PathVariable("productId") Long productId,
            @PathVariable("tenantName") String tenantName
    )
    {
        ProductResponseDTO productResponseDTO = productService.
                getProductById(productId,tenantName);
        return ResponseEntity.status(HttpStatus.OK).
                body(productResponseDTO);
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getProductsByTenant
            (@PathVariable("tenantName") String tenantName,@ParameterObject Pageable pageable){

        Page<ProductResponseDTO> pageResponse = productService.
                getProductsByTenant(tenantName,pageable);
        return ResponseEntity.status(HttpStatus.OK).body(pageResponse);
    }
    // updateProduct

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable("productId") Long productId,
            @PathVariable("tenantName") String tenantName,
            @RequestBody @Valid ProductRequestDTO productRequestDTO
    )
    {
        ProductResponseDTO productResponseDTO = productService.
                updateProduct(productId,tenantName,productRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).
                body(productResponseDTO);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct( @PathVariable("productId") Long productId,
                                               @PathVariable("tenantName") String tenantName){
        productService.deleteProduct(productId,tenantName);
        return ResponseEntity.
                status(HttpStatus.NO_CONTENT).build();
    }











}
