package com.velogexpress.controller;

import com.velogexpress.model.HistoriqueModel;
import com.velogexpress.service.HistoriqueService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/historique")
public class HistoriqueController {

    private final HistoriqueService historiqueService;

    @PostMapping
    public ResponseEntity<HistoriqueModel> createHistorique(@RequestBody HistoriqueModel model) {
        HistoriqueModel created = historiqueService.createHistorique(model);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<Page<HistoriqueModel>> getAllHistorique(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Page<HistoriqueModel> historiquePage = historiqueService.getAllHistorique(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "login"))
        );
        return ResponseEntity.ok(historiquePage);
    }

    @GetMapping("/user/{user}")
    public ResponseEntity<HistoriqueModel> getLatestHistoriqueByUser(@PathVariable String user) {
        HistoriqueModel model = historiqueService.getLatestHistoriqueByUser(user);
        return ResponseEntity.ok(model);
    }

    @PutMapping("/logout/{user}")
    public ResponseEntity<HistoriqueModel> logoutUser(@PathVariable String user) {
        HistoriqueModel model = historiqueService.updateLogoutTime(user);
        return ResponseEntity.ok(model);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<HistoriqueModel>> searchHistorique(
            @RequestParam String user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Page<HistoriqueModel> historiquePage = historiqueService.getAllHistoriqueByUser(
                user, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "login"))
        );
        return ResponseEntity.ok(historiquePage);
    }
}
