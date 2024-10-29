package com.bit.finalproject.repository;

import com.bit.finalproject.entity.ChatAlarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatAlarmRepository extends JpaRepository<ChatAlarm, Long> {
    ChatAlarm findByRoomIdAndUserId(long roomId,long userId);
}
