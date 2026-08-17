package com.velogexpress.controller;

import com.velogexpress.model.OrderDetailsModel;
import com.velogexpress.service.OrderDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/printlabel")
public class LabelController {
    private OrderDetailsService orderDetailsService;

    //Build Print Label
    @GetMapping("{upc}")
    public ResponseEntity<OrderDetailsModel> printSticker(@PathVariable("upc") String upc){
        OrderDetailsModel model=orderDetailsService.printLabel(upc);
        return ResponseEntity.ok(model);
    }
}
