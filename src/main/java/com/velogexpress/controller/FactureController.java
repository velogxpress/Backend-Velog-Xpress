package com.velogexpress.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import com.velogexpress.model.FactureModel;
import com.velogexpress.service.FactureService;
import com.velogexpress.service.PdfService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/facture")
public class FactureController {

    private final FactureService factureService;
    private final PdfService pdfService;

    // ----------------- CREATE FACTURE -----------------
    @CacheEvict(cacheNames = "facture", allEntries = true)
    @PostMapping
    public ResponseEntity<FactureModel> createFacture(@RequestBody FactureModel factureModel) {
        FactureModel facture = factureService.createFacture(factureModel);
        return new ResponseEntity<>(facture, HttpStatus.CREATED);
    }

    // ----------------- GET ALL FACTURES -----------------
    @Cacheable(cacheNames = "facture")
    @GetMapping
    public ResponseEntity<Page<FactureModel>> getAllFactures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Page<FactureModel> model = factureService.getAllFacture(PageRequest.of(page, size));
        return ResponseEntity.ok(model);
    }

    // ----------------- GET FACTURE BY CODE -----------------
    @Cacheable(cacheNames = "facture")
    @GetMapping("/{code}")
    public ResponseEntity<FactureModel> getFactureByCode(@PathVariable("code") String code) {
        Optional<FactureModel> model = factureService.getFactureByCode(code);
        return model.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null));
    }

    // ----------------- UPDATE FACTURE STATUS -----------------
    @CacheEvict(cacheNames = "facture", allEntries = true)
    @PutMapping("/{code}")
    public ResponseEntity<FactureModel> updateFacture(@PathVariable("code") String code) {
        Optional<FactureModel> model = factureService.updateFacture(code);
        return model.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null));
    }

    // ----------------- DELETE FACTURE -----------------
    @CacheEvict(cacheNames = "facture", allEntries = true)
    @DeleteMapping("/{code}")
    public ResponseEntity<String> deleteFacture(@PathVariable("code") String code) {
        factureService.deleteFacture(code);
        return ResponseEntity.ok("Facture deleted successfully.");
    }

    @Cacheable(cacheNames = "facture")
    @GetMapping("/searchfacture/{code}")
    public ResponseEntity<Page> getFactureByCode(@PathVariable("code") String code,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "25") int size
                                                 ){
        Page<FactureModel> model=factureService.searchFacture(code, PageRequest.of(page, size));
        return ResponseEntity.ok(model);
    }

    @Cacheable(cacheNames = "facture")
    @GetMapping("/facturewith/{id}&{od}")
    public ResponseEntity<Page> getFactureDetailsWith(@PathVariable("id") String Id, @PathVariable("od") Long Od,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "25") int size
                                                      ){
        Page<FactureModel> ModelList=factureService.getFactureDetailsWith(Id,Od, PageRequest.of(page, size));
        return ResponseEntity.ok(ModelList);
    }

    @GetMapping(
            value = "/facturedownload/{upc}",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<byte[]> generateManifestPdf(@PathVariable("upc") String upc) {

        byte[] pdfBytes = pdfService.factureDownload(upc);

        // 🔒 Sécurité : éviter réponse vide
        if (pdfBytes == null || pdfBytes.length == 0) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=facture-" + upc + ".pdf")
                .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                .header(HttpHeaders.PRAGMA, "no-cache")
                .header(HttpHeaders.EXPIRES, "0")
                .contentLength(pdfBytes.length)
                .body(pdfBytes);
    }

    @GetMapping(
            value = "/facturedownloadA4/{upc}",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<byte[]> generateManifestPdfA4(@PathVariable("upc") String upc) {

        byte[] pdfBytes = pdfService.factureDownloadA4(upc);

        // 🔒 Sécurité : éviter réponse vide
        if (pdfBytes == null || pdfBytes.length == 0) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=facture-" + upc + ".pdf")
                .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                .header(HttpHeaders.PRAGMA, "no-cache")
                .header(HttpHeaders.EXPIRES, "0")
                .contentLength(pdfBytes.length)
                .body(pdfBytes);
    }

    @Cacheable(cacheNames = "facture")
    @GetMapping("/whatsappfacture/{upc}")
    public ResponseEntity<String> moveFactureForWhatsappA4(@PathVariable("upc") String upc) {
        System.out.println("Controller reached");

        String data=pdfService.movefactureDownloadA4(upc);
        return ResponseEntity.ok(data);
    }


    @Cacheable(cacheNames = "facture")
    @GetMapping("/facturetoday")
    public ResponseEntity<FactureModel> getFactureToday(){
        FactureModel ModelList=factureService.getFactureToday();
        return ResponseEntity.ok(ModelList);
    }

    @Cacheable(cacheNames = "facture")
    @GetMapping("/getfacturestatistique")
    public ResponseEntity<Page<FactureModel>> getFactureStatistique(
            @RequestParam Long orderID,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ){
        Page<FactureModel> ModelList=factureService.getCountAllFacture(orderID, PageRequest.of(page, size));
        return ResponseEntity.ok(ModelList);
    }

    @Cacheable(cacheNames = "facture")
    @GetMapping("/getfacturestatistique/{orderID}")
    public ResponseEntity<Page<FactureModel>> getFactureStatistique(
            @PathVariable Long orderID,
            @RequestParam Long surcursalID,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ){
        Page<FactureModel> ModelList=factureService.getCountAllFactureBySurcursal(orderID,surcursalID, PageRequest.of(page, size));
        return ResponseEntity.ok(ModelList);
    }
    @Cacheable(cacheNames = "facture")
    @GetMapping("/facturetodays/{ID}")
    public ResponseEntity<FactureModel> getFactureTodays(@PathVariable("ID") Long ID){
        FactureModel ModelList=factureService.getFactureToday(ID);
        return ResponseEntity.ok(ModelList);
    }

}
