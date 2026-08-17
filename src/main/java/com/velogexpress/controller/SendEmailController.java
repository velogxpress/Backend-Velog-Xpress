package com.velogexpress.controller;

import org.springframework.cache.annotation.CacheEvict;

import com.velogexpress.model.ClientregisterModel;
import com.velogexpress.service.SendEmail;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/sendemail")
public class SendEmailController {
    SendEmail sendEmail;

    // Send to specific client
    @CacheEvict(cacheNames = "sendEmail", allEntries = true)
    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String body) {
        String envoyer= sendEmail.sendEmail(to, subject, body);
        return ResponseEntity.ok(envoyer);
    }

    // Send to all client
    @CacheEvict(cacheNames = "sendEmail", allEntries = true)
    @PostMapping("/sending")
    public ResponseEntity<String> sendEmail(@RequestParam String subject, @RequestParam String body) {
        String envoyer= sendEmail.sendEmail(subject, body);
        return ResponseEntity.ok(envoyer);
    }
}
