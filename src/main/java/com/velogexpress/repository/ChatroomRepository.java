package com.velogexpress.repository;

import com.velogexpress.entity.Chatroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom,Long> {
    @Query(value = "SELECT c.id,c.chat_date,c.client_id,c.destination,c.new_status,cr.id as code,cr.usercode,cr.email,cr.full_name," +
            "cd.chat_id,cd.status FROM chatroom c left join client_register cr on cr.id=c.client_id order by id desc", nativeQuery = true)
    Page<Chatroom> getAllChat(Pageable pageable);
    @Query(value = "SELECT c.id,c.chat_date,c.client_id,c.destination,c.new_status,cr.id as code,cr.usercode,cr.email,cr.full_name FROM chatroom c " +
            " left join client_register cr on cr.id=c.client_id where cr.usercode=?1 or c.destination=?1 order by id desc", nativeQuery = true)
    Page<Chatroom> getChats(String client, Pageable pageable);
    @Query(value = "SELECT c.id,c.chat_date,c.client_id,c.destination,c.new_status,cr.id as code,cr.usercode,cr.email,cr.full_name FROM chatroom c " +
            " left join client_register cr on cr.id=c.client_id where cr.usercode=?1 or email=?1 ", nativeQuery = true)
    Chatroom getChat(String client);
}
