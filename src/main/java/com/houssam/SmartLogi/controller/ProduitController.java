package com.houssam.SmartLogi.controller;

import com.houssam.SmartLogi.dto.ProduitDTO;
import com.houssam.SmartLogi.response.ApiResponse;
import com.houssam.SmartLogi.service.ProduitService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/produits")
public class ProduitController {

    private final ProduitService service;

    public ProduitController(ProduitService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProduitDTO>> createProduit(@Valid @RequestBody ProduitDTO dto){
        ProduitDTO created = service.createProduit(dto);
        return ResponseEntity.ok(new ApiResponse("Produit créé avec succès", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProduitDTO>>> findAllProduits(
            @PageableDefault(size = 20, sort = "nom") Pageable pageable){
        Page<ProduitDTO> page = service.getAllProduits(pageable);
        return ResponseEntity.ok(new ApiResponse("Liste des produits récupérée avec succès", page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProduitDTO>> findProduitById(@PathVariable String id){
        ProduitDTO produit = service.getProduitById(id);
        if(produit == null){
            return ResponseEntity.status(404).body(new ApiResponse("Produit non trouvé", null));
        } else {
            return ResponseEntity.ok(new ApiResponse("Produit trouvé", produit));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProduitDTO>> updateProduit(@PathVariable String id, @Valid @RequestBody ProduitDTO dto){
        ProduitDTO updated = service.updateProduit(id, dto);
        return ResponseEntity.ok(new ApiResponse("Produit mis à jour avec succès", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduit(@PathVariable String id){
        service.deleteProduit(id);
        return ResponseEntity.ok(new ApiResponse("Produit supprimé avec succès", null));
    }
}
