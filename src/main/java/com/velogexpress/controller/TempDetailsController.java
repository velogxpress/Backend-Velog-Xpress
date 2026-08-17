package com.velogexpress.controller;

import com.velogexpress.model.TempDetailsModel;
import com.velogexpress.service.TempDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin("*")
@RestController
@RequestMapping("/api/tempdetails")
@RequiredArgsConstructor
public class TempDetailsController {

    private final TempDetailsService tempDetailsService;

    // ✅ Create a new TempDetails entry
    @PostMapping
    public ResponseEntity<TempDetailsModel> createDetails(@RequestBody TempDetailsModel tempDetailsModel) {
        TempDetailsModel saved = tempDetailsService.createFactureDetailsTMP(tempDetailsModel);
        return ResponseEntity.status(201).body(saved);
    }

    // ✅ Get all TempDetails for a client
    @GetMapping("/{client}")
    public ResponseEntity<Page<TempDetailsModel>> getAllDetailsByClient(
            @PathVariable String client,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        Page<TempDetailsModel> details = tempDetailsService.getAllFactureDetailsTMP(client, PageRequest.of(page, size));
        if (details.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(details);
    }

    // ✅ Get TempDetails by Facture ID (string identifier)
    @GetMapping("/facture/{factureId}")
    public ResponseEntity<TempDetailsModel> getDetailsByFactureId(@PathVariable String factureId) {
        TempDetailsModel model = tempDetailsService.getSingleFactureDetailsTMP(factureId);
        return (model != null) ? ResponseEntity.ok(model) : ResponseEntity.notFound().build();
    }

    // ✅ Get TempDetails by numeric ID
    @GetMapping("/single/{id}")
    public ResponseEntity<TempDetailsModel> getDetailsById(@PathVariable Long id) {
        TempDetailsModel model = tempDetailsService.getSingleDetailsTMP(id);
        return (model != null) ? ResponseEntity.ok(model) : ResponseEntity.notFound().build();
    }

    // ✅ Update TempDetails by ID
    @PutMapping("/{id}")
    public ResponseEntity<TempDetailsModel> updateDetails(@PathVariable Long id,
                                                          @RequestBody TempDetailsModel tempDetailsModel) {
        TempDetailsModel updated = tempDetailsService.updateFactureDetailsTMP(id, tempDetailsModel);
        return (updated != null) ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // ✅ Delete TempDetails by numeric ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDetails(@PathVariable Long id) {
        tempDetailsService.deleteFactureDetailsTMP(id);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/details/{code}")
    public ResponseEntity<String> deleteFactureDetails(@PathVariable("code") String code,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "25") int size
                                                       ){
        tempDetailsService.deleteAllFactureDetailsTMP(code, PageRequest.of(page, size));
        return  ResponseEntity.ok("Temp Details Deleted Successfully.");
    }

    // ✅ Delete all TempDetails for a client
    @DeleteMapping("/client/{client}")
    public ResponseEntity<Void> deleteAllDetailsByClient(@PathVariable String client,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "25") int size
    ) {

        tempDetailsService.deleteAllFactureDetailsTMP(client, PageRequest.of(page, size));
        return ResponseEntity.noContent().build();
    }

}
