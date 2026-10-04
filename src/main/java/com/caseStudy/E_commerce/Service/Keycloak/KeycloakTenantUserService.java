package com.caseStudy.E_commerce.Service.Keycloak;

import com.caseStudy.E_commerce.DTO.User.TenantUserRequestDTO;
import com.caseStudy.E_commerce.DTO.User.UserUpdateRequestDTO;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeycloakTenantUserService {

    private final Keycloak keycloak;

    public KeycloakTenantUserService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public String createUser(TenantUserRequestDTO request) {

        UserRepresentation user = new UserRepresentation();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEnabled(true);

        CredentialRepresentation credential =
                new CredentialRepresentation();

        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.getPassword());
        credential.setTemporary(false);

        user.setCredentials(List.of(credential));

        String userId;

        try (Response response = keycloak
                .realm("E-Commerce")
                .users()
                .create(user)) {

            if (response.getStatus() != 201) {
                throw new RuntimeException(
                        "Failed to create tenant user in Keycloak. Status: "
                                + response.getStatus()
                );
            }

            userId = CreatedResponseUtil.getCreatedId(response);
        }

        // Get TENANT role
        RoleRepresentation tenantRole = keycloak
                .realm("E-Commerce")
                .roles()
                .get("TENANT")
                .toRepresentation();

        // Assign TENANT role
        keycloak
                .realm("E-Commerce")
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(List.of(tenantRole));

        return userId;
    }


    public void updateUser(
            String keycloakUserId,
            UserUpdateRequestDTO request) {

        UserRepresentation user = keycloak
                .realm("E-Commerce")
                .users()
                .get(keycloakUserId)
                .toRepresentation();

        // Only these three fields can be updated
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        keycloak
                .realm("E-Commerce")
                .users()
                .get(keycloakUserId)
                .update(user);
    }


    public void deleteUser(String keycloakUserId) {

        keycloak
                .realm("E-Commerce")
                .users()
                .delete(keycloakUserId);
    }
}
