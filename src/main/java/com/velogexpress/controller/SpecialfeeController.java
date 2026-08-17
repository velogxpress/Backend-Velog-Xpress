package com.velogexpress.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import com.velogexpress.model.SpecialfeeModel;
import com.velogexpress.service.SpecialfeeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/specialfees")
public class SpecialfeeController {

    private final SpecialfeeService specialfeeService;

    // Create Special Fee
    @CacheEvict(cacheNames = "specialfee", allEntries = true)
    @PostMapping
    public ResponseEntity<SpecialfeeModel> createSpecialfee(@RequestBody SpecialfeeModel specialfeeModel) {
        SpecialfeeModel created = specialfeeService.createSpecialfee(specialfeeModel);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Get All Special Fees
    @Cacheable(cacheNames = "specialfee")
    @GetMapping
    public ResponseEntity<Page<SpecialfeeModel>> getAllSpecialfees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Page<SpecialfeeModel> list = specialfeeService.getAllSpecialfee(PageRequest.of(page, size));
        return ResponseEntity.ok(list);
    }

    // Get Special Fee by ID
    @Cacheable(cacheNames = "specialfee")
    @GetMapping("/{id}")
    public ResponseEntity<SpecialfeeModel> getSpecialfeeById(@PathVariable("id") Long id) {
        SpecialfeeModel model = specialfeeService.getSpecialfeeById(id);
        return ResponseEntity.ok(model);
    }

    // Get Special Fee by Amount
    @Cacheable(cacheNames = "specialfee")
    @GetMapping("/by-amount")
    public ResponseEntity<Page<SpecialfeeModel>> getSpecialfeeByAmount(
            @RequestParam("amount") Double amount,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Page<SpecialfeeModel> list = specialfeeService.getSpecialfeeByAmount(amount, PageRequest.of(page, size));
        return ResponseEntity.ok(list);
    }

    // Update Special Fee
    @CacheEvict(cacheNames = "specialfee", allEntries = true)
    @PutMapping("/{id}")
    public ResponseEntity<SpecialfeeModel> updateSpecialfee(
            @PathVariable("id") Long id,
            @RequestBody SpecialfeeModel specialfeeModel
    ) {
        SpecialfeeModel updated = specialfeeService.updateSpecialfee(id, specialfeeModel);
        return ResponseEntity.ok(updated);
    }

    // Delete Special Fee
    @CacheEvict(cacheNames = "specialfee", allEntries = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSpecialfee(@PathVariable("id") Long id) {
        specialfeeService.deleteSpecialfee(id);
        return ResponseEntity.ok("Special fee deleted successfully.");
    }
}
