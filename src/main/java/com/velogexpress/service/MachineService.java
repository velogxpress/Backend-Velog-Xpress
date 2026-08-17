package com.velogexpress.service;

import com.velogexpress.model.MachineModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MachineService {
    MachineModel createMachine(MachineModel machineModel);
    Page<MachineModel> getAllMachine(Pageable pageable);
    Page<MachineModel> getMachineByParam(String serial, Pageable pageable);
    MachineModel getMachineBySerial(String serial);
    MachineModel updateMachine(String serial,MachineModel machineModel);
    void deleteMachine(String serial);
}
