package com.velogexpress.service.impl;

import com.velogexpress.entity.Chatroom;
import com.velogexpress.mapper.ChatroomMapper;
import com.velogexpress.model.ChatroomModel;
import com.velogexpress.repository.ChatroomRepository;
import com.velogexpress.service.ChatroomService;
import com.velogexpress.tools.DateTime;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class ChatroomServiceImpl implements ChatroomService {
    private ChatroomRepository chatroomRepository;
    @Override
    public ChatroomModel createChatroom(ChatroomModel chatroomModel) {
        Chatroom chatroom= ChatroomMapper.mapToChatroom(chatroomModel);
        DateTime dtime= new DateTime();
        chatroom.setDate(dtime.CURRENTDATETIME());
        chatroom.setClient(chatroomModel.getClient());
        chatroom.setTo(chatroomModel.getTo());
        Chatroom chat=chatroomRepository.save(chatroom);
        return ChatroomMapper.mapToChatroomModel(chat);
    }

    @Override
    public Page<ChatroomModel> getAllChatroom(Pageable pageable) {
        return chatroomRepository.getAllChat(pageable)
                .map(ChatroomMapper::mapToChatroomModel);
    }

    @Override
    public Page<ChatroomModel> getChatroom(String client, Pageable pageable) {
        return chatroomRepository.getChats(client, pageable)
                .map(ChatroomMapper::mapToChatroomModel);

    }

    @Override
    public ChatroomModel updateChatroom(String client) {
        Chatroom chatroom=chatroomRepository.getChat(client);
        if(chatroom!=null){
            chatroom.setStatus("A/R");
            Chatroom saveObj= chatroomRepository.save(chatroom);
            return ChatroomMapper.mapToChatroomModel(saveObj);
        }else{
            return null;
        }
    }

    @Override
    public void deleteChatroom(String client) {
        Chatroom chatroom=chatroomRepository.getChat(client);
        if(chatroom!=null){
            chatroomRepository.delete(chatroom);
        }
    }
}
