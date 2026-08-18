package com.velogexpress.controller;

import com.velogexpress.service.R2Service;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ImageController {

    @Value("${r2.public-url}")
    private String r2PublicUrl;

    private final R2Service r2Service;

    // Files now live in Cloudflare R2 instead of local disk.
    // This route is kept for backward compatibility: it redirects to the
    // object's public R2 URL instead of reading uploadDir from disk.
    //
    // Two eras of objects live in the same bucket: photos migrated from
    // the old local-disk storage were uploaded under uploads/products/,
    // while everything the current backend code uploads goes under
    // products/ (no uploads/ prefix). A bare filename alone doesn't tell
    // us which era it's from, so we check whether the current-convention
    // key exists and fall back to the legacy key if it doesn't.
    @GetMapping("/products/{filename:.+}")
    public ResponseEntity<Void> getProductImage(@PathVariable String filename) {
        String base = r2PublicUrl.endsWith("/")
                ? r2PublicUrl.substring(0, r2PublicUrl.length() - 1)
                : r2PublicUrl;

        String currentKey = "products/" + filename;
        String legacyKey = "uploads/products/" + filename;
        String key = r2Service.exists(currentKey) ? currentKey : legacyKey;

        String encodedFilename = UriUtils.encodePathSegment(filename, StandardCharsets.UTF_8);
        String keyPrefix = key.substring(0, key.length() - filename.length());
        String url = base + "/" + keyPrefix + encodedFilename;

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }
}
