package com.caseStudy.E_commerce.Controller;

import com.caseStudy.E_commerce.DTO.User.*;
import com.caseStudy.E_commerce.Service.TenantUserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tenantUser")
@SecurityRequirement(name = "bearerAuth")
public class TenantUserController {
    private final TenantUserService tenantUserService;

    public TenantUserController(TenantUserService tenantUserService) {
        this.tenantUserService = tenantUserService;
    }

    @PostMapping
    public ResponseEntity<TenantUserResponseDTO> createTenantUser(
            @RequestBody @Valid TenantUserRequestDTO request) {

        TenantUserResponseDTO response =
                tenantUserService.createUser(
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<TenantUserResponseMeDTO> getUserMe(@AuthenticationPrincipal Jwt jwt){
        String keycloakUserId = jwt.getSubject();
        TenantUserResponseMeDTO tenantUserResponseMeDTO =
                tenantUserService.getTenantUserByKeycloak(keycloakUserId);
        return ResponseEntity
                .status(HttpStatus.OK).body(tenantUserResponseMeDTO);
    }


    @GetMapping("/{tenantName}")

    public ResponseEntity<Page<TenantUserResponseDTO>> getAllUserByTenantName
            (@PathVariable("tenantName") String tenantName,@ParameterObject Pageable pageable){

        Page<TenantUserResponseDTO> pageResponse = tenantUserService.
                getUsersByTenant(tenantName,pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pageResponse);
    }
    // now what else to do
    @GetMapping("/{tenantName}/{userId}")

    public ResponseEntity<TenantUserResponseDTO> getTenantUser(
            @PathVariable String tenantName,
            @PathVariable Long userId) {

        TenantUserResponseDTO response =
                tenantUserService.getUserById(
                        userId,tenantName
                );

        return ResponseEntity.ok(response);
    }
    // next what to see
    // update mapping
    @PutMapping("/{tenantName}/{userId}")

    public ResponseEntity<TenantUserResponseDTO> updateTenantUser(
            @PathVariable String tenantName,
            @PathVariable Long userId,
            @RequestBody @Valid UserUpdateRequestDTO request) {

        TenantUserResponseDTO response =
                tenantUserService.updateUser(
                        userId,
                        tenantName,
                        request
                );

        return ResponseEntity.ok(response);
    }

    // now delete
    @DeleteMapping("/{tenantName}/{userId}")

    public ResponseEntity<Void> deleteTenantUser(
            @PathVariable String tenantName,
            @PathVariable Long userId
           ) {

        tenantUserService.deleteUser(userId,
                tenantName
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
