package com.caseStudy.E_commerce.Controller;


import com.caseStudy.E_commerce.DTO.User.UserRequestDTO;
import com.caseStudy.E_commerce.DTO.User.UserResponseDTO;
import com.caseStudy.E_commerce.DTO.User.UserUpdateRequestDTO;
import com.caseStudy.E_commerce.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
public class UserManagementController {


    private UserService userService;
    public UserManagementController(UserService userService){
        this.userService=userService;
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponseDTO> createUser(
            @RequestBody @Valid UserRequestDTO userRequestDTO
    ){
        UserResponseDTO userResponseDTO = userService.createUser(userRequestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED).body(userResponseDTO);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @PathVariable("id") Long id){
        UserResponseDTO userResponseDTO = userService.getUserById(id);
        return ResponseEntity
                .status(HttpStatus.OK).body(userResponseDTO);
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserResponseDTO>> getAllUser(Pageable pageable){
        Page<UserResponseDTO> pageResponse = userService.getAllUsers(pageable);
        return ResponseEntity
                .status(HttpStatus.OK).body(pageResponse);

    }

    @GetMapping("/{tenantName}/users")
    public ResponseEntity<Page<UserResponseDTO>> getUsersByTenant
            ( @PathVariable("tenantName") String tenantName, Pageable pageable){
        Page<UserResponseDTO> pageResponse = userService.
                getUsersByTenant(tenantName,pageable);
        return ResponseEntity
                .status(HttpStatus.OK).body(pageResponse);
    }

    // update user
    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> updateUserById
            (@PathVariable("id") Long id , @RequestBody
            @Valid UserUpdateRequestDTO userUpdateRequestDTO){

        UserResponseDTO userResponseDTO = userService.updateUser(id,userUpdateRequestDTO);
        return ResponseEntity
                .status(HttpStatus.OK).body(userResponseDTO);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable("id") Long id){
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



















}
