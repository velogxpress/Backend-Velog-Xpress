package com.velogexpress.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VilleModel {
    private Long id;
    private String description;
    private String abreger;
    private RegionModel region;

}
