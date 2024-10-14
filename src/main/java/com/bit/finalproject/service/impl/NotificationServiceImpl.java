package com.bit.finalproject.service.impl;


import com.bit.finalproject.dto.NotificationDto;
import com.bit.finalproject.entity.Notification;
import com.bit.finalproject.repository.NotificationRepository;
import com.bit.finalproject.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * NotificationServiceImpl는 NotificationService의 구현체로,
 * 알림 관련 비즈니스 로직을 처리하는 클래스입니다.
 */
@Service // Spring 빈으로 등록되기 위한 어노테이션
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    /**
     * NotificationRepository를 생성자 주입 방식으로 주입받아 초기화합니다.
     *
     * @param notificationRepository 알림 데이터를 처리할 리포지토리
     */
    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * 특정 사용자에게 전송된 알림 목록을 가져옵니다.
     *
     * @param alarmUserId 알림을 받을 사용자의 ID
     * @return 사용자의 알림 목록을 반환
     */
    @Override
    public List<NotificationDto> getNotificationsForUser(Long alarmUserId) {
        List<Notification> notifications = notificationRepository.findByAlarmUserId(alarmUserId);
        return notifications.stream()
                .map(Notification::toDto) // 엔티티를 DTO로 변환
                .collect(Collectors.toList());
    }

    /**
     * 새로운 알림을 생성합니다.
     *
     * @param notificationDto 알림 생성에 필요한 데이터
     * @return 생성된 알림의 DTO
     */
    @Override
    public NotificationDto createNotification(NotificationDto notificationDto) {
        Notification notification = Notification.builder()
                .alarmContent(notificationDto.getAlarmContent())
                .alarmUserId(notificationDto.getAlarmUserId())
                .alarmTargetId(notificationDto.getAlarmTargetId())
                .alarmType(notificationDto.getAlarmType())
                .build();
        notificationRepository.save(notification); // 알림 저장
        return notification.toDto();
    }

    /**
     * 특정 알림을 읽음 상태로 표시합니다.
     *
     * @param notificationId 읽음 상태로 변경할 알림의 ID
     */
    @Override
    public void markNotificationAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found")); // 알림이 없을 경우 예외 처리
        notification.setRead(true); // 읽음 상태로 변경
        notificationRepository.save(notification); // 변경 사항 저장
    }
}
