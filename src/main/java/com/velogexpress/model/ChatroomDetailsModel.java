package com.velogexpress.model;

import com.velogexpress.entity.Chatroom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatroomDetailsModel {
    private Long id;
    private String date;
    private Chatroom chat;
    private String message;
    private String status;
    private String position;
}
