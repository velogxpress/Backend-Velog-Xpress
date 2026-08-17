package com.velogexpress.controller;

import com.velogexpress.exception.RessourceNotFoundException;
import com.velogexpress.model.RecoveryModel;
import com.velogexpress.service.RecoveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

//@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/recoveries")
public class RecoveryController {

    private final RecoveryService recoveryService;

    @PostMapping
    public ResponseEntity<RecoveryModel> createPIN(@Valid @RequestBody RecoveryModel recoveryModel) {
       // log.info("Creating recovery PIN for email: {}", recoveryModel.getEmail());
        try {
            RecoveryModel saved = recoveryService.createPIN(recoveryModel);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating recovery PIN for {}: {}", recoveryModel.getEmail(), e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create recovery PIN");
        }
    }

    @GetMapping
    public ResponseEntity<Page<RecoveryModel>> getAllRecoveries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {

        Page<RecoveryModel> recoveryPage = recoveryService.getAllPIN(PageRequest.of(page, size));
        return ResponseEntity.ok(recoveryPage);
    }

    @GetMapping("/getotp")
    public ResponseEntity<RecoveryModel> getPIN(@RequestParam String email) {
        RecoveryModel model=recoveryService.getRecovery(email);
        return ResponseEntity.ok(model);
    }


    @PutMapping("/updatepin/{email}")
    public ResponseEntity<RecoveryModel> updatePINByEmail(@PathVariable String email) {
        try {
            RecoveryModel updated = recoveryService.updatePINByUser(email);
            return ResponseEntity.ok(updated);
        } catch (RessourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("RecoveryController is active");
    }

    @GetMapping("/verify-token")
    public ResponseEntity<?> verifyToken(@RequestParam String token) {
        String rerecovery= recoveryService.verifyToken(token);
        return ResponseEntity.ok(rerecovery);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam String token,
            @RequestBody Map<String, String> request
    ) {
       String newPassword = request.get("newPassword");
       if (newPassword == null || newPassword.isBlank()) {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le nouveau mot de passe est requis");
       }
       String recovery=recoveryService.resetPassword(token,newPassword);
       if (!"Mot de passe mis à jour".equals(recovery)) {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, recovery);
       }
       return ResponseEntity.ok(recovery);
    }
}
