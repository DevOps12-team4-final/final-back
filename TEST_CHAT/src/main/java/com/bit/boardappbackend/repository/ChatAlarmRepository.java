package com.bit.finalproject.repository;

import com.bit.finalproject.entity.ChatAlarm;
import com.bit.finalproject.entity.Room;
import com.bit.finalproject.entity.RoomChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatAlarmRepository extends JpaRepository<ChatAlarm, Long> {

    @Query("UPDATE ChatAlarm c SET c.message = :message, c.count = c.count + 1 " +
            "WHERE c.room_id = :roomId AND c.user_id != :userId")
    void updateAlarm(long roomId, long userId, String message);
}
