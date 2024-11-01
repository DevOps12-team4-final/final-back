package com.bit.finalproject.repository;

import com.bit.finalproject.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserId(Long alarmUserId);

    void deleteByUserIdAndUrlAndType(Long userId, Long url, String type);
}
