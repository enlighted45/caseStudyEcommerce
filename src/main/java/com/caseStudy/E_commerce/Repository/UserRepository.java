package com.caseStudy.E_commerce.Repository;

import com.caseStudy.E_commerce.DTO.User.UserRequestDTO;
import com.caseStudy.E_commerce.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {


    @Query(
            """
            SELECT u
            FROM User u
            WHERE u.userName = ?1
            """
    )
    Optional<User> findByUsername(String userName);

    boolean existsByEmail(String email);
    boolean existsByUserName(String username);

    @EntityGraph(attributePaths = "tenant")
    @Query(
            """
            SELECT u
            FROM User u
            JOIN u.tenant t
            WHERE t.name = ?1                                                 
            """
    )
    Page<User> findByTenantName(String tenantName , Pageable pageable);

    @Query(
            """
            Select count(u)>0
            FROM User u 
            WHERE u.id!=?1 AND u.email=?2                                    
            """
    )
    boolean existsByEmailAndIdNot(long id,String email);

    @EntityGraph(attributePaths = "tenant")
    @Query(
            """
            SELECT u
            FROM User u
            JOIN u.tenant t
            WHERE u.id=?1 AND t.id=?2                                                 
            """
    )

    Optional<User> findByUserANDTenant(Long userID, Long TenantId);

    @EntityGraph(attributePaths = "tenant")
    @Query(
            """
            SELECT u
            FROM User u
            JOIN u.tenant t
            WHERE u.id=?1 AND t.name=?2                                                 
            """
    )
    Optional<User> findByUserANDTenantName(Long userID, String tenantName);


}
