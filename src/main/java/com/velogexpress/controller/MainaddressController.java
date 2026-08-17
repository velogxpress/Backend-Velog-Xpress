package com.velogexpress.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import com.velogexpress.model.MainaddressModel;
import com.velogexpress.service.MainaddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mainaddress")
public class MainaddressController {

    private final MainaddressService mainaddressService;

    // Create a new main address
    @CacheEvict(cacheNames = "mainaddress", allEntries = true)
    @PostMapping
    public ResponseEntity<MainaddressModel> createAddress(@RequestBody MainaddressModel model) {
        MainaddressModel created = mainaddressService.createAddress(model);
        return ResponseEntity.status(201).body(created);
    }

    // Get all addresses with pagination
    @Cacheable(cacheNames = "mainaddress")
    @GetMapping
    public ResponseEntity<Page<MainaddressModel>> getAddresses(Pageable pageable) {
        Page<MainaddressModel> addresses = mainaddressService.getAddress(pageable);
        return ResponseEntity.ok(addresses);
    }

    // Get an address by ID
    @Cacheable(cacheNames = "mainaddress")
    @GetMapping("/{id}")
    public ResponseEntity<MainaddressModel> getAddressById(@PathVariable Long id) {
        MainaddressModel model = mainaddressService.getAddressById(id);
        return ResponseEntity.ok(model);
    }

    // Update an address by ID
    @CacheEvict(cacheNames = "mainaddress", allEntries = true)
    @PutMapping("/{id}")
    public ResponseEntity<MainaddressModel> updateAddress(
            @PathVariable Long id,
            @RequestBody MainaddressModel model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        MainaddressModel updated = mainaddressService.updateAddress(id, model, PageRequest.of(page, size));
        return ResponseEntity.ok(updated);
    }
}
