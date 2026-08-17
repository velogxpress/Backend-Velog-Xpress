package com.velogexpress.controller;

import com.velogexpress.model.OrderDetailsModel;
import com.velogexpress.service.OrderDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/findExpediteur")
public class ExpediteurController {
    private OrderDetailsService orderDetailsService;

    @GetMapping("{phone}")
    public ResponseEntity getExpediteur(@PathVariable("phone") String phone){
        OrderDetailsModel orderDetailsModel=orderDetailsService.searchExpediteur(phone);
        return ResponseEntity.ok(orderDetailsModel);
    }
}
