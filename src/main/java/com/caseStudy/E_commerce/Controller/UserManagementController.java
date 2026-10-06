package com.caseStudy.E_commerce.Controller;


import com.caseStudy.E_commerce.DTO.User.UserRequestDTO;
import com.caseStudy.E_commerce.DTO.User.UserResponseDTO;
import com.caseStudy.E_commerce.DTO.User.UserUpdateRequestDTO;
import com.caseStudy.E_commerce.Service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
@SecurityRequirement(name = "bearerAuth")
@SecurityRequirement(name = "bearerAuth")

public class UserManagementController {


    private UserService userService;
    public UserManagementController(UserService userService){
        this.userService=userService;
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<UserResponseDTO> createUser(
            @RequestBody @Valid UserRequestDTO userRequestDTO
    ){
        UserResponseDTO userResponseDTO = userService.createUser(userRequestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED).body(userResponseDTO);
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("@userAuthorizationService.isCurrentUser(#id)")
    public ResponseEntity<UserResponseDTO> getUserById(
            @PathVariable("id") Long id){
        UserResponseDTO userResponseDTO = userService.getUserById(id);
        return ResponseEntity
                .status(HttpStatus.OK).body(userResponseDTO);
    }

    // update user
    @PutMapping("/users/{id}")
    @PreAuthorize("@userAuthorizationService.isCurrentUser(#id)")
    public ResponseEntity<UserResponseDTO> updateUserById
    (@PathVariable("id") Long id , @RequestBody
    @Valid UserUpdateRequestDTO userUpdateRequestDTO){

        UserResponseDTO userResponseDTO = userService.updateUser(id,userUpdateRequestDTO);
        return ResponseEntity
                .status(HttpStatus.OK).body(userResponseDTO);
    }
    @PreAuthorize("@userAuthorizationService.isCurrentUser(#id)")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable("id") Long id){
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
