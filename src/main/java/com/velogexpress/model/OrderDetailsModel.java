package com.velogexpress.model;

import com.velogexpress.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsModel {
    private Long id;
    private Order ship;
    private Clientregister client;
    private String upc;
    private Category category;
    private Cipinfee citypoundfee;
    private Double pounds;
    private Double subtotal;
    private String status;
    private String delivery;
    private String exp_name;
    private String exp_email;
    private String exp_phone;
    private String rec_name;
    private String rec_email;
    private String rec_phone;
    private String type;
    private String condition;
    private BigDecimal price;
    private String tracking;
    private BigDecimal douane;
    private String picture;
    private String note;
    private Clientregister user;
    private LocalDateTime createdAt;
}
