package com.velogexpress.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import com.velogexpress.model.CipinfeeModel;
import com.velogexpress.service.CipinfeeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/cipinfees")
public class CipinfeeController {

    private final CipinfeeService cipinfeeService;

    // CREATE
    @CacheEvict(cacheNames = "cipinfee", allEntries = true)
    @PostMapping
    public ResponseEntity<CipinfeeModel> createCipinfee(@RequestBody CipinfeeModel cipinfeeModel) {
        CipinfeeModel model = cipinfeeService.createCipinfee(cipinfeeModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    // GET ALL (paged)
    @Cacheable(cacheNames = "cipinfee")
    @GetMapping
    public ResponseEntity<Page<CipinfeeModel>> getAllCipinfee(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {

        Page<CipinfeeModel> modelList = cipinfeeService.getAllCipinfee(PageRequest.of(page, size));
        return ResponseEntity.ok(modelList);
    }

    @Cacheable(cacheNames = "cipinfee")
    @GetMapping("/search/{city}")
    public ResponseEntity<Page<CipinfeeModel>> getAllCipinfeeByCity(
            @PathVariable("city") String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {

        Page<CipinfeeModel> modelList = cipinfeeService.getCipinfeeByCity(city,PageRequest.of(page, size));
        return ResponseEntity.ok(modelList);
    }

    // GET BY ID
    @Cacheable(cacheNames = "cipinfee")
    @GetMapping("{id}")
    public ResponseEntity<CipinfeeModel> getCipinfeeById(@PathVariable Long id) {
        CipinfeeModel model = cipinfeeService.getCipinfeeById(id);
        return ResponseEntity.ok(model);
    }

    // UPDATE BY ID
    @CacheEvict(cacheNames = "cipinfee", allEntries = true)
    @PutMapping("{id}")
    public ResponseEntity<CipinfeeModel> updateCipinfee(
            @PathVariable Long id,
            @RequestBody CipinfeeModel cipinfeeModel) {

        CipinfeeModel updated = cipinfeeService.updateCipinfeeById(id, cipinfeeModel);
        return ResponseEntity.ok(updated);
    }

    // DELETE
    @CacheEvict(cacheNames = "cipinfee", allEntries = true)
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCipinfee(@PathVariable Long id) {
        cipinfeeService.deleteCipinfee(id);
        return ResponseEntity.ok("City vs fees deleted successfully.");
    }

    //Build get City Vs Fees By ID
    @Cacheable(cacheNames = "cipinfee")
    @GetMapping("/feesfound/{id}")
    public ResponseEntity<CipinfeeModel> getCipinfeeModelById(@PathVariable("id") Long id){
        CipinfeeModel model=cipinfeeService.getCipinfeeByCity(id);
        return ResponseEntity.ok(model);
    }
}
