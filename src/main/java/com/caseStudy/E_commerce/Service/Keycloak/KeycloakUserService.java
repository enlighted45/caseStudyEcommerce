package com.caseStudy.E_commerce.Service.Keycloak;


import com.caseStudy.E_commerce.DTO.User.UserRequestDTO;
import com.caseStudy.E_commerce.DTO.User.UserUpdateRequestDTO;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.keycloak.representations.idm.CredentialRepresentation;

import java.util.List;

@Service
public class KeycloakUserService {

    private final Keycloak keycloak;

    public KeycloakUserService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public String createUser(
            UserRequestDTO userRequestDTO) {

        UserRepresentation user = new UserRepresentation();

        user.setUsername(userRequestDTO.getUsername());
        user.setEmail(userRequestDTO.getEmail());
        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());
        user.setEnabled(true);


        CredentialRepresentation credential =
                new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(userRequestDTO.getPassword());
        credential.setTemporary(false);


        user.setCredentials(List.of(credential));

        Response response = keycloak
                .realm("E-Commerce")
                .users()
                .create(user);

        if (response.getStatus() != 201) {
            throw new RuntimeException(
                    "Failed to create user in Keycloak. Status: "
                            + response.getStatus()
            );
        }

        String userId =
                CreatedResponseUtil.getCreatedId(response);

        response.close();

        RoleRepresentation userRole = keycloak
                .realm("E-Commerce")
                .roles()
                .get("USER")
                .toRepresentation();

        keycloak
                .realm("E-Commerce")
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(List.of(userRole));
        return userId;
    }


    public void updateUser(String keycloakUserId,
                           UserUpdateRequestDTO userUpdateRequestDTO){
        UserRepresentation user = keycloak
                .realm("E-Commerce")
                .users()
                .get(keycloakUserId)
                .toRepresentation();

        user.setEmail(userUpdateRequestDTO.getEmail());
        user.setFirstName(userUpdateRequestDTO.getFirstName());
        user.setLastName(userUpdateRequestDTO.getLastName());


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
