package com.houssam.SmartLogi.security.controller;

import com.houssam.SmartLogi.dto.LivreurDTO;
import com.houssam.SmartLogi.dto.LoginDTO;
import com.houssam.SmartLogi.dto.RegisterDTO;
import com.houssam.SmartLogi.response.ApiResponse;
import com.houssam.SmartLogi.security.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentification", description = "Opérations d'authentification")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Connexion", description = "Authentifier un utilisateur et obtenir un token JWT")
    public ResponseEntity<ApiResponse<LoginDTO.Response>> login(@Valid @RequestBody LoginDTO.Request request) {
        LoginDTO.Response response = authService.login(request);
        return ResponseEntity.ok(new ApiResponse<>("Connexion réussie", response));
    }

    @PostMapping("/register/client")
    @Operation(summary = "Inscription Client", description = "Créer un nouveau compte client")
    public ResponseEntity<ApiResponse<LoginDTO.Response>> registerClient(@Valid @RequestBody RegisterDTO request) {
        LoginDTO.Response response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Inscription réussie. Vous êtes maintenant connecté en tant que CLIENT.", response));
    }

    @PostMapping("/register/livreur")
    @Operation(summary = "Inscription Livreur", description = "Créer un nouveau compte livreur")
    public ResponseEntity<ApiResponse<LoginDTO.Response>> registerLivreur(@Valid @RequestBody LivreurDTO request) {
        LoginDTO.Response response = authService.registerLivreur(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Inscription réussie. Vous êtes maintenant connecté en tant que LIVREUR.", response));
    }

}
