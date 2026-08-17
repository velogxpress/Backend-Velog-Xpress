package com.velogexpress.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "surcursal")
public class Surcursal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "surc_name",unique = true)
    private String name;
    @NotNull
    @Column(name = "surc_address")
    private String address;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ville_id")
    private Ville ville;
    @NotNull
    @Column(name = "surc_phone")
    private String phone;
    @NotNull
    @Column(name = "surc_horaire")
    private String horaire;
}
