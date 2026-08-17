package com.velogexpress.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import com.velogexpress.model.FeepoundsModel;
import com.velogexpress.service.FeepoundsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feepounds")
public class FeepoundsController {

    private final FeepoundsService feepoundsService;

    // ✅ Create Fee
    @CacheEvict(cacheNames = "feepounds", allEntries = true)
    @PostMapping
    public ResponseEntity<FeepoundsModel> createFeepounds(@RequestBody FeepoundsModel feepoundsModel) {
        FeepoundsModel created = feepoundsService.createFee(feepoundsModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ✅ Get All Fees (Paginated)
    @Cacheable(cacheNames = "feepounds")
    @GetMapping
    public ResponseEntity<Page<FeepoundsModel>> getAllFeepounds(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        Page<FeepoundsModel> feeList = feepoundsService.getAllFee(PageRequest.of(page, size));
        return ResponseEntity.ok(feeList);
    }

    // ✅ Get Fee by ID
    @Cacheable(cacheNames = "feepounds")
    @GetMapping("/{id}")
    public ResponseEntity<FeepoundsModel> getFeepoundById(@PathVariable("id") Long id) {
        FeepoundsModel fee = feepoundsService.getFeeById(id);
        return ResponseEntity.ok(fee);
    }

    // ✅ Get Fees by Amount (Optional filter endpoint)
    @Cacheable(cacheNames = "feepounds")
    @GetMapping("/search")
    public ResponseEntity<Page<FeepoundsModel>> getFeepoundsByAmount(
            @RequestParam("amount") Double amount,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        Page<FeepoundsModel> fees = feepoundsService.getFeeByAmount(amount, PageRequest.of(page, size));
        return ResponseEntity.ok(fees);
    }

    // ✅ Update Fee
    @CacheEvict(cacheNames = "feepounds", allEntries = true)
    @PutMapping("/{id}")
    public ResponseEntity<FeepoundsModel> updateFeepounds(
            @PathVariable("id") Long id,
            @RequestBody FeepoundsModel feeModel) {
        FeepoundsModel updated = feepoundsService.updateFee(id, feeModel);
        return ResponseEntity.ok(updated);
    }

    // ✅ Delete Fee
    @CacheEvict(cacheNames = "feepounds", allEntries = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFeepounds(@PathVariable("id") Long id) {
        feepoundsService.deleteFee(id);
        return ResponseEntity.ok("Fee pounds deleted successfully");
    }
}
