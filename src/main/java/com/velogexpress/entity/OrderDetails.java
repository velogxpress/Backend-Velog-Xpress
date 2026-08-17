package com.velogexpress.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_details")
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Order ship;
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "client_id", nullable = true)
    private Clientregister client;

    @NotNull
    @Column(name = "upc_colis",unique = true)
    private String upc;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_fees")
    private Cipinfee citypoundfee;

    @NotNull
    @Column(name = "pounds")
    private Double pounds;
    @NotNull
    @Column(name = "sub_total")
    private Double subtotal;
    @NotNull
    @Column(name = "status")
    private String status;
    @Column(name = "delivery_date")
    private String delivery;
    @Column(name = "exp_name")
    private String exp_name;
    @Column(name = "exp_email")
    private String exp_email;
    @Column(name = "exp_phone")
    private String exp_phone;
    @Column(name = "rec_name")
    private String rec_name;
    @Column(name = "rec_email")
    private String rec_email;
    @Column(name = "rec_phone")
    private String rec_phone;
    @Column(name = "colis_type")
    private String type;
    @Column(name = "`condition`")
    private String condition;
    private BigDecimal price;
    private String tracking;
    private BigDecimal douane;
    @Column(name = "colis_image")
    private String picture;
    private String note;
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "user_id", nullable = true)
    private Clientregister user;
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Object getSh() {
        return null;
    }
}
