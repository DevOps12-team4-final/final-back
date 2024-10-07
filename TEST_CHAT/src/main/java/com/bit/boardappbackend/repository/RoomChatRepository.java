package com.bit.boardappbackend.repository;

import com.bit.boardappbackend.entity.Room;
import com.bit.boardappbackend.entity.RoomChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomChatRepository extends JpaRepository<RoomChat, Long> {
    List<RoomChat> findByRoom_id(Long Id);
}
