package com.houssam.SmartLogi.security.handler;

import com.houssam.SmartLogi.enums.Provider;
import com.houssam.SmartLogi.security.service.AuthService;
import com.houssam.SmartLogi.security.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    private static final String FRONT_URL = "http://localhost:8081/oauth2/success";

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        String registrationId =
                ((org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken) authentication)
                        .getAuthorizedClientRegistrationId();

        Provider provider = Provider.valueOf(registrationId.toUpperCase());

        String providerId = oAuth2User.getAttribute("sub");

        UserDetails userDetails =
                authService.loginWithOAuth2(email, name, provider, providerId);

        String jwt = jwtService.generateToken(userDetails);

        // Retour JSON direct
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"token\":\"" + jwt + "\"}");
        response.getWriter().flush();
    }
}
