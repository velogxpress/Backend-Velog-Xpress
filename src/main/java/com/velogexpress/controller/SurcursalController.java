package com.velogexpress.controller;

import com.velogexpress.model.SurcursalModel;
import com.velogexpress.service.SurcursalService;
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
@RequestMapping("/api/surcursal")
public class SurcursalController {

    private final SurcursalService surcursalService;

    // ✅ Create Surcursal
    @PostMapping
    public ResponseEntity<SurcursalModel> createSurcursal(@Valid @RequestBody SurcursalModel surcursalModel) {
        SurcursalModel saved = surcursalService.createSurcursal(surcursalModel);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // ✅ Get all Surcursals (with pagination)
    @GetMapping
    public ResponseEntity<Page<SurcursalModel>> getAllSurcursal(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Page<SurcursalModel> result = surcursalService.getAllSurcursal(PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }

    // ✅ Get Surcursal by Name
    @GetMapping("/{name}")
    public ResponseEntity<SurcursalModel> getSurcursalByName(@PathVariable String name) {
        SurcursalModel model = surcursalService.getSurcursalByName(name);
        if (model == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(model);
    }

    // ✅ Search Surcursals (name, phone, city) with pagination
    @GetMapping("/search")
    public ResponseEntity<Page<SurcursalModel>> searchSurcursal(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Page<SurcursalModel> result = surcursalService.getSurcursal(query, PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }

    // ✅ Get Surcursals by Ville (city) with pagination
    @GetMapping("/ville/{ville}")
    public ResponseEntity<Page<SurcursalModel>> getSurcursalByVille(
            @PathVariable String ville,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Page<SurcursalModel> result = surcursalService.getSurcursal(ville, PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }

    // ✅ Update Surcursal
    @PutMapping("/{name}")
    public ResponseEntity<SurcursalModel> updateSurcursal(
            @PathVariable String name,
            @Valid @RequestBody SurcursalModel surcursalModel
    ) {
        SurcursalModel updated = surcursalService.updateSurcursal(name, surcursalModel);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    // ✅ Delete Surcursal
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteSurcursal(@PathVariable String name) {
        surcursalService.deleteSurcursal(name);
        return ResponseEntity.noContent().build();
    }

    //Build get All Surcursal
    @GetMapping("/surcursalsearch/{name}")
    public ResponseEntity<Page> getSurcursal(@PathVariable("name") String name,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "25") int size
                                             ){
        Page<SurcursalModel> modelPage=surcursalService.getSurcursal(name, PageRequest.of(page, size));
        return ResponseEntity.ok(modelPage);
    }
}
