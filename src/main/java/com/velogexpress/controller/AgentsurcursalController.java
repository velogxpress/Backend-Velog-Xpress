package com.velogexpress.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import com.velogexpress.model.AgentsurcursalModel;
import com.velogexpress.service.AgentsurcursalService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


//@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/agentsurcursal")
public class AgentsurcursalController {

    private final AgentsurcursalService agentsurcursalService;

    // Create Agentsurcursal
    @CacheEvict(cacheNames = "agentsurcursal", allEntries = true)
    @PostMapping
    public ResponseEntity<AgentsurcursalModel> create(@RequestBody AgentsurcursalModel model) {
        AgentsurcursalModel created = agentsurcursalService.createAgentsurcursal(model);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Get all Agentsurcursals (paginated)
    @Cacheable(cacheNames = "agentsurcursal")
    @GetMapping
    public ResponseEntity<Page<AgentsurcursalModel>> getAll(Pageable pageable) {
        return ResponseEntity.ok(agentsurcursalService.getAllAgentsurcursals(pageable));
    }

    // Search by userCode (paginated)
    @Cacheable(cacheNames = "agentsurcursal")
    @GetMapping("/search/{userCode}")
    public ResponseEntity<Page<AgentsurcursalModel>> searchByUserCode(
            @PathVariable String userCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Page<AgentsurcursalModel> result = agentsurcursalService.searchByUserCode(userCode, PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }

    // Get by userCode
    @Cacheable(cacheNames = "agentsurcursal")
    @GetMapping("/{userCode}")
    public ResponseEntity<AgentsurcursalModel> getByUserCode(@PathVariable String userCode) {
        return ResponseEntity.ok(agentsurcursalService.getByUserCode(userCode));
    }

    // Update by userCode
    @CacheEvict(cacheNames = "agentsurcursal", allEntries = true)
    @PutMapping("/{userCode}")
    public ResponseEntity<AgentsurcursalModel> update(
            @PathVariable String userCode,
            @RequestBody AgentsurcursalModel model
    ) {
        return ResponseEntity.ok(agentsurcursalService.updateByUserCode(userCode, model));
    }

    // Delete by userCode
    @CacheEvict(cacheNames = "agentsurcursal", allEntries = true)
    @DeleteMapping("/{userCode}")
    public ResponseEntity<String> delete(@PathVariable String userCode) {
        agentsurcursalService.deleteByUserCode(userCode);
        return ResponseEntity.ok("Agentsurcursal deleted successfully");
    }

    //Build get agent by usercode
    @Cacheable(cacheNames = "agentsurcursal")
    @GetMapping("/agentsurcursalsearch/{usercode}")
    public ResponseEntity<Page> getAgentsurcursalByUsercode(@PathVariable("usercode") String usercode,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "25") int size
                                                            ){
        Page<AgentsurcursalModel>  model=agentsurcursalService.searchByUserCode(usercode, PageRequest.of(page, size));
        return ResponseEntity.ok(model);
    }
}
