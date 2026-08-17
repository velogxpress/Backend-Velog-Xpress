package com.velogexpress.model;

import com.velogexpress.entity.Order;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StorageModel {
    private Long id;
    private Order order;
    private String container;
    private String description;
    private String airwaybill;
}
