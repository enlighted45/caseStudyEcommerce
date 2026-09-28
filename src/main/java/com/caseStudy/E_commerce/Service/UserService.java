package com.caseStudy.E_commerce.Service;

import com.caseStudy.E_commerce.DTO.User.UserRequestDTO;
import com.caseStudy.E_commerce.DTO.User.UserResponseDTO;
import com.caseStudy.E_commerce.DTO.User.UserUpdateRequestDTO;
import com.caseStudy.E_commerce.Entity.Tenant;
import com.caseStudy.E_commerce.Entity.User;
import com.caseStudy.E_commerce.ExceptionHandler.DuplicateResourceException;
import com.caseStudy.E_commerce.ExceptionHandler.ResourceNotFoundException;
import com.caseStudy.E_commerce.Mapper.UserMapper;
import com.caseStudy.E_commerce.PageValidation.UserPageValidator;
import com.caseStudy.E_commerce.Repository.TenantRepository;
import com.caseStudy.E_commerce.Repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserService {

    private UserRepository userRepository ;
    private UserMapper userMapper;
    private TenantRepository tenantRepository;
    private UserPageValidator userPageValidator;

    public UserService(UserRepository userRepository, UserMapper userMapper,
                       TenantRepository tenantRepository, UserPageValidator userPageValidator){
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
        this.userPageValidator = userPageValidator;
    }

    // create user
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO){
        // checking email username
        boolean var = userRepository.existsByEmail(userRequestDTO.getEmail());
        if(var) throw new DuplicateResourceException("This Email : " + userRequestDTO.getEmail() + " already exist");
        var = userRepository.existsByUserName(userRequestDTO.getUsername());
        if(var) throw new DuplicateResourceException("This Username : " + userRequestDTO.getUsername() + " already exist");
        Long tenantId = userRequestDTO.getTenantId();
        if(tenantId==null){
            User user = userMapper.mapToEntity(userRequestDTO,null);
            user = userRepository.save(user);
            return userMapper.mapToResponseDTO(user);
        }
        else{
            Tenant tenant =  tenantRepository.findById(tenantId).orElseThrow(
                    ()-> new ResourceNotFoundException("Tenant with Id : " + tenantId + "don't exist")
            );
            User user = userMapper.mapToEntity(userRequestDTO,tenant);
            tenant.addUser(user);
            // yahan pe tune cascade lag rakha will that automatically save the tenant
            user = userRepository.save(user);
            return userMapper.mapToResponseDTO(user);
        }
    }
    // find by id
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id){
        User user = userRepository.findById(id).
                orElseThrow(()-> new ResourceNotFoundException("User with Id : " + id + "don't exist"));
        return userMapper.mapToResponseDTO(user);
    }

    // getAllUser for Admin
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        userPageValidator.validate(pageable);
        return userRepository.findAll(pageable).
                map(user->userMapper.mapToResponseDTO(user));
    }

    // getAllUser for Tenant
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> getUsersByTenant(
            Long tenantId,
            Pageable pageable
    ) {
        tenantRepository.findById(tenantId).orElseThrow(
                ()-> new ResourceNotFoundException("Tenant with Id : " + tenantId + "don't exist")
        );
        userPageValidator.validate(pageable);
        return userRepository.findByTenantId(tenantId,pageable).
                map(user->userMapper.mapToResponseDTO(user));
    }

    // update user should be allowed
    // here it didn't update username and tenantId and role will fix it when implementing the Authentication
    @Transactional
    public UserResponseDTO updateUser(
            Long userId,
            UserUpdateRequestDTO request
    ) {
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new ResourceNotFoundException("User with Id : " + userId + "don't exist")
        );
        boolean emailAlreadyExists = userRepository.existsByEmailAndIdNot(userId,request.getEmail());
        if(emailAlreadyExists) throw new DuplicateResourceException("This Email : " + request.getEmail() + " already exist");
        user.setUpdatedAt(LocalDateTime.now());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        return userMapper.mapToResponseDTO(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        // q-1 should I also remove user from tenant usersList if it is a part of list
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new ResourceNotFoundException("User with Id : " + userId + "don't exist")
        );
        Tenant tenant = user.getTenant();
        if (tenant != null) {
            tenant.getUsers().remove(user);
        }
        userRepository.delete(user);
    }

}
