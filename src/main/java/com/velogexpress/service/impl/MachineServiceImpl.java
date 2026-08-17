package com.velogexpress.service.impl;

import com.velogexpress.entity.Machine;
import com.velogexpress.mapper.MachineMapper;
import com.velogexpress.model.MachineModel;
import com.velogexpress.repository.MachineRepository;
import com.velogexpress.service.MachineService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MachineServiceImpl implements MachineService {

    private final MachineRepository machineRepository;

    @Override
    public MachineModel createMachine(MachineModel machineModel) {
        Machine machine = MachineMapper.mapToMachine(machineModel);
        Machine saved = machineRepository.save(machine);
        return MachineMapper.mapToMachineModel(saved);
    }

    @Override
    public Page<MachineModel> getAllMachine(Pageable pageable) {
        return machineRepository.findAll(pageable)
                .map(MachineMapper::mapToMachineModel);
    }

    @Override
    public Page<MachineModel> getMachineByParam(String keyword, Pageable pageable) {
        return machineRepository.search(keyword, pageable)
                .map(MachineMapper::mapToMachineModel);
    }

    @Override
    public MachineModel getMachineBySerial(String serial) {
        return machineRepository.findBySerial(serial)
                .map(MachineMapper::mapToMachineModel)
                .orElse(null);
    }

    @Override
    public MachineModel updateMachine(String serial, MachineModel machineModel) {
        Optional<Machine> optionalMachine = machineRepository.findBySerial(serial);
        if (optionalMachine.isEmpty()) {
            return null;
        }

        Machine machine = optionalMachine.get();
        machine.setName(machineModel.getName());
        machine.setMarque(machineModel.getMarque());
        machine.setDescription(machineModel.getDescription());
        machine.setSerial(machineModel.getSerial());

        Machine updated = machineRepository.save(machine);
        return MachineMapper.mapToMachineModel(updated);
    }

    @Override
    public void deleteMachine(String serial) {
        machineRepository.findBySerial(serial)
                .ifPresent(machineRepository::delete);
    }
}
