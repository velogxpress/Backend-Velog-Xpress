package com.velogexpress.service;

import com.velogexpress.model.ChatroomModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChatroomService {
    ChatroomModel createChatroom(ChatroomModel chatroomModel);
    Page<ChatroomModel> getAllChatroom(Pageable pageable);
    Page<ChatroomModel> getChatroom(String client, Pageable pageable);
    ChatroomModel updateChatroom(String client);
    void deleteChatroom(String client);
}
