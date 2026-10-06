package com.caseStudy.E_commerce.Authentication.AuthenticationService;

import com.caseStudy.E_commerce.Entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserAuthorizationService {

    private final AuthenticatedUserService authenticatedUserService;

    public UserAuthorizationService(
            AuthenticatedUserService authenticatedUserService) {
        this.authenticatedUserService =
                authenticatedUserService;
    }

    public boolean isCurrentUser(Long userId) {

        User currentUser =
                authenticatedUserService.getCurrentUser();

        System.out.println(currentUser.getId());
        System.out.println(userId);

        return currentUser.getId().equals(userId);
    }
}
