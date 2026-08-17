package com.velogexpress.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import com.velogexpress.model.SpecialfeeModel;
import com.velogexpress.model.StorageModel;
import com.velogexpress.service.StorageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/storage")
public class StorageController {
    private StorageService storageService;

    @CacheEvict(cacheNames = "storage", allEntries = true)
    @PostMapping
    public ResponseEntity<StorageModel> createStorage(@RequestBody StorageModel storageModel) {
        StorageModel created = storageService.createStorage(storageModel);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Get All Special Fees
    @Cacheable(cacheNames = "storage")
    @GetMapping
    public ResponseEntity<List> getAllStorage() {
        List<StorageModel> list = storageService.getAllStorage();
        return ResponseEntity.ok(list);
    }

    // Get All Special Fees
    @Cacheable(cacheNames = "storage")
    @GetMapping("/search/{param}")
    public ResponseEntity<List> getSearchStorage(@PathVariable String param) {
        List<StorageModel> list = storageService.getAllStorage(param);
        return ResponseEntity.ok(list);
    }

    // Get Special Fee by ID
    @Cacheable(cacheNames = "storage")
    @GetMapping("/{container}")
    public ResponseEntity<StorageModel> getStorageByContainer(@PathVariable("container") String container) {
        StorageModel model = storageService.getStorageByContainer(container);
        return ResponseEntity.ok(model);
    }

    @CacheEvict(cacheNames = "storage", allEntries = true)
    @PutMapping("/{id}")
    public ResponseEntity<StorageModel> updateStorage(@PathVariable("id") Long id, @RequestBody StorageModel storageModel) {
        StorageModel model = storageService.updateStorage(id, storageModel);
        return ResponseEntity.ok(model);
    }
}
