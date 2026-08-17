package com.velogexpress.controller;

import org.springframework.cache.annotation.Cacheable;

import com.velogexpress.model.OrderDetailsModel;
import com.velogexpress.service.ColisCounterService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/colis")
public class ColisController {
    private ColisCounterService colisCounterService;

    @Cacheable(cacheNames = "colis")
    @GetMapping("{usercode}")
    public ResponseEntity<Page> getDeliveredColis(@PathVariable("usercode") String code,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "25") int size
                                                  ){
        Page<OrderDetailsModel> model=colisCounterService.countDeliveredColis(code, PageRequest.of(page, size));
        return ResponseEntity.ok(model);
    }

    @Cacheable(cacheNames = "colis")
    @GetMapping("/ready/{usercode}")
    public ResponseEntity<Page> getReadyColis(@PathVariable("usercode") String code,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "25") int size
                                                ){
        Page<OrderDetailsModel> model=colisCounterService.countReadyColis(code, PageRequest.of(page, size));
        return ResponseEntity.ok(model);
    }

    @Cacheable(cacheNames = "colis")
    @GetMapping("/shipped/{usercode}")
    public ResponseEntity<Page> getShippedColis(@PathVariable("usercode") String code,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "25") int size
                                                ){
        Page<OrderDetailsModel> model=colisCounterService.countShippedColis(code, PageRequest.of(page, size));
        return ResponseEntity.ok(model);
    }
}
