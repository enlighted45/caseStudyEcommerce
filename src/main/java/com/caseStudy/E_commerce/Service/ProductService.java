package com.caseStudy.E_commerce.Service;

import com.caseStudy.E_commerce.DTO.Product.ProductRequestDTO;
import com.caseStudy.E_commerce.DTO.Product.ProductResponseDTO;
import com.caseStudy.E_commerce.Entity.Product;
import com.caseStudy.E_commerce.Entity.Tenant;
import com.caseStudy.E_commerce.ExceptionHandler.ResourceNotFoundException;
import com.caseStudy.E_commerce.Mapper.ProductMapper;
import com.caseStudy.E_commerce.PageValidation.PageableValidator;
import com.caseStudy.E_commerce.Repository.ProductRepository;
import com.caseStudy.E_commerce.Repository.TenantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private ProductRepository productRepository;
    private TenantRepository tenantRepository;
    private ProductMapper productMapper;
    private PageableValidator pageableValidator;
    public ProductService(ProductRepository productRepository,
                          TenantRepository tenantRepository ,
                          ProductMapper productMapper, PageableValidator pageableValidator
    ){
        this.productRepository=productRepository;
        this.tenantRepository=tenantRepository;
        this.productMapper = productMapper;
        this.pageableValidator=pageableValidator;
    }

   @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO,Long tenantId){

        // I need to get Tenant
        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow( ()->
                new ResourceNotFoundException("Tenant with tenantId : " + tenantId + " Don't exist")
        );
        // check whether it is duplicate or not
        Product product = productMapper.mapToEntity(productRequestDTO,tenant);
        tenant.addProduct(product);
        product = productRepository.save(product);
        return productMapper.mapToResponseDTO(product);
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(Long productId, Long tenantId){

        Product product = productRepository.findByIdAndTenantId(productId,tenantId).orElseThrow(
                ()-> new ResourceNotFoundException(
                        "Product with id " + productId +
                                " does not exist under tenant " + tenantId
                )
        );
        return productMapper.mapToResponseDTO(product);
    }

    @Transactional
    public ProductResponseDTO updateProduct(Long productId, Long tenantId,
                                            ProductRequestDTO productRequestDTO){
        Product product = productRepository.findByIdAndTenantId(productId,tenantId).orElseThrow(
                ()-> new ResourceNotFoundException(
                        "Product with id " + productId +
                                " does not exist under tenant " + tenantId
                )
        );
        // check whether it is duplicate or not
        product.setName(productRequestDTO.getName());
        product.setPrice(productRequestDTO.getPrice());
        product.setQuantity(productRequestDTO.getQuantity());
        product.setCategory(productRequestDTO.getCategory());
        product.setDescription(productRequestDTO.getDescription());
        return productMapper.mapToResponseDTO(product);
    }

    @Transactional
    public void deleteProduct(Long productId, Long tenantId){
        Product product = productRepository.findByIdAndTenantId(productId,tenantId).orElseThrow(
                ()-> new ResourceNotFoundException(
                        "Product with id " + productId +
                                " does not exist under tenant " + tenantId
                )
        );
        Tenant tenant = product.getTenant();

        if (tenant != null) {
            tenant.getProducts().remove(product);
        }
        productRepository.delete(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getProductsByTenant(Long tenantId, Pageable pageable) {

        tenantRepository.findById(tenantId).orElseThrow(()->new ResourceNotFoundException(
                "Tenant with Id : " + tenantId + "don't exist"
        ));

        pageableValidator.validate(pageable);

        return productRepository.
                findByTenantId(tenantId,pageable).
                map(product -> productMapper.mapToResponseDTO(product));
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getProductsByCategory(Long tenantId, String category, Pageable pageable)
    {
        tenantRepository.findById(tenantId).orElseThrow(()->new ResourceNotFoundException(
                "Tenant with Id : " + tenantId + "don't exist"
        ));
        pageableValidator.validate(pageable);
        return productRepository.
                findByTenantIdAndCategory(tenantId,category,pageable).
                map(product -> productMapper.mapToResponseDTO(product));
    }
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> searchProductsByName(Long tenantId, String name, Pageable pageable) {
        tenantRepository.findById(tenantId).orElseThrow(()->new ResourceNotFoundException(
                "Tenant with Id : " + tenantId + "don't exist"
        ));
        pageableValidator.validate(pageable);
        return productRepository.
                findByTenantIdAndName(tenantId,name,pageable).
                map(product -> productMapper.mapToResponseDTO(product));
    }
}
