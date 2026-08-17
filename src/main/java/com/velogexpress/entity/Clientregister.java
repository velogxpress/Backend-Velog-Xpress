package com.velogexpress.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "client_register")
public class Clientregister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "full_name",unique = true)
    private String name;

    @NotNull
    @Column(name = "email",unique = true)
    private String email;

    @NotNull
    @Column(name = "address")
    private String address;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ville_id")
    private Ville ville;

    @NotNull
    @Column(name = "usercode",unique = true)
    private String usercode;
    @NotNull
    @Column(name = "password")
    private String password;
    @NotNull
    @Column(name = "phone")
    private String phone;
    @NotNull
    @Column(name = "role")
    private String role;
    @NotNull
    @Column(name = "status")
    private String status;
}
