package com.velogexpress.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TempDetailsModel {
    private Long id;
    private String colis;
    private CategoryModel category;
    private String description;
    private Double fixedprice;
    private Double pounds;
    private Double fee;
    private Double soubtotal;
    private String client;
}
