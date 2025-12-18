package com.houssam.SmartLogi.security.service;

import com.houssam.SmartLogi.model.Permission;
import com.houssam.SmartLogi.model.User;
import com.houssam.SmartLogi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable avec l'email: " + email));

        List<GrantedAuthority> authorities = new ArrayList<>();

        // Ajouter le rôle principal (ROLE_GESTIONNAIRE, ROLE_LIVREUR, ROLE_CLIENT)
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));

        // Ajouter les permissions (COLIS_READ, COLIS_UPDATE, etc.)
        Set<Permission> permissions = user.getRole().getPermissions();
        if (permissions != null) {
            for (Permission permission : permissions) {
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            }
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }

}

