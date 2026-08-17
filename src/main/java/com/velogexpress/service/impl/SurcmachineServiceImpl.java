package com.velogexpress.service.impl;

import com.velogexpress.entity.Machine;
import com.velogexpress.entity.Surcmachine;
import com.velogexpress.entity.Surcursal;
import com.velogexpress.mapper.SurcmachineMapper;
import com.velogexpress.model.SurcmachineModel;
import com.velogexpress.repository.MachineRepository;
import com.velogexpress.repository.SurcmachineRepository;
import com.velogexpress.repository.SurcursalRepository;
import com.velogexpress.service.SurcmachineService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SurcmachineServiceImpl implements SurcmachineService {

    private final SurcmachineRepository surcmachineRepository;
    private final SurcursalRepository surcursalRepository;
    private final MachineRepository machineRepository;

    @Override
    public SurcmachineModel createSurcmachine(SurcmachineModel model) {
        Surcmachine entity = SurcmachineMapper.mapToSurcmachine(model);
        Surcmachine saved = surcmachineRepository.save(entity);
        return SurcmachineMapper.mapToSurcmachineModel(saved);
    }

    @Override
    public Page<SurcmachineModel> getAllSurcmachine(Pageable pageable) {
        return surcmachineRepository.findAll(pageable)
                .map(SurcmachineMapper::mapToSurcmachineModel);
    }

    @Override
    public Page<SurcmachineModel> searchSurcmachines(String query, Pageable pageable) {
        return surcmachineRepository.search(query, pageable)
                .map(SurcmachineMapper::mapToSurcmachineModel);
    }

    @Override
    public SurcmachineModel getSurcmachineByMachineSerial(String serial) {
        Surcmachine entity = surcmachineRepository.findByMachine(serial);
        return entity != null ? SurcmachineMapper.mapToSurcmachineModel(entity) : null;
    }

    @Override
    public SurcmachineModel updateSurcmachine(String machineSerial, SurcmachineModel model) {
        Surcmachine entity = surcmachineRepository.findByMachine(machineSerial);
        if (entity == null) return null;

        // Fetch Surcursal
        Surcursal surcursal = surcursalRepository.findByName(model.getSurcursal().getName());
        if (surcursal == null) return null;

        // Fetch Machine
        Optional<Machine> machineOpt = machineRepository.findById(model.getMachine().getId());
        if (machineOpt.isEmpty()) return null;

        entity.setSurcursal(surcursal);
        entity.setMachine(machineOpt.get());

        Surcmachine saved = surcmachineRepository.save(entity);
        return SurcmachineMapper.mapToSurcmachineModel(saved);
    }

    @Override
    public void deleteSurcmachine(String machineSerial) {
        Surcmachine entity = surcmachineRepository.findByMachine(machineSerial);
        if (entity != null) {
            surcmachineRepository.delete(entity);
        }
    }
}
