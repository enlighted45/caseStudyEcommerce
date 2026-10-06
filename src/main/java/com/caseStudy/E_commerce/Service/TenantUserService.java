package com.caseStudy.E_commerce.Service;

import com.caseStudy.E_commerce.DTO.User.*;
import com.caseStudy.E_commerce.Entity.Tenant;
import com.caseStudy.E_commerce.Entity.User;
import com.caseStudy.E_commerce.ExceptionHandler.DuplicateResourceException;
import com.caseStudy.E_commerce.ExceptionHandler.ResourceNotFoundException;
import com.caseStudy.E_commerce.Mapper.TenantUserMapper;
import com.caseStudy.E_commerce.Mapper.UserMapper;
import com.caseStudy.E_commerce.PageValidation.UserPageValidator;
import com.caseStudy.E_commerce.Repository.TenantRepository;
import com.caseStudy.E_commerce.Repository.UserRepository;
import com.caseStudy.E_commerce.Service.Keycloak.KeycloakTenantUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TenantUserService {

    private UserRepository userRepository ;
    private TenantUserMapper tenantUserMapper;
    private TenantRepository tenantRepository;
    private UserPageValidator userPageValidator;
    private KeycloakTenantUserService keycloakTenantUserService;

    public TenantUserService(UserRepository userRepository, TenantUserMapper tenantUserMapper,
                             TenantRepository tenantRepository, UserPageValidator
                                     userPageValidator,KeycloakTenantUserService keycloakTenantUserService){
        this.tenantUserMapper = tenantUserMapper;
        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
        this.userPageValidator = userPageValidator;
        this.keycloakTenantUserService=keycloakTenantUserService;
    }

    // create user
    @Transactional
    public TenantUserResponseDTO createUser(
            TenantUserRequestDTO tenantUserRequestDTO){

        String keycloakTenantUserId = null;

       try{
           // checking email username
           boolean var = userRepository.existsByEmail(tenantUserRequestDTO.getEmail());
           if(var) throw new DuplicateResourceException("This Email : " + tenantUserRequestDTO.getEmail() + " already exist");
           var = userRepository.existsByUserName(tenantUserRequestDTO.getUsername());
           if(var) throw new DuplicateResourceException("This Username : " + tenantUserRequestDTO.getUsername() + " already exist");
           Tenant tenant =  tenantRepository.findByName(
                   tenantUserRequestDTO.getTenantName()).orElseThrow(
                   ()-> new ResourceNotFoundException("Tenant with Name : " +
                           tenantUserRequestDTO.getTenantName() + "don't exist")
           );

           User user = tenantUserMapper.mapToEntity(tenantUserRequestDTO,tenant);
           tenant.addUser(user);
           keycloakTenantUserId=
                   keycloakTenantUserService.createUser(tenantUserRequestDTO);
           // yahan pe tune cascade lag rakha will that automatically save the tenant
           user.setKeycloakId(keycloakTenantUserId);
           user = userRepository.save(user);
           return tenantUserMapper.mapToResponseDTO(user);
       } catch (Exception e) {
           if(keycloakTenantUserId!=null){
               keycloakTenantUserService.deleteUser(keycloakTenantUserId);
           }
           throw e;
       }
    }
    // find by id and tenantName
    @Transactional(readOnly = true)
    public TenantUserResponseDTO getUserById(Long id,String tenantName){
        User user = userRepository.findByUserANDTenantName(id,tenantName).
                orElseThrow(()-> new ResourceNotFoundException("User with Id : " + id +
                        "don't exist under Tenant Name : " + tenantName));
        return tenantUserMapper.mapToResponseDTO(user);
    }

    // getAllUser for Admin
    @Transactional(readOnly = true)
    public Page<TenantUserResponseDTO> getAllUsers(Pageable pageable) {
        userPageValidator.validate(pageable);
        return userRepository.findAll(pageable).
                map(user->tenantUserMapper.mapToResponseDTO(user));
    }

    // getAllUser for Tenant
    @Transactional(readOnly = true)
    public Page<TenantUserResponseDTO> getUsersByTenant(
            String tenantName,
            Pageable pageable
    ) {
        tenantRepository.findByName(tenantName).orElseThrow(
                ()-> new ResourceNotFoundException("Tenant with Name : " + tenantName + "don't exist")
        );
        userPageValidator.validate(pageable);
        return userRepository.findByTenantName(tenantName,pageable).
                map(user->tenantUserMapper.mapToResponseDTO(user));
    }

    // update user should be allowed
    // here it didn't update username and tenantId and role will fix it when implementing the Authentication
    @Transactional
    public TenantUserResponseDTO updateUser(
            Long userId, String tenantName,
            UserUpdateRequestDTO request
    ) {
        User user = userRepository.findByUserANDTenantName(userId,tenantName).
                orElseThrow(()-> new ResourceNotFoundException("User with Id : " + userId +
                        "don't exist under Tenant Name : " + tenantName));
        boolean emailAlreadyExists = userRepository.existsByEmailAndIdNot(userId,request.getEmail());
        if(emailAlreadyExists) throw new DuplicateResourceException("This Email : " + request.getEmail() + " already exist");
        user.setUpdatedAt(LocalDateTime.now());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        keycloakTenantUserService.updateUser(user.getKeycloakId(), request);
        return tenantUserMapper.mapToResponseDTO(user);
    }

    @Transactional
    public void deleteUser(Long userId,String tenantName) {
        // q-1 should I also remove user from tenant usersList if it is a part of list
        User user = userRepository.findByUserANDTenantName(userId,tenantName).
                orElseThrow(()-> new ResourceNotFoundException("User with Id : " + userId +
                        "don't exist under Tenant Name : " + tenantName));
        keycloakTenantUserService.deleteUser(user.getKeycloakId());
        user.getTenant()
                .getUsers()
                .remove(user);
        userRepository.delete(user);
    }

}

