package com.caseStudy.E_commerce.Mapper;

import com.caseStudy.E_commerce.DTO.tenant.TenantRequestDTO;
import com.caseStudy.E_commerce.DTO.tenant.TenantResponseDTO;
import com.caseStudy.E_commerce.Entity.Tenant;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TenantMapper {

    public Tenant mapToEntity(TenantRequestDTO tenantRequestDTO){
        Tenant tenant = new Tenant();
        tenant.setName(tenantRequestDTO.getName());
        tenant.setCreatedAt(LocalDateTime.now());
        tenant.setUpdatedAt(LocalDateTime.now());
        return tenant;
    }

    public TenantResponseDTO maptoResponseDTO(Tenant tenant){
        TenantResponseDTO tenantResponseDTO = new TenantResponseDTO();
        tenantResponseDTO.setId(tenant.getId());
        tenantResponseDTO.setName(tenant.getName());
        tenantResponseDTO.setCreatedAt(tenant.getCreatedAt());
        tenantResponseDTO.setUpdatedAt(tenant.getUpdatedAt());
        return tenantResponseDTO;
    }

}
