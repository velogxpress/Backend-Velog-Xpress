package com.velogexpress.controller;

import com.velogexpress.model.ChatroomModel;
import com.velogexpress.service.ChatroomService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


//@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/chatroom")
public class ChatroomController {

    private final ChatroomService chatroomService;

    // Create Chatroom
    @PostMapping
    public ResponseEntity<ChatroomModel> createChat(@RequestBody ChatroomModel chatroomModel) {
        ChatroomModel savedChat = chatroomService.createChatroom(chatroomModel);
        return new ResponseEntity<>(savedChat, HttpStatus.CREATED);
    }

    // Get all chatrooms with pagination
    @GetMapping
    public ResponseEntity<Page<ChatroomModel>> getAllChat(Pageable pageable) {
        Page<ChatroomModel> chatList = chatroomService.getAllChatroom(pageable);
        return ResponseEntity.ok(chatList);
    }

    // Get chatrooms by client or description
    @GetMapping("/{client}")
    public ResponseEntity<Page<ChatroomModel>> getChatsByClient(
            @PathVariable String client,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Page<ChatroomModel> chatrooms = chatroomService.getChatroom(client, PageRequest.of(page, size));
        return ResponseEntity.ok(chatrooms);
    }

    // Update chatroom status
    @PutMapping("/{client}")
    public ResponseEntity<?> updateChatroom(@PathVariable String client) {
        ChatroomModel updated = chatroomService.updateChatroom(client);
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chatroom not found");
        }
        return ResponseEntity.ok(updated);
    }

    // Delete chatroom
    @DeleteMapping("/{client}")
    public ResponseEntity<?> deleteChat(@PathVariable String client) {
        chatroomService.deleteChatroom(client);
        return ResponseEntity.ok("Chat deleted successfully");
    }
}
