package com.velogexpress.controller;

import com.velogexpress.model.SurcmachineModel;
import com.velogexpress.service.SurcmachineService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

//@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/surcmachine")
public class SurcmachineController {

    private final SurcmachineService surcmachineService;

    // ✅ Create Surcmachine
    @PostMapping
    public ResponseEntity<SurcmachineModel> createSurcmachine(@Valid @RequestBody SurcmachineModel model) {
        SurcmachineModel saved = surcmachineService.createSurcmachine(model);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // ✅ Get all Surcmachines (paginated)
    @GetMapping
    public ResponseEntity<Page<SurcmachineModel>> getAllSurcmachine(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Page<SurcmachineModel> result = surcmachineService.getAllSurcmachine(PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }

    // ✅ Search Surcmachines by serial or name (paginated)
    @GetMapping("/search")
    public ResponseEntity<Page<SurcmachineModel>> searchSurcmachines(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Page<SurcmachineModel> result = surcmachineService.searchSurcmachines(query, PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }

    // ✅ Get a Surcmachine by machine serial
    @GetMapping("/machine/{serial}")
    public ResponseEntity<SurcmachineModel> getSurcmachineBySerial(@PathVariable String serial) {
        SurcmachineModel model = surcmachineService.getSurcmachineByMachineSerial(serial);
        if (model == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(model);
    }

    // ✅ Update Surcmachine by machine serial
    @PutMapping("/machine/{serial}")
    public ResponseEntity<SurcmachineModel> updateSurcmachine(
            @PathVariable String serial,
            @Valid @RequestBody SurcmachineModel model
    ) {
        SurcmachineModel updated = surcmachineService.updateSurcmachine(serial, model);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    // ✅ Delete Surcmachine by machine serial
    @DeleteMapping("/machine/{serial}")
    public ResponseEntity<Void> deleteSurcmachine(@PathVariable String serial) {
        surcmachineService.deleteSurcmachine(serial);
        return ResponseEntity.noContent().build();
    }

    // ✅ Search Surcmachines by Surcursal (paginated)
    @GetMapping("/surcursal/{surcursal}")
    public ResponseEntity<Page<SurcmachineModel>> getSurcmachinesBySurcursal(
            @PathVariable("surcursal") String surcursal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Page<SurcmachineModel> result = surcmachineService.searchSurcmachines(surcursal, PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }

}
