package com.velogexpress.repository;

import com.velogexpress.entity.ChatroomDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatroomDetailsRepository extends JpaRepository<ChatroomDetails,Long> {
    @Query(value = "SELECT * FROM chatroom_details  where chat_id=?1 order by id desc", nativeQuery = true)
    Page<ChatroomDetails> findChat(Long id, Pageable pageable);
    @Query(value = "SELECT count(id) AS id,conversation_date,message,status,chat_id,position FROM chatroom_details " +
            " where chat_id=?1 and status='U/R' group by chat_id order by id desc", nativeQuery = true)
    ChatroomDetails countChat(Long id);
}
