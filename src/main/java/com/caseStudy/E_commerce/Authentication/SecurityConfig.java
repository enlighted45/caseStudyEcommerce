package com.caseStudy.E_commerce.Authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity , JwtAuthenticationConverter jwtAuthenticationConverter
    ){
        httpSecurity.csrf(csrf->csrf.disable())
                .sessionManagement(
                        session->
                                session.sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        auth->  auth
                                // 1. Platform Admin
                                .requestMatchers("/tenants/**")
                                .hasRole("ADMIN")

                                // 2. Admin manages tenant users
                                .requestMatchers("/tenantUser/**")
                                .hasRole("ADMIN")

                                // 3. Tenant manages products of its brand
                                .requestMatchers("/*/products/**")
                                .hasRole("TENANT")

                                // 4. Users and Tenant Users can browse/purchase products
                                .requestMatchers("/products/**")
                                .hasAnyRole("USER", "TENANT")

                                // 5. Users and Tenant Users can create/view orders
                                .requestMatchers("/users/*/orders/**")
                                .hasAnyRole("USER", "TENANT")

                                // 6 For User Managing and updating their own data
                                .requestMatchers("/users/**")
                                .hasAnyRole("USER")

                                // 7 register endpoint for user
                                .requestMatchers(HttpMethod.POST, "/auth/signup")
                                .permitAll()



                )
                .oauth2ResourceServer(
                        oauth2->oauth2.jwt(
                                jwt-> jwt.jwtAuthenticationConverter(
                                        jwtAuthenticationConverter
                                )
                        ));

        return httpSecurity.build();

    }
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(
            KeycloakRoleConverter keycloakRoleConverter) {

        JwtAuthenticationConverter converter =
                new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(
                keycloakRoleConverter
        );

        return converter;
    }
}
