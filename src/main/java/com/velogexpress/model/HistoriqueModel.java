package com.velogexpress.model;

import com.velogexpress.entity.Clientregister;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoriqueModel {
    private Long id;
    private ClientregisterModel user;
    private String login;
    private String place;
    private String logout;
}
