package com.velogexpress.model;

import com.velogexpress.entity.OrderDetails;
import com.velogexpress.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreModel {
    private Long id;
    private OrderDetails orderdetails;
    private Tag tag;
    private String status;
}
