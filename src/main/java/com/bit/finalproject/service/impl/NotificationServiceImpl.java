package com.bit.finalproject.service.impl;


import com.bit.finalproject.dto.NotificationDto;
import com.bit.finalproject.entity.Notification;
import com.bit.finalproject.repository.NotificationRepository;
import com.bit.finalproject.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * NotificationServiceImpl는 NotificationService의 구현체로,
 * 알림 관련 비즈니스 로직을 처리하는 클래스입니다.
 */
@Service // Spring 빈으로 등록되기 위한 어노테이션
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;


    @Override
    public List<NotificationDto> getNotificationsForUser(Long alarmUserId) {
        List<Notification> notifications = notificationRepository.findByUserId(alarmUserId);
        return notifications.stream()
                .map(Notification::toDto) // 엔티티를 DTO로 변환
                .collect(Collectors.toList());
    }

    @Override
    public NotificationDto createNotification(NotificationDto notificationDto) {
        return null;
    }

    @Override
    public void markNotificationAsRead(Long userId,Long url,String type) {
        notificationRepository.deleteByUserIdAndUrlAndType(userId,url,type);
    }
}
