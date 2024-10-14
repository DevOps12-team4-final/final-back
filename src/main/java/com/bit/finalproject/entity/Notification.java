package com.bit.finalproject.entity;

import com.bit.finalproject.dto.NotificationDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notificationId")  // Primary Key
    private Long notificationId;

    @Column(name = "alarmUserId", nullable = false)  // 알람 대상 사용자 ID
    private Long alarmUserId;

    @Column(name = "alarmContent", nullable = false)  // 알람 내용
    private String alarmContent;

    @Column(name = "alarmTargetId", nullable = false)  // 알람 대상 ID (댓글이나 게시물)
    private Long alarmTargetId;

    // 알람 타입 (댓글, 댓글 좋아요, 게시물 등)
    @Column(name = "alarmType", nullable = false)
    private String alarmType;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAlarmTime; // 알림 생성 시간

    // 알림을 읽었는지 여부를 저장 (알림이 읽혔는지 확인하기 위해 사용)
    @Column(nullable = false)
    private boolean isRead;


    // NotificationDto로 변환하는 메서드 수정
    public NotificationDto toDto() {
        return NotificationDto.builder()
                .alarmUserId(this.alarmUserId)  // 알림을 받은 사용자 ID
                .alarmContent(this.alarmContent)  // 알림 내용
                .alarmTargetId(this.alarmTargetId)  // 알림 대상 ID (댓글이나 게시물)
                .alarmType(this.alarmType)  // 알림 유형 (댓글, 좋아요 등)
                .createdAlarmTime(this.createdAlarmTime)  // 알림 생성 시간
                .isRead(this.isRead)  // 알림이 읽혔는지 여부
                .build();
    }
}
