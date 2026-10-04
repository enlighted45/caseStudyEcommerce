package com.caseStudy.E_commerce.Mapper;

import com.caseStudy.E_commerce.DTO.User.UserRequestDTO;
import com.caseStudy.E_commerce.DTO.User.UserResponseDTO;
import com.caseStudy.E_commerce.Entity.Tenant;
import com.caseStudy.E_commerce.Entity.User;
import com.caseStudy.E_commerce.Enum.Role;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {

    public User mapToEntity(UserRequestDTO userRequestDTO){
        User user = new User();
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setEmail(userRequestDTO.getEmail());
        user.setUserName(userRequestDTO.getUsername());
        user.setRole(Role.USER);
        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());
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
        return userResponseDTO;
    }


}
