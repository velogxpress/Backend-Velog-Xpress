package com.velogexpress.model;

import com.velogexpress.entity.Category;
import com.velogexpress.entity.Facture;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class FactureDetailsModel {
    private Long id;
    private FactureModel facture;
    private String colis;
    private CategoryModel category;
    private String description;
    private Double fixedprice;
    private Double pounds;
    private Double fee;
    private Double soubtotal;
}
