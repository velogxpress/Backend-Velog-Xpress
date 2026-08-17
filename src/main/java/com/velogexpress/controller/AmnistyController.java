package com.velogexpress.controller;

import com.velogexpress.model.AmnistyModel;
import com.velogexpress.service.AmnistyService;
import com.velogexpress.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/amnisty")
@RequiredArgsConstructor
public class AmnistyController {
    private final AmnistyService amnistyService;
    private final PdfService pdfService;

    @PostMapping(
            value = "/save-colis",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<AmnistyModel> createAmnisty(
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart("amnistyModel") AmnistyModel amnistyModel
    ) {
        AmnistyModel saved =
                amnistyService.createAmnesty(file, amnistyModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<AmnistyModel>> getAllAmnisty() {
        return ResponseEntity.ok(amnistyService.getAllAmnisty());
    }

    @GetMapping("/searchamnisty")
    public ResponseEntity<List<AmnistyModel>> searchAmnisty( @RequestParam String search) {
        return ResponseEntity.ok(amnistyService.searchAmnisty(search));
    }

    @PutMapping("/updateamnisty/{upc}")
    public ResponseEntity<AmnistyModel> updateAmnisty(@PathVariable String upc,@RequestBody AmnistyModel amnistyModel) {
        AmnistyModel model=amnistyService.updateAmnisty(upc, amnistyModel);
        return ResponseEntity.ok(model);
    }

    @GetMapping(
            value = "/amnistylabeldownload/{upc}",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<byte[]> generateLabelPdf(@PathVariable("upc") String upc) {

        byte[] pdfBytes = pdfService.labelamnistyDownload(upc);

        // 🔒 Sécurité : éviter réponse vide
        if (pdfBytes == null || pdfBytes.length == 0) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=label-" + upc + ".pdf")
                .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                .header(HttpHeaders.PRAGMA, "no-cache")
                .header(HttpHeaders.EXPIRES, "0")
                .contentLength(pdfBytes.length)
                .body(pdfBytes);
    }


    @GetMapping(
            value = "/amnistyinvoicedownload",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<byte[]> generateManifestPdf(
            @RequestParam String name,
            @RequestParam String phone,
            @RequestParam(required = false) String tracking
    ) {
        byte[] pdfBytes = pdfService.amnistyDownload(name, phone, tracking);

        // 🔒 Sécurité : éviter réponse vide
        if (pdfBytes == null || pdfBytes.length == 0) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=invoice-" + name + ".pdf")
                .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                .header(HttpHeaders.PRAGMA, "no-cache")
                .header(HttpHeaders.EXPIRES, "0")
                .contentLength(pdfBytes.length)
                .body(pdfBytes);
    }
}
