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

    // Product Management

   @Transactional
    public ProductResponseDTO createProduct(
            String tenantName,ProductRequestDTO productRequestDTO){

        // I need to get Tenant
        Tenant tenant = tenantRepository.findByName(tenantName).orElseThrow( ()->
                new ResourceNotFoundException("Tenant with Name : " + tenantName + " Don't exist")
        );
        // check whether it is duplicate or not
        Product product = productMapper.mapToEntity(productRequestDTO,tenant);
        tenant.addProduct(product);
        product = productRepository.save(product);
        return productMapper.mapToResponseDTO(product);
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(Long productId, String tenantName){

        Product product = productRepository.findByIdAndTenantName(productId,tenantName).orElseThrow(
                ()-> new ResourceNotFoundException(
                        "Product with id " + productId +
                                " does not exist under tenant " + tenantName
                )
        );
        return productMapper.mapToResponseDTO(product);
    }

    @Transactional
    public ProductResponseDTO updateProduct(Long productId, String tenantName,
                                            ProductRequestDTO productRequestDTO){
        Product product = productRepository.findByIdAndTenantName(productId,tenantName).orElseThrow(
                ()-> new ResourceNotFoundException(
                        "Product with id " + productId +
                                " does not exist under tenant " + tenantName
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
    public void deleteProduct(Long productId, String tenantName){
        Product product = productRepository.findByIdAndTenantName(productId,tenantName).orElseThrow(
                ()-> new ResourceNotFoundException(
                        "Product with id " + productId +
                                " does not exist under tenant " + tenantName
                )
        );
        Tenant tenant = product.getTenant();

        if (tenant != null) {
            tenant.getProducts().remove(product);
        }
        product.setIsDeleted(true);

    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getProductsByTenant(String tenantName, Pageable pageable) {

        tenantRepository.findByName(tenantName).orElseThrow(()->new ResourceNotFoundException(
                "Tenant with Name : " + tenantName + "don't exist"
        ));

        pageableValidator.validate(pageable);

        return productRepository.
                findByTenantName(tenantName,pageable).
                map(product -> productMapper.mapToResponseDTO(product));
    }


    // product Discovery

    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getProductsByCategory
            ( String category, Pageable pageable)
    {
        pageableValidator.validate(pageable);
        return productRepository.
                findByCategory(category,pageable).
                map(product -> productMapper.mapToResponseDTO(product));
    }


    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> searchProductsByName( String name, Pageable pageable) {
        pageableValidator.validate(pageable);
        return productRepository.
                findByName(name,pageable).
                map(product -> productMapper.mapToResponseDTO(product));
    }

    @Transactional(readOnly = true)
    public  Page<ProductResponseDTO> getProducts(Pageable pageable){
        pageableValidator.validate(pageable);
        return productRepository.
                findAllActiveProducts(pageable).
                map(product -> productMapper.mapToResponseDTO(product));
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> searchProductsByCategoryAndName(
             String category, String name,Pageable pageable) {
        pageableValidator.validate(pageable);
        return productRepository.
                findByCategoryAndName(category, name,pageable).
                map(product -> productMapper.mapToResponseDTO(product));
    }



}
