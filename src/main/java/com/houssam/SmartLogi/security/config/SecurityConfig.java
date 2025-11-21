package com.houssam.SmartLogi.security.config;

import com.houssam.SmartLogi.security.filter.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // ========== ROUTES PUBLIQUES ==========
                        .requestMatchers("/api/auth/register/client").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // ========== AUTH - REGISTER LIVREUR ==========
                        .requestMatchers("/api/auth/register/livreur").hasRole("GESTIONNAIRE")

                        // ========== ZONES ==========
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/zones").hasRole("GESTIONNAIRE")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/zones/**").hasAnyRole("GESTIONNAIRE", "LIVREUR")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/zones/**").hasRole("GESTIONNAIRE")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/zones/**").hasRole("GESTIONNAIRE")

                        // ========== PRODUITS ==========
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/produits").hasRole("GESTIONNAIRE")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/produits/**").hasAnyRole("GESTIONNAIRE", "LIVREUR")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/produits/**").hasRole("GESTIONNAIRE")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/produits/**").hasRole("GESTIONNAIRE")

                        // ========== LIVREURS ==========
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/livreurs").hasRole("GESTIONNAIRE")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/livreurs/**").hasAnyRole("GESTIONNAIRE", "LIVREUR")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/livreurs/**").hasRole("GESTIONNAIRE")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/livreurs/**").hasRole("GESTIONNAIRE")

                        // ========== CLIENTS EXPÉDITEURS ==========
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/clients").hasRole("GESTIONNAIRE")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/clients/**").hasAnyRole("GESTIONNAIRE", "LIVREUR")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/clients/**").hasRole("GESTIONNAIRE")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/clients/**").hasRole("GESTIONNAIRE")

                        // ========== DESTINATAIRES ==========
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/destinations").hasRole("GESTIONNAIRE")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/destinations/**").hasAnyRole("GESTIONNAIRE", "LIVREUR")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/destinations/**").hasRole("GESTIONNAIRE")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/destinations/**").hasRole("GESTIONNAIRE")

                        // ========== COLIS ==========
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/colis").hasAnyRole("CLIENT", "GESTIONNAIRE")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/colis/**").hasAnyRole("CLIENT", "GESTIONNAIRE", "LIVREUR")
                        .requestMatchers(org.springframework.http.HttpMethod.PATCH, "/api/colis/*/statut").hasAnyRole("GESTIONNAIRE", "LIVREUR")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/colis/**").hasRole("GESTIONNAIRE")

                        // ========== TEST EMAIL ==========
                        .requestMatchers("/api/test-email/**").hasRole("GESTIONNAIRE")

                        // ========== TOUT LE RESTE ==========
                        .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
