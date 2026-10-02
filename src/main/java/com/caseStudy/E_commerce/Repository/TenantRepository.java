package com.caseStudy.E_commerce.Repository;

import com.caseStudy.E_commerce.Entity.Tenant;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TenantRepository extends JpaRepository<Tenant,Long> {


    Optional<Tenant> findByName(String name);


    @Query(
            """
            SELECT COUNT(t)>0
            FROM Tenant t
            Where t.name=?1 AND t.id!= ?2        
                    """
    )
    boolean existsByNameAndNotById(String name , long id );

    boolean existsByName( String name);
}
