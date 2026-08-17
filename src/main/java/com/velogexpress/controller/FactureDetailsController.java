package com.velogexpress.controller;

import com.velogexpress.model.FactureDetailsModel;
import com.velogexpress.service.FactureDetailsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin("*")
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/facturedetails")
public class FactureDetailsController {

    private final FactureDetailsService factureDetailsService;

    @PostMapping
    public ResponseEntity<FactureDetailsModel> createDetails(@RequestBody FactureDetailsModel factureDetailsModel) {
        log.info("Creating FactureDetails for facture: {}", factureDetailsModel.getFacture());
        FactureDetailsModel model = factureDetailsService.createFactureDetails(factureDetailsModel);
        return new ResponseEntity<>(model, HttpStatus.CREATED);
    }

    @PostMapping("/quickfacture")
    public ResponseEntity<FactureDetailsModel> createQuickDetails(@RequestBody FactureDetailsModel factureDetailsModel) {
        log.info("Creating FactureDetails for facture: {}", factureDetailsModel.getFacture());
        FactureDetailsModel model = factureDetailsService.createQuickFactureDetails(factureDetailsModel);
        return new ResponseEntity<>(model, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<FactureDetailsModel>> getAllFactureDetails(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        Page<FactureDetailsModel> model = factureDetailsService.getAllFactureDetails(PageRequest.of(page, size));
        return ResponseEntity.ok(model);
    }

    @GetMapping("/details/{code}")
    public ResponseEntity<Page<FactureDetailsModel>> getAFactureDetails(
            @PathVariable("code") String code,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        Page<FactureDetailsModel> model = factureDetailsService.getFactureDetails(code,PageRequest.of(page, size));
        return ResponseEntity.ok(model);
    }

    @GetMapping("{code}")
    public ResponseEntity<FactureDetailsModel> getFactureDetailsByColis(@PathVariable("code") String code) {
        return factureDetailsService.getSingleFactureDetails(code)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("{code}")
    public ResponseEntity<String> deleteFactureDetails(@PathVariable("code") Long code) {
        log.info("Deleting FactureDetails with id: {}", code);
        factureDetailsService.deleteFactureDetails(code);
        return ResponseEntity.ok("Facture Details deleted successfully.");
    }

    @GetMapping("/by-facture/{code}")
    public ResponseEntity<Page<FactureDetailsModel>> getFactureDetailsByFactureCode(
            @PathVariable("code") String code,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        Page<FactureDetailsModel> model = factureDetailsService.getFactureDetails(code, PageRequest.of(page, size));
        return ResponseEntity.ok(model);
    }
}
