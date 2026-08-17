package com.velogexpress.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderModel {
    private Long id;
    private String date;
    private String shiporder;
    private Integer colisQty;
    private Double poundQty;
    private Double amount;
    private String status;
    private String shipdate;
}
