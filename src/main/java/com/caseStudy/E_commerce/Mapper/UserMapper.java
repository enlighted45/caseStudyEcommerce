package com.caseStudy.E_commerce.Mapper;

import com.caseStudy.E_commerce.DTO.User.UserRequestDTO;
import com.caseStudy.E_commerce.Entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User mapToEntity(UserRequestDTO userRequestDTO){
        User user = new User();
        user.setCreatedAt();
        user.setEmail();
        user.setUserName();
        user.setRole();
        user.setFirstName();
        user.setLastName();
        user.

    }
}
