package com.velogexpress.mapper;

import com.velogexpress.entity.Surcmachine;
import com.velogexpress.model.SurcmachineModel;

public class SurcmachineMapper {
    public static SurcmachineModel mapToSurcmachineModel(Surcmachine surcmachine){
        return new SurcmachineModel(
                surcmachine.getId(),
                SurcursalMapper.mapToSurcursalModel(surcmachine.getSurcursal()),
                MachineMapper.mapToMachineModel(surcmachine.getMachine())
        );
    }

    public static Surcmachine mapToSurcmachine(SurcmachineModel surcmachineModel){
        return new Surcmachine(
                surcmachineModel.getId(),
                SurcursalMapper.mapToSurcursal(surcmachineModel.getSurcursal()),
                MachineMapper.mapToMachine(surcmachineModel.getMachine())
        );
    }
}
