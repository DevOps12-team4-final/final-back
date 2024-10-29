package com.bit.finalproject.repository;

import com.bit.finalproject.entity.RoomChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomChatRepository extends JpaRepository<RoomChat, Long> {
    List<RoomChat> findByRoom_id(Long Id);
}
