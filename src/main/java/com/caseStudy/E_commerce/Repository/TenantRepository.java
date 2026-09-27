package com.caseStudy.E_commerce.Repository;

import com.caseStudy.E_commerce.Entity.Tenant;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TenantRepository extends JpaRepository<Tenant,Long> {


    Optional<Tenant> findByName(String name);

    boolean existsByName(@NotBlank(message = "Tenant name is required") String name);
}
