package com.velogexpress.service.impl;

import com.velogexpress.entity.Region;
import com.velogexpress.exception.RessourceNotFoundException;
import com.velogexpress.mapper.RegionMapper;
import com.velogexpress.model.RegionModel;
import com.velogexpress.repository.RegionRepository;
import com.velogexpress.service.RegionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    @Override
    public RegionModel createRegion(RegionModel regionModel) {
        log.info("Creating new region: {}", regionModel.getDescription());
        Region region = RegionMapper.mapToRegion(regionModel);
        Region savedRegion = regionRepository.save(region);
        return RegionMapper.mapToRegionModel(savedRegion);
    }

    @Override
    public Page<RegionModel> getAllRegion(Pageable pageable) {
        log.debug("Fetching all regions with pagination: {}", pageable);
        return regionRepository.findAll(pageable)
                .map(RegionMapper::mapToRegionModel);
    }

    @Override
    public RegionModel getRegionById(Long regionId) {
        log.debug("Fetching region by ID: {}", regionId);
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new RessourceNotFoundException("Region does not exist with id: " + regionId));
        return RegionMapper.mapToRegionModel(region);
    }

    @Override
    public RegionModel getRegionByDescription(String description) {
        log.debug("Fetching region by description: {}", description);
        Region region = regionRepository.findByDescription(description);
        if (region == null) {
            throw new RessourceNotFoundException("Region not found with description: " + description);
        }
        return RegionMapper.mapToRegionModel(region);
    }

    @Transactional
    @Override
    public RegionModel updateRegion(Long regionId, RegionModel regionModel) {
        log.info("Updating region with ID: {}", regionId);
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new RessourceNotFoundException("Region does not exist with id: " + regionId));

        region.setDescription(regionModel.getDescription());
        Region updatedRegion = regionRepository.save(region);

        return RegionMapper.mapToRegionModel(updatedRegion);
    }

    @Transactional
    @Override
    public void deleteRegion(Long regionId) {
        log.warn("Deleting region with ID: {}", regionId);
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new RessourceNotFoundException("Region does not exist with id: " + regionId));
        regionRepository.delete(region);
    }

    @Override
    public Page<RegionModel> findAllRegions(Pageable pageable) {
        Page<Region> regions = regionRepository.findByAllRegion(pageable);
        return regions.map(RegionMapper::mapToRegionModel);
    }
}
