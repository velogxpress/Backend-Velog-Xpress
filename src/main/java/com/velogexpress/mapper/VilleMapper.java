package com.velogexpress.mapper;

import com.velogexpress.entity.Ville;
import com.velogexpress.model.VilleModel;

public class VilleMapper {
    public static VilleModel mapToVilleModel(Ville ville){
        return new VilleModel(
                ville.getId(),
                ville.getDescription(),
                ville.getAbreger(),
                RegionMapper.mapToRegionModel(ville.getRegion())
        );
    }

    public static Ville mapToVille(VilleModel villeModel){
        return new Ville(
                villeModel.getId(),
                villeModel.getDescription(),
                villeModel.getAbreger(),
                RegionMapper.mapToRegion(villeModel.getRegion())
        );
    }
}
