package com.velogexpress.model;

import com.velogexpress.entity.Ville;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientregisterModel {
    private Long id;
    private String name;
    private String email;
    private String address;
    private Ville ville;
    private String usercode;
    private String password;
    private String phone;
    private String role;
    private String status;
}
