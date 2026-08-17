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
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name = "feedback_name")
    private String name;
    @NotNull
    @Column(name = "feedback_email")
    private String email;
    @NotNull
    @Column(name = "feedback_phone")
    private String phone;
    @NotNull
    @Column(name = "feedback_subject")
    private String subject;
    @NotNull
    @Column(name = "feedback_message")
    private String message;
    @NotNull
    @Column(name = "feedback_status")
    private String status;
    @NotNull
    @Column(name = "feedback_date")
    private String date;
}
