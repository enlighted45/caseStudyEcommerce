package com.caseStudy.E_commerce.PageValidation;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TenantPageableValidator {
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "name",
            "createdAt",
            "updatedAt"

    );

    public void validate(Pageable pageable) {

        pageable.getSort().forEach(order -> {

            String property = order.getProperty();

            if (!ALLOWED_SORT_FIELDS.contains(property)) {
                throw new IllegalArgumentException(
                        "Invalid sort field: " + property
                );
            }
        });
    }
}
