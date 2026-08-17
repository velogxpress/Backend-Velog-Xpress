package com.velogexpress.controller;

import com.velogexpress.model.ClientregisterModel;
import com.velogexpress.model.OrderDetailsModel;
import com.velogexpress.model.RegisterModel;
import com.velogexpress.service.ClientregisterService;
import com.velogexpress.service.OrderDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/register")
public class RegisterController {
    private final ClientregisterService clientregisterService;
    private final OrderDetailsService orderDetailsService;

    // Create Client User
    @PostMapping("/create")
    public ResponseEntity<ClientregisterModel> createClientUser(@RequestBody ClientregisterModel clientregisterModel) {
        ClientregisterModel savedModel = clientregisterService.createUser(clientregisterModel);
        return new ResponseEntity<>(savedModel, HttpStatus.CREATED);
    }
    // Get Clientregister by Usercode
    // Get Clientregister by Usercode
    @GetMapping("/client")
    public ResponseEntity<RegisterModel> getRegisterByUsercode(@RequestParam String code) {
        RegisterModel clientregister = clientregisterService.getRegisterByUsercode(code);
        return ResponseEntity.ok(clientregister);
    }

    @GetMapping("/existuser/{email}")
    public ResponseEntity<String> getXeExistsmail(@PathVariable("email") String email) {
        String valor=clientregisterService.findExistEmail(email);
        return ResponseEntity.ok(valor);
    }

    @GetMapping("/countclient")
    public ResponseEntity<Long> getCountClient() {
        Long client=clientregisterService.countClient();
        return ResponseEntity.ok(client);
    }

    @GetMapping("/trackcolis")
    public ResponseEntity<OrderDetailsModel> trackColis(@RequestParam() String search) {
        return ResponseEntity.ok(orderDetailsService.trackColis(search));
    }
}
