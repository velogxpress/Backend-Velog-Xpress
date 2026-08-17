package com.velogexpress.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/uploads")
public class ImageController {
    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping("/products/{filename:.+}")
    public ResponseEntity<Resource> getProductImage(
            @PathVariable String filename) throws IOException {

        Path imagePath = Paths.get(uploadDir, "products", filename);

        if (!Files.exists(imagePath)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(imagePath.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        Files.probeContentType(imagePath)))
                .body(resource);
    }
}
