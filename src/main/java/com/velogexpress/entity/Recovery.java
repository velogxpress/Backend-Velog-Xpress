package com.velogexpress.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recovery")
public class Recovery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name = "date")
    private String date;
    @NotNull
    @Column(name = "recuperation_email")
    private String email;
    @NotNull
    @Column(name = "code_pin")
    private String code;
    @NotNull
    @Column(name = "status")
    private String status;
    private String resetToken;
    private LocalDateTime tokenExpiration;
}
