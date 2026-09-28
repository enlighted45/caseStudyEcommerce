package com.caseStudy.E_commerce.Mapper;

import com.caseStudy.E_commerce.DTO.User.UserRequestDTO;
import com.caseStudy.E_commerce.DTO.User.UserResponseDTO;
import com.caseStudy.E_commerce.Entity.Tenant;
import com.caseStudy.E_commerce.Entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {

    public User mapToEntity(UserRequestDTO userRequestDTO, Tenant tenant){
        User user = new User();
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setEmail(userRequestDTO.getEmail());
        user.setUserName(userRequestDTO.getUsername());
        user.setRole(userRequestDTO.getRole());
        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());
        user.setTenant(tenant);
        return user;
    }
    public UserResponseDTO mapToResponseDTO(User user){
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setUsername(user.getUserName());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setFirstName(user.getFirstName());
        userResponseDTO.setLastName(user.getLastName());
        userResponseDTO.setRole(user.getRole());
        if (user.getTenant() != null) {
            userResponseDTO.setTenantId(user.getTenant().getId());
        }
        return userResponseDTO;
    }


}
