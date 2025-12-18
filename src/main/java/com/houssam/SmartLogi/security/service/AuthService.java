package com.houssam.SmartLogi.security.service;

import com.houssam.SmartLogi.dto.LivreurDTO;
import com.houssam.SmartLogi.dto.LoginDTO;
import com.houssam.SmartLogi.dto.RegisterDTO;
import com.houssam.SmartLogi.exception.ResourceNotFoundException;
import com.houssam.SmartLogi.model.ClientExpediteur;
import com.houssam.SmartLogi.model.Livreur;
import com.houssam.SmartLogi.model.User;
import com.houssam.SmartLogi.model.Zone;
import com.houssam.SmartLogi.repository.ClientExpediteurRepository;
import com.houssam.SmartLogi.repository.LivreurRepository;
import com.houssam.SmartLogi.repository.UserRepository;
import com.houssam.SmartLogi.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.houssam.SmartLogi.repository.RoleRepository;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClientExpediteurRepository clientExpediteurRepository;
    @Autowired
    private LivreurRepository livreurRepository;
    @Autowired
    private ZoneRepository zoneRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    public LoginDTO.Response login(LoginDTO.Request request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
            String token = jwtService.generateToken(userDetails);
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

            return new LoginDTO.Response(
                    token,
                    user.getEmail(),
                    user.getRole().getName(),
                    user.getId()
            );

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Email ou mot de passe incorrect");
        }
    }

    @Transactional
    public LoginDTO.Response register(RegisterDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Récupérer le rôle CLIENT depuis la base de données
        com.houssam.SmartLogi.model.Role clientRole = roleRepository.findByName("CLIENT")
                .orElseThrow(() -> new RuntimeException("Rôle CLIENT non trouvé dans la base"));
        user.setRole(clientRole);

        ClientExpediteur client = new ClientExpediteur();
        client.setNom(request.getNom());
        client.setPrenom(request.getPrenom());
        client.setEmail(request.getEmail());
        client.setTelephone(request.getTelephone());
        client.setAdresse(request.getAdresse());

        client.setUser(user);
        user.setClientExpediteur(client);

        ClientExpediteur savedClient = clientExpediteurRepository.save(client);

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtService.generateToken(userDetails);

        return new LoginDTO.Response(
                token,
                savedClient.getUser().getEmail(),
                savedClient.getUser().getRole().getName(),
                savedClient.getUser().getId()
        );
    }

    @Transactional
    public LoginDTO.Response registerLivreur(LivreurDTO request) {
        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà");
        }

        // Vérifier que la zone existe
        Zone zone = zoneRepository.findById(request.getZoneAssigneeId())
                .orElseThrow(() -> new ResourceNotFoundException("Zone introuvable avec l'ID " + request.getZoneAssigneeId()));

        // Créer le User
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Récupérer le rôle LIVREUR depuis la base de données
        com.houssam.SmartLogi.model.Role livreurRole = roleRepository.findByName("LIVREUR")
                .orElseThrow(() -> new RuntimeException("Rôle LIVREUR non trouvé dans la base"));
        user.setRole(livreurRole);

        // Créer le Livreur
        Livreur livreur = new Livreur();
        livreur.setNom(request.getNom());
        livreur.setPrenom(request.getPrenom());
        livreur.setEmail(request.getEmail());
        livreur.setTelephone(request.getTelephone());
        livreur.setVehicule(request.getVehicule());
        livreur.setZoneAssignee(zone);

        // Association bidirectionnelle User <-> Livreur
        livreur.setUser(user);
        user.setLivreur(livreur);

        // Sauvegarder (cascade sauvegarde aussi le User)
        Livreur savedLivreur = livreurRepository.save(livreur);

        // Générer le token JWT
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtService.generateToken(userDetails);

        // Retourner la réponse
        return new LoginDTO.Response(
                token,
                savedLivreur.getUser().getEmail(),
                savedLivreur.getUser().getRole().getName(),
                savedLivreur.getUser().getId()
        );
    }
}

