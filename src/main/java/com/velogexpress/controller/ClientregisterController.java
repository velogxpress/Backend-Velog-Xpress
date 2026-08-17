package com.velogexpress.controller;

import com.velogexpress.model.ClientregisterModel;
import com.velogexpress.model.RegisterModel;
import com.velogexpress.service.ClientregisterService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/clientregister")
public class ClientregisterController {

    private final ClientregisterService clientregisterService;

    // Create Client User
    @PostMapping
    public ResponseEntity<ClientregisterModel> createClientUser(@RequestBody ClientregisterModel clientregisterModel) {
        ClientregisterModel savedModel = clientregisterService.createUser(clientregisterModel);
        return new ResponseEntity<>(savedModel, HttpStatus.CREATED);
    }

    // Create Client User
    @PostMapping("/newutilisateur")
    public ResponseEntity<ClientregisterModel> createutilisateur(@RequestBody ClientregisterModel clientregisterModel) {
        ClientregisterModel savedModel = clientregisterService.createUtilisateur(clientregisterModel);
        return new ResponseEntity<>(savedModel, HttpStatus.CREATED);
    }

    // Get All Clientregister
    @GetMapping
    public ResponseEntity<Page<ClientregisterModel>> getAllClientregister(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Page<ClientregisterModel> clientregisterModels =
                clientregisterService.getAllClienteregister(PageRequest.of(page, size, Sort.by("id").descending()));
        return ResponseEntity.ok(clientregisterModels);
    }

    // Get Clientregister by Usercode
    @GetMapping("/{usercode}")
    public ResponseEntity<ClientregisterModel> getClientregisterByUsercode(
            @PathVariable("usercode") String code
    ) {
        ClientregisterModel clientregister =
                clientregisterService.getClientregisterByUsercode(code);
        return ResponseEntity.ok(clientregister);
    }


    // Update Clientregister
    @PutMapping("/{usercode}")
    public ResponseEntity<ClientregisterModel> updateClientregister(
            @PathVariable("usercode") String code,
            @RequestBody ClientregisterModel clientregisterModel
    ) {
        ClientregisterModel updated =
                clientregisterService.updateClientregister(code, clientregisterModel);
        return ResponseEntity.ok(updated);
    }

    // Count Clients by user
    @GetMapping("/clientcounter/{user}")
    public ResponseEntity<Long> getCountClient(
            @PathVariable("user") String user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Long clientCount =
                clientregisterService.getCountUser(user, PageRequest.of(page, size));
        return ResponseEntity.ok(clientCount);
    }


    // Get all Agents
    @GetMapping("/user")
    public ResponseEntity<Page<ClientregisterModel>> getAllAgents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Page<ClientregisterModel> agents =
                clientregisterService.getAllAgent(PageRequest.of(page, size, Sort.by("id").descending()));
        return ResponseEntity.ok(agents);
    }

    // Get Agent by Usercode
    @GetMapping("/user/{usercode}")
    public ResponseEntity<Page<ClientregisterModel>> getAgentByUsercode(
            @PathVariable("usercode") String code,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Page<ClientregisterModel> agent =
                clientregisterService.getAgent(code, PageRequest.of(page, size, Sort.by("id").descending()));
        return ResponseEntity.ok(agent);
    }

    @GetMapping("/countclient")
    public ResponseEntity<Long> getCountClient() {
        Long client=clientregisterService.countClient();
        return ResponseEntity.ok(client);
    }

    @GetMapping("/countclientcity")
    public ResponseEntity<List> getCountClientCity() {
        List<ClientregisterModel> client=clientregisterService.getCountGraphe();
        return ResponseEntity.ok(client);
    }

    @PutMapping("/edituser/{id}")
    public ResponseEntity<ClientregisterModel> editUser(@PathVariable("id") String id,@RequestBody ClientregisterModel clientregisterModel) {
        ClientregisterModel client=clientregisterService.EditUserInfo(id, clientregisterModel);
        return ResponseEntity.ok(client);
    }

    @DeleteMapping("/deleteuser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") String id) {
        clientregisterService.deleteUser(id);
        return ResponseEntity.ok("Utilisateur supprimer avec succes.");
    }

    @GetMapping("/existuser/{email}")
    public ResponseEntity<String> getXeExistsmail(@PathVariable("email") String email) {
        String valor=clientregisterService.findExistEmail(email);
        return ResponseEntity.ok(valor);
    }

}
