package com.velogexpress.mapper;

import com.velogexpress.entity.Surcursal;
import com.velogexpress.model.SurcursalModel;

public class SurcursalMapper {
    public static SurcursalModel mapToSurcursalModel(Surcursal surcursal){
        return new SurcursalModel(
                surcursal.getId(),
                surcursal.getName(),
                surcursal.getAddress(),
                VilleMapper.mapToVilleModel(surcursal.getVille()),
                surcursal.getPhone(),
                surcursal.getHoraire()
        );
    }

    public static Surcursal mapToSurcursal(SurcursalModel surcursalModel){
        return new Surcursal(
                surcursalModel.getId(),
                surcursalModel.getName(),
                surcursalModel.getAddress(),
                VilleMapper.mapToVille(surcursalModel.getVille()),
                surcursalModel.getPhone(),
                surcursalModel.getHoraire()
        );
    }

}
