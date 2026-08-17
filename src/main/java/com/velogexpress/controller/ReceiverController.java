package com.velogexpress.controller;

import com.velogexpress.model.OrderDetailsModel;
import com.velogexpress.service.OrderDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/findReceiver")
public class ReceiverController {
    private OrderDetailsService orderDetailsService;

    @GetMapping("{phone}")
    public ResponseEntity getReceiver(@PathVariable("phone") String phone){
        OrderDetailsModel orderDetailsModel=orderDetailsService.searchReceiver(phone);
        return ResponseEntity.ok(orderDetailsModel);
    }
}
