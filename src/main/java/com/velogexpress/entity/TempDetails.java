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
@Table(name = "temp_facture_details")
public class TempDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name = "colis")
    private String colis;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(name = "description")
    private String description;
    @Column(name = "fixe")
    private Double fixedprice;
    @NotNull
    @Column(name = "poids")
    private Double pounds;
    @NotNull
    @Column(name = "fee")
    private Double fee;
    @NotNull
    @Column(name = "subtotal")
    private Double soubtotal;
    @NotNull
    @Column(name = "client")
    private String client;
}
