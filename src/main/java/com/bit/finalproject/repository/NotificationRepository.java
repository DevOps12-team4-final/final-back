package com.bit.finalproject.repository;

import com.bit.finalproject.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * 특정 사용자의 수신된 알림 리스트를 가져오는 메서드
     *
     * @param alarmUserId 수신자의 ID
     * @return 수신자에게 해당하는 알림 리스트
     */
    List<Notification> findByAlarmUserId(Long alarmUserId);
}
