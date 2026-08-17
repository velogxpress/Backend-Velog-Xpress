package com.velogexpress.service.impl;

import com.velogexpress.entity.ChatroomDetails;
import com.velogexpress.mapper.ChatroomDetailsMapper;
import com.velogexpress.model.ChatroomDetailsModel;
import com.velogexpress.repository.ChatroomDetailsRepository;
import com.velogexpress.service.ChatroomDetailsService;
import com.velogexpress.tools.DateTime;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class ChatroomDetailsServiceImpl implements ChatroomDetailsService {

    private final ChatroomDetailsRepository chatroomDetailsRepository;

    @Transactional
    @Override
    public ChatroomDetailsModel createChatroomDetails(ChatroomDetailsModel chatroomDetailsModel) {

        ChatroomDetails entity = ChatroomDetailsMapper.mapToChatroomDetails(chatroomDetailsModel);

        entity.setDate(DateTime.CURRENTDATE());
        entity.setStatus("U/R"); // Prefer enum or constant

        ChatroomDetails saved = chatroomDetailsRepository.save(entity);

        return ChatroomDetailsMapper.mapToChatroomDetailsModel(saved);
    }

    @Override
    public Page<ChatroomDetailsModel> getChatroomDetails(Long id, Pageable pageable) {
        return chatroomDetailsRepository.findChat(id, pageable)
                .map(ChatroomDetailsMapper::mapToChatroomDetailsModel);
    }

    @Override
    public ChatroomDetailsModel countRoom(Long id) {
        ChatroomDetails details = chatroomDetailsRepository.countChat(id);
        return (details != null)
                ? ChatroomDetailsMapper.mapToChatroomDetailsModel(details)
                : null;
    }
}
