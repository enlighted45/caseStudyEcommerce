package com.caseStudy.E_commerce.Authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity , JwtAuthenticationConverter jwtAuthenticationConverter
    ){
        httpSecurity.csrf(csrf->csrf.disable())
                //used for cors cross origin reference
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .sessionManagement(
                        session->
                                session.sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(
                        auth->  auth

                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                                // 1. Platform Admin
                                .requestMatchers("/tenants/**")
                                .hasRole("ADMIN")

                                .requestMatchers("/tenantUser/me")
                                .hasRole("TENANT")

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

                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/v3/api-docs/**"
                                ).permitAll()



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
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of("http://localhost:5173")
        );
        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
        );
        configuration.setAllowedHeaders(
                List.of("Authorization", "Content-Type", "Accept")
        );
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
