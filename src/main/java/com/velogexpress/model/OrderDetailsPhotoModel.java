package com.velogexpress.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsPhotoModel {
    private Long id;
    private Long orderDetailsId;
    private String photo;
    private LocalDateTime createdAt;
}
