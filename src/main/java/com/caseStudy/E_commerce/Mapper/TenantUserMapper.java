package com.caseStudy.E_commerce.Mapper;

import com.caseStudy.E_commerce.DTO.User.TenantUserRequestDTO;
import com.caseStudy.E_commerce.DTO.User.TenantUserResponseDTO;
import com.caseStudy.E_commerce.DTO.User.UserRequestDTO;
import com.caseStudy.E_commerce.DTO.User.UserResponseDTO;
import com.caseStudy.E_commerce.Entity.Tenant;
import com.caseStudy.E_commerce.Entity.User;
import com.caseStudy.E_commerce.Enum.Role;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TenantUserMapper {

    public User mapToEntity(TenantUserRequestDTO userRequestDTO, Tenant tenant){
        User user = new User();
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setEmail(userRequestDTO.getEmail());
        user.setUserName(userRequestDTO.getUsername());
        user.setRole(Role.TENANT);
        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());
        user.setTenant(tenant);
        return user;
    }
    public TenantUserResponseDTO mapToResponseDTO(User user){
        TenantUserResponseDTO tenantUserResponseDTO = new TenantUserResponseDTO();
        tenantUserResponseDTO.setId(user.getId());
        tenantUserResponseDTO.setUsername(user.getUserName());
        tenantUserResponseDTO.setEmail(user.getEmail());
        tenantUserResponseDTO.setFirstName(user.getFirstName());
        tenantUserResponseDTO.setLastName(user.getLastName());
        tenantUserResponseDTO.setRole(user.getRole());
        tenantUserResponseDTO.setTenantId(user.getTenant().getId());
        return tenantUserResponseDTO;
    }


}