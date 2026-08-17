package com.velogexpress.controller;

import com.velogexpress.model.ClientregisterModel;
import com.velogexpress.service.ClientregisterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/getupdatepassword")
public class UpdatPasswordController {
    private ClientregisterService clientregisterService;
    //Build Update Password
    @PutMapping("{usercode}")
    public ResponseEntity<ClientregisterModel> updateUserregister(@PathVariable("usercode") String code, @RequestBody ClientregisterModel clientregisterModel){
        ClientregisterModel clientregisterModel1=clientregisterService.updatePassword(code,clientregisterModel);
        return ResponseEntity.ok(clientregisterModel1);
    }
}
