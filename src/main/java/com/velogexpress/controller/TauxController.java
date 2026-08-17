package com.velogexpress.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import com.velogexpress.model.TauxModel;
import com.velogexpress.service.TauxService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/taux")
@RequiredArgsConstructor
public class TauxController {

    private final TauxService tauxService;

    // ✅ Create a new Taux
    @CacheEvict(cacheNames = "taux", allEntries = true)
    @PostMapping
    public ResponseEntity<TauxModel> createTaux(@Valid @RequestBody TauxModel tauxModel) {
        TauxModel saved = tauxService.createTaux(tauxModel);
        return ResponseEntity.status(201).body(saved);
    }

    // ✅ Get all Taux with pagination
    @Cacheable(cacheNames = "taux")
    @GetMapping
    public ResponseEntity<Page<TauxModel>> getAllTaux(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {

        Page<TauxModel> tauxPage = tauxService.getAllTaux(PageRequest.of(page, size));
        if (tauxPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tauxPage);
    }

    // ✅ Get Taux by Devise with pagination
    @Cacheable(cacheNames = "taux")
    @GetMapping("/devise/{description}")
    public ResponseEntity<Page<TauxModel>> getTauxByDevise(
            @PathVariable String description,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {

        Page<TauxModel> tauxPage = tauxService.getTauxByDevise(description, PageRequest.of(page, size));
        if (tauxPage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tauxPage);
    }

    // ✅ Get Taux by Devise with pagination
    @Cacheable(cacheNames = "taux")
    @GetMapping("/gettaux/{description}")
    public ResponseEntity<TauxModel> getTaux(@PathVariable String description) {
        TauxModel tauxPage = tauxService.getTauxByDevise(description);
        return ResponseEntity.ok(tauxPage);
    }

    // ✅ Update Taux by Devise
    @CacheEvict(cacheNames = "taux", allEntries = true)
    @PutMapping("/{id}")
    public ResponseEntity<TauxModel> updateTaux(
            @PathVariable Long id,
            @Valid @RequestBody TauxModel tauxModel) {

        TauxModel updated = tauxService.updateTaux(id, tauxModel);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    // ✅ Delete Taux by Devise
    @CacheEvict(cacheNames = "taux", allEntries = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaux(@PathVariable Long id) {
        tauxService.deleteTaux(id);
        return ResponseEntity.noContent().build();
    }
}
