package com.velogexpress.controller;

import com.velogexpress.model.CipinfeeModel;
import com.velogexpress.service.CipinfeeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/cipinfees")
public class CipinfeeController {

    private final CipinfeeService cipinfeeService;

    // CREATE
    @PostMapping
    public ResponseEntity<CipinfeeModel> createCipinfee(@RequestBody CipinfeeModel cipinfeeModel) {
        CipinfeeModel model = cipinfeeService.createCipinfee(cipinfeeModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    // GET ALL (paged)
    @GetMapping
    public ResponseEntity<Page<CipinfeeModel>> getAllCipinfee(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {

        Page<CipinfeeModel> modelList = cipinfeeService.getAllCipinfee(PageRequest.of(page, size));
        return ResponseEntity.ok(modelList);
    }

    @GetMapping("/search/{city}")
    public ResponseEntity<Page<CipinfeeModel>> getAllCipinfeeByCity(
            @PathVariable("city") String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {

        Page<CipinfeeModel> modelList = cipinfeeService.getCipinfeeByCity(city,PageRequest.of(page, size));
        return ResponseEntity.ok(modelList);
    }

    // GET BY ID
    @GetMapping("{id}")
    public ResponseEntity<CipinfeeModel> getCipinfeeById(@PathVariable Long id) {
        CipinfeeModel model = cipinfeeService.getCipinfeeById(id);
        return ResponseEntity.ok(model);
    }

    // UPDATE BY ID
    @PutMapping("{id}")
    public ResponseEntity<CipinfeeModel> updateCipinfee(
            @PathVariable Long id,
            @RequestBody CipinfeeModel cipinfeeModel) {

        CipinfeeModel updated = cipinfeeService.updateCipinfeeById(id, cipinfeeModel);
        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCipinfee(@PathVariable Long id) {
        cipinfeeService.deleteCipinfee(id);
        return ResponseEntity.ok("City vs fees deleted successfully.");
    }

    //Build get City Vs Fees By ID
    @GetMapping("/feesfound/{id}")
    public ResponseEntity<CipinfeeModel> getCipinfeeModelById(@PathVariable("id") Long id){
        CipinfeeModel model=cipinfeeService.getCipinfeeByCity(id);
        return ResponseEntity.ok(model);
    }
}
