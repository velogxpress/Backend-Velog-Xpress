package com.velogexpress.mapper;

import com.velogexpress.entity.Machine;
import com.velogexpress.model.MachineModel;

public class MachineMapper {
    public static MachineModel mapToMachineModel(Machine machine){
        return new MachineModel(
                machine.getId(),
                machine.getName(),
                machine.getMarque(),
                machine.getDescription(),
                machine.getSerial()
        );
    }

    public static Machine mapToMachine(MachineModel machineModel){
        return new Machine(
                machineModel.getId(),
                machineModel.getName(),
                machineModel.getMarque(),
                machineModel.getDescription(),
                machineModel.getSerial()
        );
    }
}
