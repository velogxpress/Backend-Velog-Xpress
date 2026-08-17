package com.velogexpress.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import com.velogexpress.model.RegionModel;
import com.velogexpress.service.RegionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

//@CrossOrigin("*")
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/region")
public class RegionController {

    private final RegionService regionService;

    // Create a new Region
    @CacheEvict(cacheNames = "region", allEntries = true)
    @PostMapping
    public ResponseEntity<RegionModel> createRegion(@Valid @RequestBody RegionModel regionModel) {
        log.info("Creating region: {}", regionModel.getDescription());
        RegionModel savedRegion = regionService.createRegion(regionModel);
        return new ResponseEntity<>(savedRegion, HttpStatus.CREATED);
    }

    // Get all Regions with pagination
    @Cacheable(cacheNames = "region")
    @GetMapping
    public ResponseEntity<Page<RegionModel>> getAllRegions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {

        log.debug("Fetching all regions, page: {}, size: {}", page, size);
        Page<RegionModel> regions = regionService.getAllRegion(PageRequest.of(page, size));
        return ResponseEntity.ok(regions);
    }

    // Get Region by description (using query parameter)
    @Cacheable(cacheNames = "region")
    @GetMapping("/search")
    public ResponseEntity<RegionModel> getRegionByDescription(@RequestParam String description) {
        log.debug("Fetching region by description: {}", description);
        RegionModel region = regionService.getRegionByDescription(description);
        return ResponseEntity.ok(region);
    }

    // Get Region by id (using query parameter)
    @Cacheable(cacheNames = "region")
    @GetMapping("/id/{id}")
    public ResponseEntity<RegionModel> getRegionById(@PathVariable("id") Long id) {
        log.debug("Fetching region by id: {}", id);
        RegionModel region = regionService.getRegionById(id);
        return ResponseEntity.ok(region);
    }

    // Update a Region
    @CacheEvict(cacheNames = "region", allEntries = true)
    @PutMapping("{id}")
    public ResponseEntity<RegionModel> updateRegion(
            @PathVariable("id") Long regionId,
            @Valid @RequestBody RegionModel regionModel) {

        log.info("Updating region with ID: {}", regionId);
        RegionModel updatedRegion = regionService.updateRegion(regionId, regionModel);
        return ResponseEntity.ok(updatedRegion);
    }

    // Delete a Region
    @CacheEvict(cacheNames = "region", allEntries = true)
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteRegion(@PathVariable("id") Long regionId) {
        log.warn("Deleting region with ID: {}", regionId);
        regionService.deleteRegion(regionId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // Get all Regions with pagination
    @Cacheable(cacheNames = "region")
    @GetMapping("/limitedregion")
    public ResponseEntity<Page<RegionModel>> findAllRegions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        Page<RegionModel> regions = regionService.findAllRegions(PageRequest.of(page, size));
        return ResponseEntity.ok(regions);
    }
}
