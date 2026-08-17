package com.velogexpress.controller;

import com.velogexpress.entity.StorageDetails;
import com.velogexpress.model.StorageDetailsModel;
import com.velogexpress.service.StorageDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/storagedetails")
public class StorageDetailsController {
    private final StorageDetailsService storageDetailsService;

    @PostMapping("/create")
    public ResponseEntity<StorageDetailsModel> saveStorageDetails(@RequestBody StorageDetailsModel storageDetails) {
        StorageDetailsModel model=storageDetailsService.create(storageDetails);
        return new ResponseEntity<>(model, HttpStatus.CREATED);
    }

    @GetMapping("{order}")
    public ResponseEntity<List<StorageDetailsModel>> getAllStorageDetails(@PathVariable String order) {
        List<StorageDetailsModel> model=storageDetailsService.getStorageDetails(order);
        return ResponseEntity.ok(model);
    }

    @GetMapping("/searching/{order}")
    public ResponseEntity<List<StorageDetailsModel>> getAllStorageDetails(@PathVariable("order") String order, @RequestParam String search) {
        List<StorageDetailsModel> model=storageDetailsService.getStorageDetails(order,search);
        return ResponseEntity.ok(model);
    }

    @GetMapping("/findstoragedetails/{search}")
    public ResponseEntity<List<StorageDetailsModel>> getCountStorageDetails(@PathVariable("search") String search) {
        List<StorageDetailsModel> model=storageDetailsService.getCountStorageDetails(search);
        return ResponseEntity.ok(model);
    }

}
