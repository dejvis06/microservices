package com.example.composite.product;

import static org.springframework.http.HttpMethod.*;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class SecurityConfig {

    public static final String PRODUCTS_COMPOSITE = "/products-composite/**";

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange()
                .pathMatchers("/openapi/**").permitAll()
                .pathMatchers("/webjars/**").permitAll()
                .pathMatchers("/actuator/**").permitAll()
                .pathMatchers(POST, PRODUCTS_COMPOSITE).hasAuthority("SCOPE_product:write")
                .pathMatchers(DELETE, PRODUCTS_COMPOSITE).hasAuthority("SCOPE_product:write")
                .pathMatchers(GET, PRODUCTS_COMPOSITE).hasAuthority("SCOPE_product:read")
                .anyExchange().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt();
        return http.build();
    }
}