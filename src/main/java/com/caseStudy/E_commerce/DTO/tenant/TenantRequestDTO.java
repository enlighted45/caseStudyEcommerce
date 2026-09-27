package com.caseStudy.E_commerce.DTO.tenant;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenantRequestDTO {
    @NotBlank(message = "Tenant name is required")
    private String name;
}
