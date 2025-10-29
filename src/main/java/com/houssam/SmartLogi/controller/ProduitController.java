package com.houssam.SmartLogi.controller;

import com.houssam.SmartLogi.dto.ProduitDTO;
import com.houssam.SmartLogi.response.ApiResponse;
import com.houssam.SmartLogi.service.ProduitService;
import jakarta.validation.Valid;
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
    public ResponseEntity<ApiResponse<List<ProduitDTO>>> findAllProduits(){
        List<ProduitDTO> liste = service.getAllProduits();
        return ResponseEntity.ok(new ApiResponse("Produits liste", liste));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProduitDTO>> findProduitById(@PathVariable Long id){
        ProduitDTO produit = service.getProduitById(id);
        if(produit == null){
            return ResponseEntity.status(404).body(new ApiResponse("Produit non trouvé", null));
        } else {
            return ResponseEntity.ok(new ApiResponse("Produit trouvé", produit));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduit(@PathVariable Long id){
        service.deleteProduit(id);
        return ResponseEntity.ok(new ApiResponse("Produit supprimé avec succès", null));
    }
}
