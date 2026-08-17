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
@Table(name = "machine")
public class Machine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name = "machine_name")
    private String name;
    @NotNull
    @Column(name = "machine_marque")
    private String marque;
    @NotNull
    @Column(name = "machine_desc")
    private String description;
    @NotNull
    @Column(name = "machine_serial", unique = true)
    private String serial;
}
