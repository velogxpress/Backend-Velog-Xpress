package com.velogexpress.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="taux")
public class Taux {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "devise")
    private String devise;
    @Column(name = "achat")
    private double buy;
    @Column(name = "vente")
    private double sale;
    @Column(name = "symbole")
    private String symbole;
}
