package com.velogexpress.controller;

import com.velogexpress.model.VilleModel;
import com.velogexpress.service.VilleService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

//@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/ville")
public class VilleController {

    private final VilleService villeService;

    // ✅ Create Ville
    @PostMapping
    public ResponseEntity<VilleModel> createVille(@Valid @RequestBody VilleModel villeModel) {
        VilleModel savedVille = villeService.createVille(villeModel);
        return new ResponseEntity<>(savedVille, HttpStatus.CREATED);
    }

    // ✅ Get all Villes
    @GetMapping
    public ResponseEntity<Page<VilleModel>> getAllVille(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Page<VilleModel> villeModels = villeService.getAllVille(page, size);
        return ResponseEntity.ok(villeModels);
    }


    // ✅ Get Villes by Region (with pagination)
    @GetMapping("/region/{region}")
    public ResponseEntity<Page<VilleModel>> getAllVilleByRegion(
            @PathVariable Long region,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Page<VilleModel> villePage = villeService.getVilleByRegion(region, PageRequest.of(page, size));
        return ResponseEntity.ok(villePage);
    }

    // ✅ Get Ville by ID
    @GetMapping("/{id}")
    public ResponseEntity<VilleModel> getVilleById(@PathVariable Long id) {
        VilleModel villeModel = villeService.getVilleByID(id);
        return ResponseEntity.ok(villeModel);
    }

    // ✅ Update Ville
    @PutMapping("/{id}")
    public ResponseEntity<VilleModel> updateVille(@PathVariable Long id, @Valid @RequestBody VilleModel villeModel) {
        VilleModel updatedVille = villeService.updateVille(id, villeModel);
        return ResponseEntity.ok(updatedVille);
    }

    // ✅ Delete Ville
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVille(@PathVariable Long id) {
        villeService.deleteVille(id);
        return ResponseEntity.noContent().build();
    }
}
