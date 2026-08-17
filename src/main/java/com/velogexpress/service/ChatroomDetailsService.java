package com.velogexpress.service;

import com.velogexpress.model.ChatroomDetailsModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ChatroomDetailsService {
    ChatroomDetailsModel createChatroomDetails(ChatroomDetailsModel chatroomDetailsModel);
    Page<ChatroomDetailsModel> getChatroomDetails(Long id, Pageable pageable);
    ChatroomDetailsModel countRoom(Long id);
}
