package com.velogexpress.model;

import com.velogexpress.entity.OrderDetails;
import com.velogexpress.entity.Storage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StorageDetailsModel {
    private Long id;
    private Storage storage;
    private OrderDetails orderdetails;
}
