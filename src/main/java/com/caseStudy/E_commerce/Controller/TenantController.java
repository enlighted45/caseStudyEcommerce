package com.caseStudy.E_commerce.Controller;


import com.caseStudy.E_commerce.DTO.tenant.TenantRequestDTO;
import com.caseStudy.E_commerce.DTO.tenant.TenantResponseDTO;
import com.caseStudy.E_commerce.Service.TenantService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tenants")
@PreAuthorize("hasRole('ADMIN')")
public class TenantController {

    private TenantService tenantService;
    public TenantController(TenantService tenantService){
        this.tenantService=tenantService;
    }

    @PostMapping
    public ResponseEntity<TenantResponseDTO> createTenant(
            @RequestBody  @Valid TenantRequestDTO  tenantRequestDTO){
        // pass in service layer to get tenantresponse dto
        //build responseEntity and return it
        TenantResponseDTO tenantResponseDTO =
                tenantService.createTenant(tenantRequestDTO);
        return ResponseEntity.
                status(HttpStatus.CREATED).body(tenantResponseDTO);
    }

    // get tenant
    @GetMapping("/{id}")
    public ResponseEntity<TenantResponseDTO> getTenantById(
            @PathVariable("id") Long id){
        TenantResponseDTO tenantResponseDTO =
                tenantService.getTenantById(id);
        return ResponseEntity.
                status(HttpStatus.OK).body(tenantResponseDTO);
    }

    @GetMapping
    public ResponseEntity<Page<TenantResponseDTO>> getAllTenant(
             Pageable page){
        Page<TenantResponseDTO> pageResponse = tenantService.getAllTenants(page);
        return ResponseEntity.
                status(HttpStatus.OK).body(pageResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TenantResponseDTO> updateTenant(
            @PathVariable("id") Long id,
            @RequestBody @Valid TenantRequestDTO tenantRequestDTO){
        TenantResponseDTO tenantResponseDTO =
                tenantService.updateTenant(id,tenantRequestDTO);
        return ResponseEntity.
                status(HttpStatus.CREATED).body(tenantResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTenant(
            @PathVariable("id") Long id){
                tenantService.deleteTenant(id);
        return ResponseEntity.
                status(HttpStatus.NO_CONTENT).build();
    }






}
