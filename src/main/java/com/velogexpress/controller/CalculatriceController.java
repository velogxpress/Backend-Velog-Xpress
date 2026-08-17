package com.velogexpress.controller;

import org.springframework.cache.annotation.Cacheable;

import com.velogexpress.entity.Clientregister;
import com.velogexpress.service.CalculatriceService;
import com.velogexpress.service.ClientregisterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/calculatrice")
public class CalculatriceController {
    private CalculatriceService calculatriceService;
    @Cacheable(cacheNames = "calculatrice")
    @GetMapping
    public ResponseEntity<String> calculate(@RequestParam long idCity,@RequestParam double pound) {
        String result= calculatriceService.calculeEstimatePrice(idCity,pound);
        return ResponseEntity.ok(result);
    }
}
