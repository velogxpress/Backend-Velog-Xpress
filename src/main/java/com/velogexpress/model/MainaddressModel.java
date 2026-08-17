package com.velogexpress.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MainaddressModel {
    private Long id;
    private String addressline;
    private String city;
    private String state;
    private String zipcode;
    private String phone;
}
