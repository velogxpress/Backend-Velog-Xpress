package com.velogexpress.mapper;

import com.velogexpress.entity.Chatroom;
import com.velogexpress.model.ChatroomModel;

public class ChatroomMapper {
    public static ChatroomModel mapToChatroomModel(Chatroom chatroom){
        return new ChatroomModel(
                chatroom.getId(),
                chatroom.getDate(),
                chatroom.getClient(),
                chatroom.getTo(),
                chatroom.getStatus()
        );
    }
    public static Chatroom mapToChatroom(ChatroomModel chatroomModel){
        return new Chatroom(
                chatroomModel.getId(),
                chatroomModel.getDate(),
                chatroomModel.getClient(),
                chatroomModel.getTo(),
                chatroomModel.getStatus()
        );
    }
}
