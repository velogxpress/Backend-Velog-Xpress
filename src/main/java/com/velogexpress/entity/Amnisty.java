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
@Table(name = "amnisty")
public class Amnisty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;
    @NotNull
    @Column(name = "pounds")
    private Double pounds;
    @NotNull
    @Column(name = "status")
    private String status;
    private String tracking;
    @Column(name = "colis_image")
    private String picture;
    private String note;
    private String name;
    private String telephone;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "douane")
    private BigDecimal douane;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_fees")
    private Cipinfee citypoundfee;
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "user_id", nullable = true)
    private Clientregister user;
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
