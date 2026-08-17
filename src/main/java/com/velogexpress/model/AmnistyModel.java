package com.velogexpress.model;

import com.velogexpress.entity.Category;
import com.velogexpress.entity.Cipinfee;
import com.velogexpress.entity.Clientregister;
import jakarta.persistence.*;
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
public class AmnistyModel {
    private Long id;
    private Category category;
    private Double pounds;
    private String status;
    private String tracking;
    private String picture;
    private String note;
    private String name;
    private String telephone;
    private BigDecimal price;
    private BigDecimal douane;
    private Cipinfee citypoundfee;
    private Clientregister user;
    private LocalDateTime createdAt;
}
