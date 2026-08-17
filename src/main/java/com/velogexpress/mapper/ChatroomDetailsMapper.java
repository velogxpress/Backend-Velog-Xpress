package com.velogexpress.mapper;

import com.velogexpress.entity.ChatroomDetails;
import com.velogexpress.model.ChatroomDetailsModel;

public class ChatroomDetailsMapper {
    public static ChatroomDetailsModel mapToChatroomDetailsModel(ChatroomDetails chatroomDetails){
        return new ChatroomDetailsModel(
                chatroomDetails.getId(),
                chatroomDetails.getDate(),
                chatroomDetails.getChat(),
                chatroomDetails.getMessage(),
                chatroomDetails.getStatus(),
                chatroomDetails.getPosition()
        );
    }
    public static ChatroomDetails mapToChatroomDetails(ChatroomDetailsModel chatroomDetailsModel){
        return new ChatroomDetails(
                chatroomDetailsModel.getId(),
                chatroomDetailsModel.getDate(),
                chatroomDetailsModel.getChat(),
                chatroomDetailsModel.getMessage(),
                chatroomDetailsModel.getStatus(),
                chatroomDetailsModel.getPosition()
        );
    }
}
