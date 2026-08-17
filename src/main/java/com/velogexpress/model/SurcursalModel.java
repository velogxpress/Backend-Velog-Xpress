package com.velogexpress.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class SurcursalModel {
    private Long id;
    private String name;
    private String address;
    private VilleModel ville;
    private String phone;
    private String horaire;
}
