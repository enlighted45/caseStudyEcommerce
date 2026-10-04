package com.caseStudy.E_commerce.Authentication.AuthenticationService;

import com.caseStudy.E_commerce.Entity.User;
import org.springframework.stereotype.Component;

@Component
public class TenantAuthorizationService {

    private final AuthenticatedUserService authenticatedUserService;

    public TenantAuthorizationService(
            AuthenticatedUserService authenticatedUserService) {

        this.authenticatedUserService =
                authenticatedUserService;
    }

    public boolean isCurrentUserTenant(String tenantName) {

        User user =
                authenticatedUserService.getCurrentUser();

        if (user.getTenant() == null) {
            return false;
        }

        return user.getTenant()
                .getName()
                .equals(tenantName);
    }
}
