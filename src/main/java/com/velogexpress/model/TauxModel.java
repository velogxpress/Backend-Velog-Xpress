package com.velogexpress.model;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TauxModel {
    private Long id;
    private String devise;
    private double buy;
    private double sale;
    private String symbole;
}
