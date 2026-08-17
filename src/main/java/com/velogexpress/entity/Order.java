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
@Table(name = "ship_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private String date;

    @NotNull
    @Column(name = "ship_order",unique = true)
    private String shiporder;

    @Column(name = "qty_colis")
    private Integer colisQty;

    @Column(name = "qty_pounds")
    private Double poundQty;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "status")
    private String status;
    @Column(name = "ship_date")
    private String shipdate;

}
