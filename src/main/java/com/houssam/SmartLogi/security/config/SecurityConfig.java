package com.houssam.SmartLogi.security.config;

import com.houssam.SmartLogi.security.filter.JwtAuthFilter;
import com.houssam.SmartLogi.security.handler.OAuth2LoginSuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    @Lazy
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:4200",  // Angular
                "http://localhost:3000",  // React
                "http://localhost:8080",
                "http://localhost:5173"   // Vite
        ));

        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));

        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept"
        ));

        // Permettre l'envoi de credentials
        configuration.setAllowCredentials(true);

        // Headers exposés au client
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        // Durée de cache (1 heure)
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CORS et CSRF
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())

                // Gestion des URL publiques
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/oauth2/**",
                                "/login/**",
                                "/api/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // Routes réservées aux gestionnaires
                        .requestMatchers("/api/auth/register/livreur").hasRole("GESTIONNAIRE")
                        .requestMatchers("/api/admin/**").hasRole("GESTIONNAIRE")

                        // Zones
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/zones").hasRole("GESTIONNAIRE")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/zones/**").hasAnyRole("GESTIONNAIRE", "LIVREUR")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/zones/**").hasRole("GESTIONNAIRE")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/zones/**").hasRole("GESTIONNAIRE")

                        // Produits
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/produits").hasRole("GESTIONNAIRE")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/produits/**").hasAnyRole("GESTIONNAIRE", "LIVREUR")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/produits/**").hasRole("GESTIONNAIRE")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/produits/**").hasRole("GESTIONNAIRE")

                        // Livreurs
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/livreurs").hasRole("GESTIONNAIRE")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/livreurs/**").hasAnyRole("GESTIONNAIRE", "LIVREUR")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/livreurs/**").hasRole("GESTIONNAIRE")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/livreurs/**").hasRole("GESTIONNAIRE")

                        // Clients
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/clients").hasRole("GESTIONNAIRE")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/clients/**").hasAnyRole("GESTIONNAIRE", "LIVREUR")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/clients/**").hasRole("GESTIONNAIRE")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/clients/**").hasRole("GESTIONNAIRE")

                        // Destinataires
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/destinations").hasRole("GESTIONNAIRE")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/destinations/**").hasAnyRole("GESTIONNAIRE", "LIVREUR")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/destinations/**").hasRole("GESTIONNAIRE")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/destinations/**").hasRole("GESTIONNAIRE")

                        // Colis
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/colis").hasAnyRole("CLIENT", "GESTIONNAIRE")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/colis/**").hasAnyRole("CLIENT", "GESTIONNAIRE", "LIVREUR")
                        .requestMatchers(org.springframework.http.HttpMethod.PATCH, "/api/colis/*/statut").hasAnyRole("GESTIONNAIRE", "LIVREUR")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/colis/**").hasRole("GESTIONNAIRE")

                        // Test email
                        .requestMatchers("/api/test-email/**").hasRole("GESTIONNAIRE")

                        // Tout le reste requiert authentification
                        .anyRequest().authenticated()
                )

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            if (request.getRequestURI().startsWith("/api/")) {
                                response.setContentType("application/json");
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                response.getWriter().write("{\"error\":\"Non autorisé\"}");
                            } else {
                                response.sendRedirect("/login");
                            }
                        })
                )

                // Activation OAuth2 Login
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2LoginSuccessHandler)
                )

                // Sessions Stateless
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // JWT Filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
