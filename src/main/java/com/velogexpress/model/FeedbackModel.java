package com.velogexpress.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackModel {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String subject;
    private String message;
    private String status;
    private String date;
}
