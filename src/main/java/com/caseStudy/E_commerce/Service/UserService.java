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
import com.caseStudy.E_commerce.Service.Keycloak.KeycloakUserService;
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
    private final KeycloakUserService keycloakUserService;

    public UserService(UserRepository userRepository, UserMapper userMapper,
                       TenantRepository tenantRepository, UserPageValidator userPageValidator, KeycloakUserService keycloakUserService){
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
        this.userPageValidator = userPageValidator;
        this.keycloakUserService = keycloakUserService;
    }

    // create user
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO){

        String keycloakUserId = null;

        try{
            // checking email username
            boolean var = userRepository.existsByEmail(userRequestDTO.getEmail());
            if(var) throw new DuplicateResourceException("This Email : " + userRequestDTO.getEmail() + " already exist");
            var = userRepository.existsByUserName(userRequestDTO.getUsername());
            if(var) throw new DuplicateResourceException("This Username : " + userRequestDTO.getUsername() + " already exist");
            // 1. Create user in Keycloak
            keycloakUserId=keycloakUserService.createUser(userRequestDTO);
            User user = userMapper.mapToEntity(userRequestDTO);
            user.setKeycloakId(keycloakUserId);

            user = userRepository.save(user);
            return userMapper.mapToResponseDTO(user);
        } catch (Exception e) {
            if (keycloakUserId != null) {
                keycloakUserService.deleteUser(keycloakUserId);
            }
            throw new RuntimeException(e);
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


    // update user should be allowed
    // here it didn't update username and tenantId and role will fix it when implementing the Authentication
    @Transactional
    public UserResponseDTO updateUser(
            Long userId,
            UserUpdateRequestDTO request
    ) {
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new ResourceNotFoundException("User with Id : "
                        + userId + "don't exist")
        );
        boolean emailAlreadyExists = userRepository.existsByEmailAndIdNot
                (userId,request.getEmail());
        if(emailAlreadyExists) throw new DuplicateResourceException
                ("This Email : " + request.getEmail() + " already exist");

        keycloakUserService.updateUser(user.getKeycloakId(),request);
        user.setUpdatedAt(LocalDateTime.now());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        return userMapper.mapToResponseDTO(user);


    }

    public Long getUserByKeycloakUserID(String userId){
        User user = userRepository.findByKeycloakId(userId).orElseThrow(
                ()-> new ResourceNotFoundException("User with Id : " + userId + "don't exist") );
        return user.getId();
    }

    @Transactional
    public void deleteUser(Long userId) {
        // q-1 should I also remove user from tenant usersList if it is a part of list
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new ResourceNotFoundException("User with Id : " + userId + "don't exist")
        );

        keycloakUserService.deleteUser(user.getKeycloakId());


        userRepository.delete(user);

    }

}
