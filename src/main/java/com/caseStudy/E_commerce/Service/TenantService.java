package com.caseStudy.E_commerce.Service;


import com.caseStudy.E_commerce.DTO.tenant.TenantRequestDTO;
import com.caseStudy.E_commerce.DTO.tenant.TenantResponseDTO;
import com.caseStudy.E_commerce.Entity.Tenant;
import com.caseStudy.E_commerce.ExceptionHandler.DuplicateResourceException;
import com.caseStudy.E_commerce.ExceptionHandler.ResourceNotFoundException;
import com.caseStudy.E_commerce.Mapper.TenantMapper;
import com.caseStudy.E_commerce.PageValidation.TenantPageableValidator;
import com.caseStudy.E_commerce.Repository.TenantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TenantService {

    private TenantRepository tenantRepository;
    private TenantMapper tenantMapper;
    private TenantPageableValidator tenantPageableValidator;
    public TenantService(TenantRepository tenantRepository,
                         TenantMapper tenantMapper,
                         TenantPageableValidator tenantPageableValidator
    ){
        this.tenantRepository = tenantRepository;
        this.tenantMapper = tenantMapper;
        this.tenantPageableValidator = tenantPageableValidator;
    }


    @Transactional
    public TenantResponseDTO createTenant(TenantRequestDTO request) {
        boolean val = tenantRepository.existsByName(request.getName());
        if (val) throw new DuplicateResourceException("Tenant with Name : "+request.getName() + " already exist");
        Tenant tenant = tenantMapper.mapToEntity(request);
        tenant = tenantRepository.save(tenant);
        return tenantMapper.maptoResponseDTO(tenant);
    }
    @Transactional(readOnly = true)
    public TenantResponseDTO getTenantById(Long tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(
                ()-> new ResourceNotFoundException("Tenant with Id : " + tenantId + "don't exist")
        );
        return tenantMapper.maptoResponseDTO(tenant);
    }

    @Transactional(readOnly = true)
    public Page<TenantResponseDTO> getAllTenants(Pageable pageable) {
        tenantPageableValidator.validate(pageable);
        return tenantRepository.findAll(pageable).
        map(tenant -> tenantMapper.maptoResponseDTO(tenant));
    }

    // update
    @Transactional
    public TenantResponseDTO updateTenant(Long tenantId, TenantRequestDTO request) {
        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(
                ()-> new ResourceNotFoundException("Tenant with Id : " + tenantId + "don't exist")
        );
        boolean val = tenantRepository.existsByNameAndNotById(request.getName(),tenantId);
        if (val) throw new DuplicateResourceException("Tenant with Name : "+request.getName() + " already exist");
        tenant.setUpdatedAt(LocalDateTime.now());
        tenant.setName(request.getName());
        return tenantMapper.maptoResponseDTO(tenant);
    }
    // now Delete // let's assume we don't want cascade here
    @Transactional
    public void deleteTenant(Long tenantId) {
        // now what to write here
        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(
                ()-> new ResourceNotFoundException("Tenant with Id : " + tenantId + "don't exist")
        );
        tenantRepository.delete(tenant);

    }







}
