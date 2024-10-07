package com.bit.boardappbackend.repository;

import com.bit.boardappbackend.entity.Room;
import com.bit.boardappbackend.entity.RoomChat;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoomRepository extends JpaRepository<Room, Long> {


}
