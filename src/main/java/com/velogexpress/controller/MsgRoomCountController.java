package com.velogexpress.controller;

import com.velogexpress.model.ChatroomDetailsModel;
import com.velogexpress.service.ChatroomDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/chatroomdetailscount")
public class MsgRoomCountController {
    private ChatroomDetailsService chatroomDetailsService;
    //Build Get Room By Description
    @GetMapping("{id}")
    public ResponseEntity<ChatroomDetailsModel> getRoomCount(@PathVariable("id") Long id){
        ChatroomDetailsModel ChatroomDetailsModel=chatroomDetailsService.countRoom(id);
        return  ResponseEntity.ok(ChatroomDetailsModel);
    }
}
