package com.velogexpress.controller;

import com.velogexpress.model.StoreModel;
import com.velogexpress.service.StoreService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/store")
public class StoreController {
    private StoreService storeService;

    @PostMapping
    public ResponseEntity<StoreModel> createStore(@RequestBody StoreModel storeModel) {
        StoreModel saveObj = storeService.createStore(storeModel);
        return new  ResponseEntity<>(saveObj, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<StoreModel>> getStores() {
        List<StoreModel> storeModels = storeService.getStores();
        return ResponseEntity.ok(storeModels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreModel> getStore(@PathVariable Long id) {
        StoreModel storeModel = storeService.getStore(id);
        return ResponseEntity.ok(storeModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoreModel> updateStore(@PathVariable Long id, @RequestBody StoreModel storeModel) {
        StoreModel saveObj = storeService.updateStore(id, storeModel);
        return ResponseEntity.ok(saveObj);
    }

}
