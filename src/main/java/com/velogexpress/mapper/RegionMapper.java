package com.velogexpress.mapper;

import com.velogexpress.entity.Region;
import com.velogexpress.model.RegionModel;

public class RegionMapper {
    public static RegionModel mapToRegionModel(Region region){
        return new RegionModel(
                region.getId(),
                region.getDescription()
        );
    }

    public static Region mapToRegion(RegionModel regionModel){
        return new Region(regionModel.getId(), regionModel.getDescription());
    }
}
