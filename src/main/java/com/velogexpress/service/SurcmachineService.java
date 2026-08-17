package com.velogexpress.service;

import com.velogexpress.model.SurcmachineModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SurcmachineService {

    // Create a new Surcmachine
    SurcmachineModel createSurcmachine(SurcmachineModel surcmachineModel);

    // Get all Surcmachines with pagination
    Page<SurcmachineModel> getAllSurcmachine(Pageable pageable);

    // Search Surcmachines by serial or name with pagination
    Page<SurcmachineModel> searchSurcmachines(String query, Pageable pageable);

    // Get a Surcmachine by its machine serial
    SurcmachineModel getSurcmachineByMachineSerial(String serial);

    // Update a Surcmachine by its machine serial
    SurcmachineModel updateSurcmachine(String machineSerial, SurcmachineModel surcmachineModel);

    // Delete a Surcmachine by its machine serial
    void deleteSurcmachine(String machineSerial);
}
