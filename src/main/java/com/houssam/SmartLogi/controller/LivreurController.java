package com.houssam.SmartLogi.controller;

import com.houssam.SmartLogi.dto.LivreurDTO;
import com.houssam.SmartLogi.service.LivreurService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/livreurs")
public class LivreurController {

    private final LivreurService service;

    public LivreurController(LivreurService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<LivreurDTO> createLivreur(@Valid @RequestBody LivreurDTO dto) {
        LivreurDTO created = service.createLivreur(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<LivreurDTO>> getAllLivreurs() {
        return ResponseEntity.ok(service.getAllLivreurs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivreurDTO> getLivreurById(@PathVariable Long id) {
        LivreurDTO livreur = service.getLivreurById(id);
        return (livreur != null) ? ResponseEntity.ok(livreur) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLivreur(@PathVariable Long id) {
        service.deleteLivreur(id);
        return ResponseEntity.noContent().build();
    }
}
