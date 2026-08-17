package com.velogexpress.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecoveryModel {
    private Long id;
    private String email;
    private String date;
    private String code;
    private String status;
    private String resetToken;
    private LocalDateTime tokenExpiration;
}
