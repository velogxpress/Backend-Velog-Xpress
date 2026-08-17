package com.velogexpress.controller;

import com.velogexpress.model.OrderDetailsPhotoModel;
import com.velogexpress.service.OrderDetailsPhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/orderdetails-photos")
@RequiredArgsConstructor
public class OrderDetailsPhotoController {
    private final OrderDetailsPhotoService orderDetailsPhotoService;

    @PostMapping(value = "/{orderDetailsId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<OrderDetailsPhotoModel> addPhoto(
            @PathVariable Long orderDetailsId,
            @RequestPart("file") MultipartFile file
    ) {
        OrderDetailsPhotoModel saved = orderDetailsPhotoService.addPhoto(orderDetailsId, file);
        return saved != null ? ResponseEntity.status(HttpStatus.CREATED).body(saved) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{orderDetailsId}")
    public ResponseEntity<List<OrderDetailsPhotoModel>> getPhotos(@PathVariable Long orderDetailsId) {
        return ResponseEntity.ok(orderDetailsPhotoService.getPhotos(orderDetailsId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePhoto(@PathVariable Long id) {
        orderDetailsPhotoService.deletePhoto(id);
        return ResponseEntity.ok("Photo deleted successfully.");
    }
}
