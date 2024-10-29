package com.bit.finalproject.repository;

import com.bit.finalproject.entity.Room;
import com.bit.finalproject.entity.RoomChat;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoomRepository extends JpaRepository<Room, Long> {


}
