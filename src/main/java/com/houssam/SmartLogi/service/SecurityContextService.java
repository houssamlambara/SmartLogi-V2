package com.houssam.SmartLogi.service;

import com.houssam.SmartLogi.exception.ResourceNotFoundException;
import com.houssam.SmartLogi.model.User;
import com.houssam.SmartLogi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityContextService {

    private final UserRepository userRepository;

    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()){
            throw new RuntimeException("No authenticated user found");
        }

        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("Utilisateur non trouvé avec l'email: " + email));
    }

    public String getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public String getCurrentUserRoleName() {
        return getCurrentUser().getRole().getName();
    }

    public boolean isGestionnaire() {
        return "GESTIONNAIRE".equals(getCurrentUserRoleName());
    }

    public boolean isLivreur() {
        return "LIVREUR".equals(getCurrentUserRoleName());
    }

    public boolean isClient() {
        return "CLIENT".equals(getCurrentUserRoleName());
    }

    public String getCurrentClientExpediteurId() {
        User user = getCurrentUser();
        if (user.getClientExpediteur() == null) {
            throw new RuntimeException("L'utilisateur connecté n'est pas un client expéditeur");
        }
        return user.getClientExpediteur().getId();
    }

    public String getCurrentLivreurId() {
        User user = getCurrentUser();
        if (user.getLivreur() == null) {
            throw new RuntimeException("L'utilisateur connecté n'est pas un livreur");
        }
        return user.getLivreur().getId();
    }
}
