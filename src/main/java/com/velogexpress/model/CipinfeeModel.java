package com.velogexpress.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CipinfeeModel {
    private Long id;
    private VilleModel city;
    private FeepoundsModel pounds;
    private InsuranceModel insurance;
    private SpecialfeeModel specialfee;
}
