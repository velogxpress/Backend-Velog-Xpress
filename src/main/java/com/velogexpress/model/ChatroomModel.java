package com.velogexpress.model;

import com.velogexpress.entity.Clientregister;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatroomModel {
    private Long id;
    private String date;
    private Clientregister client;
    private String to;
    private String status;
}
