package com.velogexpress.service;

import com.velogexpress.model.RegionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface RegionService {
    RegionModel createRegion(RegionModel regionModel);
    Page<RegionModel> getAllRegion(Pageable pageable);
    RegionModel getRegionById(Long regionId);
    RegionModel getRegionByDescription(String description);
    RegionModel updateRegion(Long regionId,RegionModel regionModel);
    void deleteRegion(Long regionId);
    Page<RegionModel> findAllRegions(Pageable pageable);
}
