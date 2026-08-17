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
@Table(name = "facture")
public class Facture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name = "facture_code",unique = true)
    private String code;
    @NotNull
    @Column(name = "date")
    private String date;
   // @ManyToOne(fetch = FetchType.EAGER)
    @Column(name = "client")
    private String client;
    @Column(name = "client_phone")
    private String clientphone;
    @NotNull
    @Column(name = "amount")
    private Double amount;
    @NotNull
    @Column(name = "status")
    private String status;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Order ship;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Clientregister user;
    @Column(name = "tarif")
    private Double tarif;
    @Column(name = "assurance")
    private Double assurance;
    @Column(name = "discount")
    private Double discount;
    @Column(name = "subtotal")
    private Double subtotal;
    @Column(name = "balance")
    private Double balance;
    @Column(name = "effectif")
    private Double effectif;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "surcursal_id")
    private Agentsurcursal surcursal;
    private String destination;



}
