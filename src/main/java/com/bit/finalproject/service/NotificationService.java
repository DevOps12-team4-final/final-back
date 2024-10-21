package com.bit.finalproject.service;


import com.bit.finalproject.dto.NotificationDto;
import com.bit.finalproject.entity.Notification;
import com.bit.finalproject.entity.User;

import java.util.List;

public interface NotificationService {
    /**
     * 특정 사용자에게 전송된 알림 목록을 가져옵니다.
     *
     * @param receiverId 알림을 받을 사용자의 ID
     * @return 사용자의 알림 목록을 반환
     */
    List<NotificationDto> getNotificationsForUser(Long receiverId);

    /**
     * 새로운 알림을 생성합니다.
     *
     * @param notificationDto 알림 생성에 필요한 데이터
     * @return 생성된 알림의 DTO
     */
    NotificationDto createNotification(NotificationDto notificationDto);

    /**
     * 특정 알림을 읽음 상태로 표시합니다.
     *
     * @param notificationId 읽음 상태로 변경할 알림의 ID
     */
    void markNotificationAsRead(Long notificationId);
}
