package com.velogexpress.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chatroom_details")
public class ChatroomDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name = "conversation_date")
    private String date;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chat_id")
    private Chatroom chat;
    @NotNull
    @Column(name = "message")
    private String message;
    @NotNull
    @Column(name = "status")
    private String status;
    @NotNull
    @Column(name = "position")
    private String position;
}
