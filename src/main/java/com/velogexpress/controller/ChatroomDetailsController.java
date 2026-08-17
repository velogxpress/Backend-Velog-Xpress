package com.velogexpress.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import com.velogexpress.model.ChatroomDetailsModel;
import com.velogexpress.service.ChatroomDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/chatroomdetails")
public class ChatroomDetailsController {

    private final ChatroomDetailsService chatroomDetailsService;

    // Create Chatroom Detail
    @CacheEvict(cacheNames = "chatroomDetails", allEntries = true)
    @PostMapping
    public ResponseEntity<ChatroomDetailsModel> createChatroomDetail(
            @RequestBody ChatroomDetailsModel chatroomDetailsModel) {
        ChatroomDetailsModel saved = chatroomDetailsService.createChatroomDetails(chatroomDetailsModel);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Get Chatroom Details by Chatroom ID
    @Cacheable(cacheNames = "chatroomDetails")
    @GetMapping("/{id}")
    public ResponseEntity<Page<ChatroomDetailsModel>> getChatroomDetailsById(
            @PathVariable("id") Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {

        Page<ChatroomDetailsModel> details = chatroomDetailsService.getChatroomDetails(id, PageRequest.of(page, size));
        return ResponseEntity.ok(details);
    }
}
