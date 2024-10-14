package com.bit.finalproject.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class NotificationDto {

    private final Long notificationId;
    private Long alarmUserId; // 알림 대상 사용자 ID
    private String alarmContent; // 알림 내용
    private Long alarmTargetId; // 알림 대상 ID (댓글, 게시물 등)
    private String alarmType; // 알림 유형 (댓글, 좋아요 등)
    private LocalDateTime createdAlarmTime; // 알림 생성 시간
    private boolean isRead; // 알림이 읽혔는지 여부

    // 생성자를 public으로 수정
    public NotificationDto(Long notificationId,  Long alarmUserId, String alarmContent, Long alarmTargetId, String alarmType, LocalDateTime createdAlarmTime, boolean isRead) {
        this.notificationId = notificationId;
        this.alarmUserId = alarmUserId;
        this.alarmContent = alarmContent;
        this.alarmTargetId = alarmTargetId;
        this.alarmType = alarmType;
        this.createdAlarmTime = createdAlarmTime;
        this.isRead = isRead;
    }
}
