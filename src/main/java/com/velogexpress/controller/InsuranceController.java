package com.velogexpress.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import com.velogexpress.model.InsuranceModel;
import com.velogexpress.service.InsuranceService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/insurance")
public class InsuranceController {

    private final InsuranceService insuranceService;

    @CacheEvict(cacheNames = "insurance", allEntries = true)
    @PostMapping
    public ResponseEntity<InsuranceModel> createInsurance(@RequestBody InsuranceModel insuranceModel) {
        InsuranceModel created = insuranceService.createInsurance(insuranceModel);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Cacheable(cacheNames = "insurance")
    @GetMapping
    public ResponseEntity<Page<InsuranceModel>> getAllInsurance(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        Page<InsuranceModel> result = insuranceService.getAllInsurance(PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }

    @Cacheable(cacheNames = "insurance")
    @GetMapping("/{id}")
    public ResponseEntity<InsuranceModel> getInsuranceById(@PathVariable("id") Long id) {
        InsuranceModel model = insuranceService.getInsuranceById(id);
        return ResponseEntity.ok(model);
    }

    @Cacheable(cacheNames = "insurance")
    @GetMapping("/by-amount")
    public ResponseEntity<Page<InsuranceModel>> getInsuranceByAmount(
            @RequestParam("amount") Double amount,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        Page<InsuranceModel> pageResult = insuranceService.getInsuranceByAmount(amount, PageRequest.of(page, size));
        return ResponseEntity.ok(pageResult);
    }

    @CacheEvict(cacheNames = "insurance", allEntries = true)
    @PutMapping("/{id}")
    public ResponseEntity<InsuranceModel> updateInsurance(
            @PathVariable("id") Long id,
            @RequestBody InsuranceModel insuranceModel) {
        InsuranceModel updated = insuranceService.updateInsurance(id, insuranceModel);
        return ResponseEntity.ok(updated);
    }

    @CacheEvict(cacheNames = "insurance", allEntries = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsurance(@PathVariable("id") Long id) {
        insuranceService.deleteInsurance(id);
        return ResponseEntity.noContent().build();
    }
}
