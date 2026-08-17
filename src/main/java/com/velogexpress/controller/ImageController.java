package com.velogexpress.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/uploads")
public class ImageController {

    @Value("${r2.public-url}")
    private String r2PublicUrl;

    // Files now live in Cloudflare R2 instead of local disk.
    // This route is kept for backward compatibility: it redirects to the
    // object's public R2 URL instead of reading uploadDir from disk.
    @GetMapping("/products/{filename:.+}")
    public ResponseEntity<Void> getProductImage(@PathVariable String filename) {
        String base = r2PublicUrl.endsWith("/")
                ? r2PublicUrl.substring(0, r2PublicUrl.length() - 1)
                : r2PublicUrl;
        String url = base + "/products/" + UriUtils.encodePathSegment(filename, StandardCharsets.UTF_8);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }
}
