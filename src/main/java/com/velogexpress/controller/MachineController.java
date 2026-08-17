package com.velogexpress.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import com.velogexpress.model.MachineModel;
import com.velogexpress.service.MachineService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/machines")
//@CrossOrigin("*")
@RequiredArgsConstructor
public class MachineController {

    private final MachineService machineService;

    /** ✅ Create a new machine */
    @CacheEvict(cacheNames = "machine", allEntries = true)
    @PostMapping
    public ResponseEntity<MachineModel> createMachine(@RequestBody MachineModel machineModel) {
        MachineModel created = machineService.createMachine(machineModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /** ✅ Get all machines with pagination */
    @Cacheable(cacheNames = "machine")
    @GetMapping
    public ResponseEntity<Page<MachineModel>> getAllMachines(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MachineModel> machines = machineService.getAllMachine(pageable);
        return ResponseEntity.ok(machines);
    }

    /** ✅ Get one machine by serial */
    @Cacheable(cacheNames = "machine")
    @GetMapping("/{serial}")
    public ResponseEntity<MachineModel> getMachineBySerial(@PathVariable String serial) {
        MachineModel machine = machineService.getMachineBySerial(serial);
        if (machine == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(machine);
    }

    /** ✅ Update a machine by serial */
    @CacheEvict(cacheNames = "machine", allEntries = true)
    @PutMapping("/{serial}")
    public ResponseEntity<MachineModel> updateMachine(
            @PathVariable String serial,
            @RequestBody MachineModel machineModel
    ) {
        MachineModel updated = machineService.updateMachine(serial, machineModel);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    /** ✅ Delete a machine by serial */
    @CacheEvict(cacheNames = "machine", allEntries = true)
    @DeleteMapping("/{serial}")
    public ResponseEntity<Void> deleteMachine(@PathVariable String serial) {
        machineService.deleteMachine(serial);
        return ResponseEntity.noContent().build();
    }

    /** ✅ Search machines by keyword (serial, name, marque) */
    @Cacheable(cacheNames = "machine")
    @GetMapping("/search")
    public ResponseEntity<Page<MachineModel>> searchMachines(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MachineModel> results = machineService.getMachineByParam(keyword, pageable);
        return ResponseEntity.ok(results);
    }
}
