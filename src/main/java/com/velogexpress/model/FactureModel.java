package com.velogexpress.model;

import com.velogexpress.entity.Agentsurcursal;
import com.velogexpress.entity.Clientregister;
import com.velogexpress.entity.Order;
import com.velogexpress.entity.Surcursal;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FactureModel {
    private Long id;
    private String code;
    private String date;
    private String client;
    private String clientphone;
    private Double amount;
    private String status;
    private Order ship;
    private Clientregister user;
    private Double tarif;
    private Double assurance;
    private Double discount;
    private Double subtotal;
    private Double balance;
    private Double effectif;
    private Agentsurcursal surcursal;
    private String destination;
}
